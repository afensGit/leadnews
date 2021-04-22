package com.bin.images.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bin.common.kafka.messages.app.ApHotArticleMessage;
import com.bin.images.config.InitConfig;
import com.bin.images.service.CacheImageService;
import com.bin.images.service.HotArticleImageService;
import com.bin.model.article.pojos.ApArticle;
import com.bin.model.article.pojos.ApArticleContent;
import com.bin.model.article.pojos.ApHotArticles;
import com.bin.model.crawler.core.parse.ZipUtils;
import com.bin.model.mappers.app.ApArticleContentMapper;
import com.bin.model.mappers.app.ApArticleMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author huangbin
 */
@Service
@Slf4j
public class HotArticleImageServiceImpl implements HotArticleImageService {


    @Autowired
    private ApArticleContentMapper apArticleContentMapper;
    @Autowired
    private CacheImageService cacheImageService;
    @Autowired
    private ApArticleMapper apArticleMapper;

    @Override
    public void handleHotImage(ApHotArticleMessage message) {
        ApHotArticles hotArticles =  message.getData();
        log.info("处理热文章图片开始#articleId:{},message:{}", hotArticles.getArticleId(), JSON.toJSONString(message));
        ApArticleContent content = apArticleContentMapper.selectArticleById(hotArticles.getArticleId());
        //文章内容缓存
        String source = ZipUtils.gunzip(content.getContent());
        JSONArray array = JSONArray.parseArray(source);
        for (int i = 0; i< array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
            if(!"image".equals(obj.getString("type"))) {
                continue;
            }
            String imagePath = obj.getString("value");
            if(!imagePath.startsWith(InitConfig.PREFIX)){
                log.info("非站内图片不缓存#articleId:{}", hotArticles.getArticleId());
                continue;
            }
            //缓存图片
            cacheImageService.cache2Redis(imagePath, true);
        }
        //封面图片缓存
        ApArticle article = apArticleMapper.selectById(Long.valueOf(hotArticles.getArticleId()));
        if(StringUtils.isNotEmpty(article.getImages())){
            String[] articleImages = article.getImages().split(",");
            for (String img : articleImages){
                if(!img.startsWith(InitConfig.PREFIX)){
                    log.info("非站内图片不缓存#articleId:{}", hotArticles.getArticleId());
                    continue;
                }
                //缓存图片
                cacheImageService.cache2Redis(img, true);
            }
        }
        log.info("处理热文章图片结束#message:{}", JSON.toJSONString(message));
    }
}