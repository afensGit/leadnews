package com.bin.crawler.process.parse.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bin.common.common.util.HMStringUtils;
import com.bin.common.kafka.KafkaSender;
import com.bin.common.kafka.messages.SubmitArticleAuthMessage;
import com.bin.crawler.process.parse.AbstractHtmlParsePipeline;
import com.bin.crawler.process.thread.CrawlerThreadPool;
import com.bin.crawler.service.AdLabelService;
import com.bin.crawler.service.CrawlerNewsAdditionalService;
import com.bin.crawler.service.CrawlerNewsCommentService;
import com.bin.crawler.service.CrawlerNewsService;
import com.bin.crawler.utils.DateUtils;
import com.bin.crawler.utils.HtmlParser;
import com.bin.model.crawler.core.label.HtmlLabel;
import com.bin.model.crawler.core.parse.ParseItem;
import com.bin.model.crawler.core.parse.ZipUtils;
import com.bin.model.crawler.core.parse.impl.CrawlerParseItem;
import com.bin.model.crawler.enums.CrawlerEnum;
import com.bin.model.crawler.pojos.ClNews;
import com.bin.model.crawler.pojos.ClNewsAdditional;
import com.bin.model.crawler.pojos.ClNewsComment;
import com.bin.model.mess.admin.SubmitArticleAuto;
import com.bin.utils.common.ReflectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author huangbin
 */

@Component
@Slf4j
public class CrawlerHtmlParsePipeline extends AbstractHtmlParsePipeline<CrawlerParseItem> {

    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("crawler");

    private static final String csdn_comment_url = resourceBundle.getString("csdn.comment.url");

    /**
     * 下次更新时间
     */
    private static final String[] next_update_hour_array = resourceBundle.getString("crawler.nextupdatehours").split(",");


    @Autowired
    private KafkaSender kafkaSender;

    @Autowired
    private CrawlerNewsService crawlerNewsService;

    @Autowired
    private CrawlerNewsCommentService clNewsCommentService;

    @Autowired
    private CrawlerNewsAdditionalService crawlerNewsAdditionalService;


    @Autowired
    private AdLabelService adLabelService;

    /**
     * 前置参数处理
     * 阅读数 200
     * @param parameter
     */
    @Override
    public void preParameterHandel(Map<String, Object> parameter) {
        String readCount = HMStringUtils.toString(parameter.get("readCount"));
        if (StringUtils.isNotEmpty(readCount)) {
            readCount = readCount.split(" ")[0];
            if (StringUtils.isNotEmpty(readCount)) {
                parameter.put("readCount", readCount);
            }
        }
    }

    /**
     * Html 数据处理的入口
     *
     * @param parseItem
     */
    @Override
    public void handelHtmlData(final CrawlerParseItem parseItem) {
        long currentTime = System.currentTimeMillis();
        log.info("将数据加入线程池进行执行，url:{},handelType:{}", parseItem.getUrl(), parseItem.getHandelType());
        CrawlerThreadPool.submit(() -> {
            //正向抓取
            if (CrawlerEnum.HandelType.FORWARD.name().equals(parseItem.getHandelType())) {
                log.info("开始处理消息,url:{},handelType:{}", parseItem.getUrl(), parseItem.getHandelType());
                //kafkaSender.sendCrawlerParseItemMessage(parseItem);
                addParseItemMessage(parseItem);
                //逆向抓取
            } else if (CrawlerEnum.HandelType.REVERSE.name().equals(parseItem.getHandelType())) {
                //更新附加数据
                updateAdditional(parseItem);
            }
            log.info("处理文章数据完成，url:{},handelType:{}，耗时：{}", parseItem.getUrl(), parseItem.getHandelType(), System.currentTimeMillis() - currentTime);
        });

    }

    /**
     * 添加解析后的消息
     *
     * @throws
     * @throws InterruptedException
     */
    public void addParseItemMessage(CrawlerParseItem parseItem) {
        long currentTime = System.currentTimeMillis();
        String url = null;
        String handelType = null;
        if (null != parseItem) {
            url = parseItem.getUrl();
            handelType = parseItem.getHandelType();
            log.info("开始添加数据,url:{},handelType:{}", url, parseItem.getHandelType());
            //添加文章数据
            ClNews clNews = addClNewsData(parseItem);
            if (null != clNews) {
                //添加附加数据
                addAdditional(parseItem, clNews);
                //添加回复数据 只有评论数>0 才能进行添加
                if (null != parseItem && null != parseItem.getCommentCount() && parseItem.getCommentCount() > 0) {
                    addCommentData(parseItem, clNews);
                }
                sendSubmitArticleAutoMessage(clNews.getId());
            }
        }
        log.info("添加数据完成,url:{},handelType:{},耗时:{}", url, handelType, System.currentTimeMillis() - currentTime);
    }

    /**
     * 发起自动主动审核
     *
     * @param clNewId
     */
    public void sendSubmitArticleAutoMessage(Integer clNewId) {
        log.info("开始发送自动审核消息,id:{}", clNewId);
        SubmitArticleAuto submitArticleAuto = new SubmitArticleAuto();
        submitArticleAuto.setArticleId(clNewId);
        submitArticleAuto.setType(SubmitArticleAuto.ArticleType.CRAWLER);
        SubmitArticleAuthMessage submitArticleAuthMessage = new SubmitArticleAuthMessage(submitArticleAuto);
        kafkaSender.sendSubmitArticleAuthMessage(submitArticleAuthMessage);
        log.info("发送自动审核消息完成,id:{}", clNewId);
    }

    /**
     * 文章内容的保存处理
     *
     * @param parseItem
     * @return
     */
    private ClNews addClNewsData(CrawlerParseItem parseItem) {
        log.info("开始添加文章内容");
        ClNews clNews = null;
        if (null != parseItem) {
            HtmlParser htmlParser = HtmlParser.getHtmlParser(getParseExpression(), getDefHtmlStyleMap());
            //将Html内容转换为HtmlLabel 对象类别
            List<HtmlLabel> htmlLabelList = htmlParser.parseHtml(parseItem.getContent());
            //获取文章类型
            int type = getType(htmlLabelList);
            parseItem.setDocType(type);
            String jsonStr = JSON.toJSONString(htmlLabelList);
            parseItem.setCompressContent(ZipUtils.gzip(jsonStr));
            ClNewsAdditional clNewsAdditional = crawlerNewsAdditionalService.getAdditionalByUrl(parseItem.getUrl());
            if (null == clNewsAdditional) {
                clNews = toClNews(parseItem);
                long currentTime = System.currentTimeMillis();
                log.info("开始插入新的文章");
                crawlerNewsService.saveNews(clNews);
                log.info("插入新的文章完成，耗时：{}", System.currentTimeMillis() - currentTime);
            } else {
                log.info("文章URL已存在不重复添加，URL：{}", clNewsAdditional.getUrl());
            }
        }
        log.info("添加文章内容完成");
        return clNews;
    }


    /**
     * 处理文章附加信息
     *
     * @param parseItem
     * @param clNews
     */
    public void addAdditional(CrawlerParseItem parseItem, ClNews clNews) {
        long currentTime = System.currentTimeMillis();
        log.info("开始处理文章附加数据");
        if (null != parseItem && null != clNews) {
            ClNewsAdditional clNewsAdditional = toClNewsAdditional(parseItem, clNews);
            crawlerNewsAdditionalService.saveAdditional(clNewsAdditional);
        }
        log.info("文章附加数据处理完成,耗时：{}", System.currentTimeMillis() - currentTime);
    }

    /**
     * 逆向数据更新
     *
     * @param parseItem
     */
    public void updateAdditional(CrawlerParseItem parseItem) {
        long currentTime = System.currentTimeMillis();
        log.info("开始更新文章附加数据");
        if (null != parseItem) {
            ClNewsAdditional clNewsAdditional = crawlerNewsAdditionalService.getAdditionalByUrl(parseItem.getUrl());
            if (null != clNewsAdditional) {
                clNewsAdditional.setNewsId(null);
                clNewsAdditional.setUrl(null);
                //阅读量设置
                clNewsAdditional.setReadCount(parseItem.getReadCount());
                //评论数设置
                clNewsAdditional.setComment(parseItem.getCommentCount());
                //点赞数设置
                clNewsAdditional.setLikes(parseItem.getLikes());
                //更新数据设置
                clNewsAdditional.setUpdatedTime(new Date());
                clNewsAdditional.setUpdateNum(clNewsAdditional.getUpdateNum() + 1);
                int nextUpdateHours = getNextUpdateHours(clNewsAdditional.getUpdateNum());
                clNewsAdditional.setNextUpdateTime(DateUtils.addHours(new Date(), nextUpdateHours));
                crawlerNewsAdditionalService.updateAdditional(clNewsAdditional);
            }
        }
        log.info("更新文章附加数据完成，耗时：{}", System.currentTimeMillis() - currentTime);
    }

    /**
     * 处理评论数据
     *
     * @param parseItem
     */
    public void addCommentData(CrawlerParseItem parseItem, ClNews clNews) {
        long currentTime = System.currentTimeMillis();
        log.info("开始获取文章评论数据");
        List<ClNewsComment> commentList = getCommentData(parseItem);
        if (null != commentList && !commentList.isEmpty()) {
            for (ClNewsComment comment : commentList) {
                comment.setNewsId(clNews.getId());
                clNewsCommentService.saveClNewsComment(comment);
            }
        }
        log.info("获取文章评论数据完成，耗时：{}", System.currentTimeMillis() - currentTime);
    }

    /**
     * 转换为数据库对象
     *
     * @param parseItem
     * @return
     */
    private ClNews toClNews(CrawlerParseItem parseItem) {
        ClNews clNews = new ClNews();
        clNews.setName(parseItem.getAuthor());
        clNews.setLabels(parseItem.getLabels());
        clNews.setContent(parseItem.getCompressContent());
        clNews.setLabelIds(adLabelService.getLabelIds(parseItem.getLabels()));
        Integer channelId = adLabelService.getAdChannelByLabelIds(clNews.getLabels());
        clNews.setChannelId(channelId);
        clNews.setTitle(parseItem.getTitle());
        clNews.setType(parseItem.getDocType());
        clNews.setStatus((byte) 1);
        clNews.setCreatedTime(new Date());
        String releaseDate = parseItem.getReleaseDate();
        if (StringUtils.isNotEmpty(releaseDate)) {
            clNews.setOriginalTime(DateUtils.stringToDate(releaseDate, DateUtils.DATE_TIME_FORMAT));
        }
        return clNews;
    }


    /**
     * 转换为文章附加信息
     *
     * @param parseItem
     * @param clNews
     * @return
     */
    private ClNewsAdditional toClNewsAdditional(CrawlerParseItem parseItem, ClNews clNews) {
        ClNewsAdditional clNewsAdditional = null;
        if (null != parseItem) {
            clNewsAdditional = new ClNewsAdditional();
            //设置文章ID
            clNewsAdditional.setNewsId(clNews.getId());
            //设置阅读数
            clNewsAdditional.setReadCount(parseItem.getReadCount());
            //设置回复数
            clNewsAdditional.setComment(parseItem.getCommentCount());
            //设置点赞数
            clNewsAdditional.setLikes(parseItem.getLikes());
            //设置URL
            clNewsAdditional.setUrl(parseItem.getUrl());
            //设置更新时间
            clNewsAdditional.setUpdatedTime(new Date());
            //设置创建时间
            clNewsAdditional.setCreatedTime(new Date());
            //设置更新次数
            clNewsAdditional.setUpdateNum(0);
            //设置下次更新时间
            int nextUpdateHour = getNextUpdateHours(clNewsAdditional.getUpdateNum());
            /**
             * 设置下次更新时间
             */
            clNewsAdditional.setNextUpdateTime(DateUtils.addHours(new Date(), nextUpdateHour));
        }
        return clNewsAdditional;
    }

    /**
     * 获取下次更新时间
     *
     * @param count
     * @return
     */
    private int getNextUpdateHours(Integer count) {
        if (null != next_update_hour_array && next_update_hour_array.length > count) {
            return Integer.parseInt(next_update_hour_array[count]);
        } else {
            return 2 << count;
        }
    }

    /**
     * 获取图文类型
     * <p>
     * 0：无图片
     * 1：单图
     * 2：多图
     *
     * @param htmlLabelList
     * @return
     */
    public int getType(List<HtmlLabel> htmlLabelList) {
        int type = 0;
        int num = 0;
        if (null != htmlLabelList && !htmlLabelList.isEmpty()) {
            for (HtmlLabel htmlLabel : htmlLabelList) {
                if (CrawlerEnum.HtmlType.IMG_TAG.getDataType().equals(htmlLabel.getType())) {
                    num++;
                }
            }
        }
        if (num == 0) {
            type = 0;
        } else if (num == 1) {
            type = 1;
        } else {
            type = 2;
        }
        return type;
    }


    /**
     * 获取评论列表
     *
     * @param parseItem
     * @return
     */
    private List<ClNewsComment> getCommentData(ParseItem parseItem) {
        //构建评论的URl
        String buildCommentUrl = buildCommentUrl(parseItem);
        //调用父类方法进行HttpClient请求进行获取数据
        String jsonData = getOriginalRequestJsonData(buildCommentUrl, null);
        //解析获取的JSON数据
        List<ClNewsComment> commentList = analysisCommentJsonData(jsonData);
        return commentList;
    }

    /**
     * 解析评论数据
     *
     * @param jsonData
     * @return
     */

    public List<ClNewsComment> analysisCommentJsonData(String jsonData) {
        if (StringUtils.isEmpty(jsonData)) {
            return null;
        }
        List<ClNewsComment> commentList = new ArrayList<ClNewsComment>();
        JSONObject jsonObject = JSON.parseObject(jsonData);
        Map<String, Object> map = jsonObject.getObject("data", Map.class);
        JSONArray jsonArray = (JSONArray) map.get("list");
        if (null != jsonArray) {
            List<Map> dataInfoList = jsonArray.toJavaList(Map.class);
            for (Map<String, Object> dataInfo : dataInfoList) {
                JSONObject infoObject = (JSONObject) dataInfo.get("info");
                Map<String, Object> infoMap = infoObject.toJavaObject(Map.class);
                ClNewsComment comment = new ClNewsComment();
                comment.setContent(HMStringUtils.toString(infoMap.get("Content")));
                comment.setUsername(HMStringUtils.toString(infoMap.get("UserName")));
                Date date = DateUtils.stringToDate(HMStringUtils.toString(infoMap.get("PostTime")), DateUtils.DATE_TIME_FORMAT);
                comment.setCommentDate(date);
                comment.setCreatedDate(new Date());
                commentList.add(comment);
            }
        }
        return commentList;
    }

    /**
     * 生成评论访问链接
     *
     * @param parseItem
     * @return
     */
    private String buildCommentUrl(ParseItem parseItem) {
        String buildCommentUrl = csdn_comment_url;
        Map<String, Object> map = ReflectUtils.beanToMap(parseItem);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            String buildKey = "${" + key + "}";
            Object value = entry.getValue();
            if (null != value) {
                String strValue = value.toString();
                buildCommentUrl = buildCommentUrl.replace(buildKey, strValue);
            }
        }
        return buildCommentUrl;
    }


    @Override
    public int getPriority() {
        return 1000;
    }
}
