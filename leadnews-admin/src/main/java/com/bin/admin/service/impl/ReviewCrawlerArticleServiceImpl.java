package com.bin.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bin.admin.service.ReviewCrawlerArticleService;
import com.bin.common.admin.constans.AdminConstans;
import com.bin.common.aliyun.AliyunImageScanRequest;
import com.bin.common.aliyun.AliyunTextScanRequest;
import com.bin.common.common.pojo.EsIndexEntity;
import com.bin.common.common.util.HMStringUtils;
import com.bin.model.admin.pojos.AdChannel;
import com.bin.model.article.pojos.*;
import com.bin.model.common.constants.ESIndexConstants;
import com.bin.model.crawler.core.parse.ZipUtils;
import com.bin.model.crawler.pojos.ClNews;
import com.bin.model.mappers.admin.AdChannelMapper;
import com.bin.model.mappers.app.*;
import com.bin.model.mappers.crawlers.ClNewsMapper;
import com.bin.utils.common.Compute;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author huangbin
 */
@Service
@Slf4j
public class ReviewCrawlerArticleServiceImpl implements ReviewCrawlerArticleService {

    @Autowired
    private ClNewsMapper clNewsMapper;

    @Autowired
    private ApArticleLabelMapper apArticleLabelMapper;

    @Autowired
    private AliyunTextScanRequest aliyunTextScanRequest;

    @Autowired
    private AliyunImageScanRequest aliyunImageScanRequest;

    @Autowired
    private AdChannelMapper adChannelMapper;

    @Autowired
    private ApAuthorMapper apAuthorMapper;

    @Autowired
    private JestClient jestClient;

    @Autowired
    private ApArticleContentMapper apArticleContentMapper;

    @Autowired
    private ApArticleConfigMapper apArticleConfigMapper;

    @Autowired
    private ApArticleMapper apArticleMapper;


    @Override
    public void autoReviewArticleByCrawler(ClNews clNews) throws Exception {
        long currentTime = System.currentTimeMillis();
        log.info("开始自动审核流程");
        if (clNews != null) {
            log.info("开始内容审核流程");
            String content = clNews.getUnCompressContent();
            String title = clNews.getTitle();
            //2.检查文章标题与内容的匹配度
            if (content == null || title == null) {
                updateClNews(clNews, "文章内容或标题为空");
                return;
            }
            //设置固定长度
            title = HMStringUtils.getFixedLengthStr(title, 50);
            double degree = Compute.SimilarDegree(content, title);
            if (degree <= 0) {
                //文章标题与内容匹配
                updateClNews(clNews, "文章标题与内容不匹配");
                return;
            }
            log.info("开始文本审核流程");
            //3.审核文本
            List<String> images = new ArrayList<>();
            StringBuilder builder = new StringBuilder();
            JSONArray jsonArray = JSON.parseArray(content);
            handlerTextAndImages(images, builder, jsonArray);
            //截取之前五张图片
            images = HMStringUtils.getFixedLengthContentList(images, 1000, 2);
            String response = aliyunTextScanRequest.textScanRequest(builder.toString());
            //人工审核
            if ("review".equals(response)) {
                return;
            }
            //审核失败
            if ("block".equals(response)) {
                updateClNews(clNews, "文本内容审核失败");
                return;
            }
            //审核文章中的图片信息，阿里接口
            String imagesResponse = aliyunImageScanRequest.imageScanRequest(images);
            if (imagesResponse != null) {
                if ("review".equals(imagesResponse)) {//人工审核
                    return;
                }
                if ("block".equals(imagesResponse)) {//审核失败
                    updateClNews(clNews, "文本内容审核失败");
                    return;
                }
            }
//            else {
//                updateClNews(clNews, "图片审核出现问题");
//                return;
//            }

            //5.审核通过 存入数据   ap_article_config   ap_article   ap_article_content  ap_author
            Integer channelId = clNews.getChannelId();
            String channelName = "";
            if (null != channelId) {
                AdChannel adChannel = adChannelMapper.selectByPrimaryKey(channelId);
                if (adChannel != null) {
                    channelName = adChannel.getName();
                }
            }

            int type = clNews.getType();//布局类型 0 无图 1 单图  2 多图
            log.info("文章作者入库");
            //查询是否已经存在作者信息，如果不存在则插入一条新数据
            ApAuthor apAuthor = saveApAuthor(clNews);

            //文章信息表，存储已发布的文章
            log.info("保存文章数据");
            ApArticle apArticle = saveApArticleByCrawler(images, channelId, channelName, apAuthor.getId(), clNews);
            //保存标签
            saveApArticleLabel(apArticle);
            //APP已发布文章配置表
            ApArticleConfig apArticleConfig = saveApArticleConfigByCrawler(apArticle);
            //APP已发布文章内容表
            saveApArticleContentByCrawler(clNews.getContent(), apArticle);

            log.info("开始创建索引");
            //6.创建索引
            try {
                createEsIndex(apArticle, content, title, channelId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //修改状态为审核通过待发布
            log.info("更新原始文章状态为待发布");
            updateClNewsSuccess(clNews);
        }
        log.info("审核流程结束，耗时：{}", System.currentTimeMillis() - currentTime);
    }

    @Override
    public void autoReviewArticleByCrawler() throws Exception {
        ClNews param = new ClNews();
        param.setStatus((byte) 1);
        List<ClNews> clNewsList = clNewsMapper.selectList(param);

        if (null != clNewsList && !clNewsList.isEmpty()) {
            log.info("定时任务自动审核检索未审核数量：{}", clNewsList.size());
            for (ClNews clNews : clNewsList) {
                autoReviewArticleByCrawler(clNews);
            }
        } else {
            log.info("定时任务自动审核未检索出数据");
        }
    }

    @Override
    public void autoReviewArticleByCrawler(Integer clNewsId) throws Exception {
        //1.查询待审核的列表
        ClNews param = new ClNews();
        param.setId(clNewsId);
        param.setStatus((byte) 1);
        ClNews clNews = clNewsMapper.selectByIdAndStatus(param);
        if (null != clNews) {
            autoReviewArticleByCrawler(clNews);
        }
    }

    private ApAuthor saveApAuthor(ClNews clNews) {
        ApAuthor apAuthor = apAuthorMapper.selectByName(clNews.getName());
        if (apAuthor == null || apAuthor.getId() == null) {
            apAuthor = new ApAuthor();
            apAuthor.setCreatedTime(clNews.getCreatedTime());
            apAuthor.setName(clNews.getName());
            apAuthor.setType(2);
            apAuthorMapper.insert(apAuthor);
        }
        return apAuthor;
    }


    private void createEsIndex(ApArticle apArticle, String content, String title, Integer channelId) throws IOException {
        EsIndexEntity esIndexEntity = saveEsIndexEntityByCrawler(content, title, channelId, apArticle);
        Index.Builder builder = new Index.Builder(esIndexEntity);
        builder.id(apArticle.getId().toString());
        builder.refresh(true);
        Index index = builder.index(ESIndexConstants.ARTICLE_INDEX).type(ESIndexConstants.DEFAULT_DOC).build();
        JestResult result = jestClient.execute(index);
        if (result != null && !result.isSucceeded()) {
            throw new RuntimeException(result.getErrorMessage() + "插入更新索引失败!");
        }
    }

    /**
     * 创建索引信息 crawler
     *
     * @param content
     * @param title
     * @param channelId
     * @param apArticle
     * @return
     */
    private EsIndexEntity saveEsIndexEntityByCrawler(String content, String title, Integer channelId, ApArticle apArticle) {
        EsIndexEntity esIndexEntity = new EsIndexEntity();
        esIndexEntity.setId(new Long(apArticle.getId()));
        if (null != channelId) {
            esIndexEntity.setChannelId(new Long(channelId));
        }
        esIndexEntity.setContent(content);
        esIndexEntity.setPublishTime(new Date());
        esIndexEntity.setStatus(new Long(1));
        esIndexEntity.setTitle(title);
        esIndexEntity.setTag(AdminConstans.ES_INDEX_TAG_ARTICLE);
        return esIndexEntity;
    }

    /**
     * 保存文章作者信息  crawler
     *
     * @param createdTime
     * @param authorName
     * @return
     */
    private ApAuthor saveApAuthorByCrawler(Date createdTime, String authorName) {
        ApAuthor apAuthor = new ApAuthor();
        apAuthor.setCreatedTime(createdTime);
        apAuthor.setName(authorName);
        apAuthor.setType(2);
        apAuthorMapper.insert(apAuthor);
        return apAuthor;
    }

    /**
     * 保存ApArticleContent  爬虫系统
     *
     * @param content
     * @param apArticle
     * @return
     */
    private void saveApArticleContentByCrawler(String content, ApArticle apArticle) {
        ApArticleContent apArticleContent = new ApArticleContent();
        apArticleContent.setArticleId(apArticle.getId());
        apArticleContent.setContent(ZipUtils.gzip(content ));
        apArticleContentMapper.insert(apArticleContent);
    }

    /**
     * 保存ApArticleConfig  crawler 爬虫系统
     *
     * @param apArticle
     * @return
     */
    private ApArticleConfig saveApArticleConfigByCrawler(ApArticle apArticle) {
        ApArticleConfig apArticleConfig = new ApArticleConfig();
        apArticleConfig.setArticleId(apArticle.getId());
        apArticleConfig.setIsComment(true);
        apArticleConfig.setIsDelete(false);
        apArticleConfig.setIsDown(false);
        apArticleConfig.setIsForward(true);
        apArticleConfigMapper.insert(apArticleConfig);
        return apArticleConfig;
    }

    /**
     * 保存文章信息  爬虫系统
     * @param images
     * @param channelId
     * @param channelName
     * @param authorId
     * @param clNews
     * @return
     */
    private ApArticle saveApArticleByCrawler(List<String> images, Integer channelId, String channelName, Integer authorId, ClNews clNews) {
        ApArticle apArticle = new ApArticle();
        apArticle.setChannelId(channelId);
        apArticle.setChannelName(channelName);
        apArticle.setAuthorName(clNews.getName());
        apArticle.setCreatedTime(clNews.getCreatedTime());
        apArticle.setOrigin(false);
        StringBuilder sb = new StringBuilder();
        Short layout = 0;
        for (int i = 0; i < images.size() && i < 3; i++) {
            if (i != 0) {
                sb.append(",");
            }
            layout++;
            sb.append(images.get(i));
        }
        apArticle.setImages(sb.toString());
        apArticle.setLabels(clNews.getLabels());
        apArticle.setTitle(clNews.getTitle());
        apArticle.setPublishTime(new Date());
        apArticle.setAuthorId(new Long(authorId));
        apArticle.setLayout(layout);
        long currentTime = System.currentTimeMillis();
        log.info("开始插入ApArticle表");
        apArticleMapper.insert(apArticle);
        log.info("插入ApArticle表完成,耗时：{}", System.currentTimeMillis() - currentTime);
        return apArticle;
    }

    /**
     * 保存标签
     *
     * @param apArticle
     */
    private void saveApArticleLabel(ApArticle apArticle) {
        if (null != apArticle && StringUtils.isNotEmpty(apArticle.getLabels())) {
            String[] labelIdArray = apArticle.getLabels().split(",");
            for (String labelId : labelIdArray) {
                ApArticleLabel tmp = new ApArticleLabel(apArticle.getId(), Integer.parseInt(labelId));
                List<ApArticleLabel> apArticleLabelList = apArticleLabelMapper.selectList(tmp);
                if (null != apArticleLabelList && !apArticleLabelList.isEmpty()) {
                    ApArticleLabel apArticleLabel = apArticleLabelList.get(0);
                    apArticleLabel.setCount(apArticleLabel.getCount() + 1);
                    apArticleLabelMapper.updateByPrimaryKeySelective(apArticleLabel);
                } else {
                    tmp.setCount(1);
                    apArticleLabelMapper.insertSelective(tmp);
                }
            }
        }
    }

    /**
     * 文章审核失败，更新原有库状态及告知原因
     *
     * @param clNews
     * @param reason
     */
    private void updateClNews(ClNews clNews, String reason) {
        clNews.setStatus((byte) 2);
        clNews.setReason(reason);
        clNewsMapper.updateStatus(clNews);
    }

    /**
     * 文章审核成功
     * 因为mq 不能保证实时，可能存在重复读取的问题，所以优先修改状态
     *
     * @param clNews
     */
    private void updateClNewsSuccess(ClNews clNews) {
        clNews.setStatus((byte) 9);
        clNewsMapper.updateStatus(clNews);
    }

    /**
     * 处理content  找出文本和图片列表
     *
     * @param images
     * @param sb
     * @param jsonArray
     */
    private void handlerTextAndImages(List<String> images, StringBuilder sb, JSONArray jsonArray) {
        for (Object obj : jsonArray) {
            JSONObject jsonObj = (JSONObject) obj;
            String type = (String) jsonObj.get("type");
            if ("image".equals(type)) {
                String value = (String) jsonObj.get("value");
                images.add(value);
            }
            if ("text".equals(type)) {
                sb.append(jsonObj.get("value"));
            }
        }
    }
}
