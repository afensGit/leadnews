package com.bin.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.bin.article.service.AppHotArticleService;
import com.bin.common.article.contants.ArticleContants;
import com.bin.common.kafka.KafkaSender;
import com.bin.model.admin.pojos.AdChannel;
import com.bin.model.article.pojos.ApArticle;
import com.bin.model.article.pojos.ApHotArticles;
import com.bin.model.behavior.pojos.ApBehaviorEntry;
import com.bin.model.mappers.admin.AdChannelMapper;
import com.bin.model.mappers.app.ApArticleMapper;
import com.bin.model.mappers.app.ApBehaviorEntryMapper;
import com.bin.model.mappers.app.ApHotArticlesMapper;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huangbin
 */
@Service
public class AppHotArticleServiceImpl implements AppHotArticleService {
    @Autowired
    private ApHotArticlesMapper apHotArticlesMapper;

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Autowired
    private ApBehaviorEntryMapper apBehaviorEntryMapper;

    @Autowired
    private AdChannelMapper adChannelMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private KafkaSender kafkaSender;

    @Override
    public void computeHotArticle() {
        //计算逻辑
        //获取前一天的时间
        String lastDay = DateTime.now().minusDays(1).toString("yyyy-MM-dd 00:00:00");
        //查询前一天的新闻
        List<ApArticle> articleList = apArticleMapper.loadLastArticleForHot(lastDay);
        List<ApHotArticles> hotArticlesList = computeHotArticle(articleList);

        //缓存频道到redis
        cacheTagToRedis(articleList);

        //所有用户
        List<ApBehaviorEntry> entryList = apBehaviorEntryMapper.selectAllEntry();
        for(ApHotArticles hot : hotArticlesList){
            //插入热文章数据
            apHotArticlesMapper.insert(hot);
            //为每位用户保存一份
            saveHotArticleForEntryList(hot, entryList);
            //热文章计算完成发送给图片队列
            //通知处理热文章图片
            kafkaSender.sendHotArticleMessage(hot);
        }
    }

    /**
     * 计算热文章
     * @param articleList
     * @return
     */
    private List<ApHotArticles> computeHotArticle(List<ApArticle> articleList) {
        List<ApHotArticles> hotArticlesList = new ArrayList<>();
        ApHotArticles hot = null;
        for (ApArticle a : articleList) {
            hot = initHotBaseApArticle(a);
            Integer score = computeScore(a);
            hot.setScore(score);
            hotArticlesList.add(hot);
        }
        hotArticlesList.sort(new Comparator<ApHotArticles>() {
            @Override
            public int compare(ApHotArticles o1, ApHotArticles o2) {
                return o1.getScore() < o2.getScore() ? 1 : -1;
            }
        });
        if(hotArticlesList.size()>1000){
            return hotArticlesList.subList(0,1000);
        }
        return hotArticlesList;
    }

    /**
     * 初始化热文章属性
     * @param article
     * @return
     */
    private ApHotArticles initHotBaseApArticle(ApArticle article){
        ApHotArticles hot = new ApHotArticles();
        hot.setEntryId(0);
        //根据articleID查询
        hot.setTagId(article.getChannelId());
        hot.setTagName(article.getChannelName());
        hot.setScore(0);
        hot.setArticleId(article.getId());
        //设置省市区
        hot.setProvinceId(article.getProvinceId());
        hot.setCityId(article.getCityId());
        hot.setCountyId(article.getCountyId());
        hot.setIsRead(0);
        //日期
        hot.setReleaseDate(article.getPublishTime());
        hot.setCreatedTime(new Date());
        return hot;
    }

    /**
     * 计算热度分规则 1.0
     * @param a
     * @return
     */
    private Integer computeScore(ApArticle a) {
        Integer score = 0;
        if(a.getLikes()!=null){
            score += a.getLikes();
        }
        if(a.getCollection()!=null){
            score += a.getCollection();
        }
        if(a.getComment()!=null){
            score += a.getComment();
        }
        if(a.getViews()!=null){
            score += a.getViews();
        }
        return score;
    }

    /**
     * 为每位用户保存一份
     * @param hot
     * @param entryList
     */
    private void saveHotArticleForEntryList(ApHotArticles hot, List<ApBehaviorEntry> entryList) {
        for (ApBehaviorEntry entry: entryList){
            hot.setEntryId(entry.getId());
            apHotArticlesMapper.insert(hot);
        }
    }

    /**
     * 缓存频道首页到redis
     * @param articlesList
     */
    private void cacheTagToRedis(List<ApArticle> articlesList) {
        List<AdChannel> channels = adChannelMapper.selectAll();
        List<ApArticle> temp = null;
        for (AdChannel channel : channels){
            temp = articlesList.stream().
                    filter(p -> p.getChannelId().equals(channel.getId()))
                    .collect(Collectors.toList());
            if(temp.size()>30){
                temp = temp.subList(0,30);
            }
            if(temp.size()==0){
                redisTemplate.opsForValue().set(ArticleContants.HOT_ARTICLE_FIRST_PAGE+channel.getId(), "");
                continue;
            }
            redisTemplate.opsForValue().set(ArticleContants.HOT_ARTICLE_FIRST_PAGE+channel.getId(), JSON.toJSONString(temp));
        }
    }
}
