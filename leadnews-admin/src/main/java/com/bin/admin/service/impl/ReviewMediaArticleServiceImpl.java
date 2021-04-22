package com.bin.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bin.admin.service.ReviewMediaArticleService;
import com.bin.common.aliyun.AliyunImageScanRequest;
import com.bin.common.aliyun.AliyunTextScanRequest;
import com.bin.common.common.pojo.EsIndexEntity;
import com.bin.model.admin.pojos.AdChannel;
import com.bin.model.article.pojos.ApArticle;
import com.bin.model.article.pojos.ApArticleConfig;
import com.bin.model.article.pojos.ApArticleContent;
import com.bin.model.article.pojos.ApAuthor;
import com.bin.model.common.constants.ESIndexConstants;
import com.bin.model.crawler.core.parse.ZipUtils;
import com.bin.model.mappers.admin.AdChannelMapper;
import com.bin.model.mappers.app.*;
import com.bin.model.mappers.wemedia.WmNewsMapper;
import com.bin.model.mappers.wemedia.WmUserMapper;
import com.bin.model.media.pojos.WmNews;
import com.bin.model.media.pojos.WmUser;
import com.bin.model.user.pojos.ApUserMessage;
import com.bin.utils.common.Compute;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author huangbin
 */

@Service
@Slf4j
public class ReviewMediaArticleServiceImpl implements ReviewMediaArticleService {

    private static String review = "review";

    private static String block = "block";

    @Autowired
    private WmNewsMapper wmNewsMapper;

    @Autowired
    private AliyunTextScanRequest textScanRequest;

    @Autowired
    private AliyunImageScanRequest imageScanRequest;

    @Autowired
    private WmUserMapper wmUserMapper;

    @Autowired
    private ApAuthorMapper apAuthorMapper;

    @Autowired
    private AdChannelMapper adChannelMapper;

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Autowired
    private ApArticleContentMapper apArticleContentMapper;

    @Autowired
    private ApArticleConfigMapper apArticleConfigMapper;

    @Autowired
    private ApUserMessageMapper apUserMessageMapper;

    @Autowired
    private JestClient jestClient;

    @Value("${FILE_SERVER_URL}")
    private String fileServerUrl;

    @Override
    public void autoReviewMediaArticle(Integer newsId) {
        //根据文章id查询文章详细信息
        WmNews wmNews = wmNewsMapper.selectNewsById(newsId);
        //判断自媒体文章状态
        if (wmNews != null){
            int status = wmNews.getStatus();
            switch (status){
                case 1:
                    //待审核状态，利用阿里云云盾内容安全审核文章
                    String content = wmNews.getContent();
                    String title = wmNews.getTitle();
                    //审核文章内容和标题是否符合
                    double degree = Compute.SimilarDegree(content, title);
                    if (degree <= 0){
                        updateNews(wmNews, (short)2, "文章标题已内容不符");
                        return;
                    }
                    //审核文本内容
                    List<String> images = new ArrayList<>();
                    StringBuilder builder = new StringBuilder();
                    JSONArray contentArray = JSON.parseArray(content);
                    //获取文章文本和图片
                    handlerTextAndImages(images, builder, contentArray);
                    try {
                        String response = textScanRequest.textScanRequest(builder.toString());
                        if (review.equals(response)){
                            updateNews(wmNews, (short) 3, "需要人工审核");
                            return;
                        }else if (block.equals(response)){
                            updateNews(wmNews, (short) 2, "文本信息违规，审核不通过");
                            return;
                        }
                        String imagesResponse = imageScanRequest.imageScanRequest(images);
                        if (review.equals(imagesResponse)){
                            updateNews(wmNews, (short) 3, "需要人工审核");
                            return;
                        }else if (block.equals(imagesResponse)){
                            updateNews(wmNews, (short) 2, "文本信息违规，审核不通过");
                            return;
                        }
//                        else {
//                            updateNews(wmNews, (short) 2, "图片审核出现问题");
//                            return;
//                        }

                    }catch (Exception e){
                        log.info("文章审核出现异常");
                        log.info("异常文章信息", e);
                    }
                    Date publishTime = wmNews.getPublishTime();
                    if (publishTime != null){
                        if (publishTime.getTime() > System.currentTimeMillis()){
                            updateNews(wmNews, (short) 8, "待发布");
                        }else {
                            //立即发布
                            reviewSuccessSaveAll(wmNews);
                        }
                    }else {
                        reviewSuccessSaveAll(wmNews);
                    }
                    break;

                case 4:
                    reviewSuccessSaveAll(wmNews);
                    return;

                case 8:
                    if (wmNews.getPublishTime() != null && wmNews.getPublishTime().getTime() < System.currentTimeMillis()){
                        reviewSuccessSaveAll(wmNews);
                    }
                    return;
                default:
                    return;
            }
        }
    }

    private void reviewSuccessSaveAll(WmNews wmNews) {
        ApAuthor author = null;
        if (wmNews.getUserId() != null){
            WmUser wmUser = wmUserMapper.selectById(wmNews.getUserId());
            if (wmUser != null && wmUser.getName() != null){
                author = apAuthorMapper.selectByName(wmUser.getName());
                if (author == null || author.getId() == null){
                    author = new ApAuthor();
                    author.setUserId(wmUser.getApUserId());
                    author.setName(wmUser.getName());
                    author.setCreatedTime(new Date());
                    author.setUserId(wmUser.getApUserId());
                    author.setType(2);
                    apAuthorMapper.insert(author);
                }
            }
        }
        ApArticle apArticle = new ApArticle();
        if (author != null){
            apArticle.setAuthorId(author.getId().longValue());
            apArticle.setAuthorName(author.getName());
        }
        apArticle.setCreatedTime(new Date());
        Integer channelId = wmNews.getChannelId();
        if (channelId != null){
            AdChannel channel = adChannelMapper.selectByPrimaryKey(channelId);
            apArticle.setChannelId(channel.getId());
            apArticle.setChannelName(channel.getName());
        }
        apArticle.setLayout(wmNews.getType());
        apArticle.setTitle(wmNews.getTitle());
        String images = wmNews.getImages();
        if (images != null){
            String[] split = images.split(",");
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < split.length; i++){
                if (i > 0){
                    builder.append(",");
                }
                builder.append(fileServerUrl);
                builder.append(split[i]);
            }
            apArticle.setImages(builder.toString());
        }
        apArticleMapper.insert(apArticle);

        //保存文章内容
        ApArticleContent articleContent = new ApArticleContent();
        articleContent.setArticleId(apArticle.getId());
        articleContent.setContent(ZipUtils.gzip(wmNews.getContent()));
        apArticleContentMapper.insert(articleContent);

        //保存文章配置信息
        ApArticleConfig apArticleConfig = new ApArticleConfig();
        apArticleConfig.setArticleId(apArticle.getId());
        apArticleConfig.setIsComment(true);
        apArticleConfig.setIsDelete(false);
        apArticleConfig.setIsDown(false);
        apArticleConfig.setIsForward(true);
        apArticleConfigMapper.insert(apArticleConfig);

        //创建es索引
        EsIndexEntity indexEntity = new EsIndexEntity();
        indexEntity.setId(apArticle.getId().longValue());
        indexEntity.setChannelId(channelId.longValue());
        indexEntity.setContent(wmNews.getContent());
        indexEntity.setPublishTime(new Date());
        indexEntity.setStatus(new Long(1));
        indexEntity.setTitle(wmNews.getTitle());
        if (wmNews.getUserId() != null){
            indexEntity.setUserId(wmNews.getUserId());
        }
        indexEntity.setTitle("media");
        Index.Builder builder = new Index.Builder(indexEntity);
        builder.id(apArticle.getId().toString());
        builder.refresh(true);
        Index build = builder.index(ESIndexConstants.ARTICLE_INDEX).type(ESIndexConstants.DEFAULT_DOC).build();
        JestResult jestResult = null;
        try {
            jestResult = jestClient.execute(build);
        }catch (Exception e){
            log.info("创建索引失败！");
            log.error("异常信息[{}]", e);
        }
        wmNews.setArticleId(apArticle.getId());
        updateNews(wmNews, (short) 9, "审核通过");

        //通知用户审核通过
        ApUserMessage userMessage = new ApUserMessage();
        userMessage.setUserId(wmNews.getUserId());
        userMessage.setCreatedTime(new Date());
        userMessage.setIsRead(false);
        userMessage.setContent("文章审核通过");
        userMessage.setType(108);
        apUserMessageMapper.insertSelective(userMessage);

    }

    /**
     * 找出文本和图片列表
     * @param images
     * @param builder
     * @param contentArray
     */
    private void handlerTextAndImages(List<String> images, StringBuilder builder, JSONArray contentArray) {
        for (Object array : contentArray) {
            JSONObject jsonObject = (JSONObject) array;
            String type = jsonObject.getString("type");
            String value = jsonObject.getString("value");
            if ("image".equals(type)){
                images.add(value);
            }else if ("text".equals(type)){
                builder.append(value);
            }
        }
    }

    /**
     * 修改文章
     * @param wmNews
     * @param status
     * @param message
     */
    private void updateNews(WmNews wmNews, short status, String message) {
        wmNews.setStatus(status);
        wmNews.setReason(message);
        wmNewsMapper.updateNewsByPrimaryKey(wmNews);
    }
}
