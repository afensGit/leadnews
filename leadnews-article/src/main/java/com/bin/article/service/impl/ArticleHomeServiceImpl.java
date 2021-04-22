package com.bin.article.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.bin.article.service.ArticleHomeService;
import com.bin.common.article.contants.ArticleContants;
import com.bin.model.article.dtos.ArticleHomeDto;
import com.bin.model.article.pojos.ApArticle;
import com.bin.model.article.pojos.ApHotArticles;
import com.bin.model.behavior.pojos.ApBehaviorEntry;
import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.mappers.app.ApArticleMapper;
import com.bin.model.mappers.app.ApBehaviorEntryMapper;
import com.bin.model.mappers.app.ApHotArticlesMapper;
import com.bin.model.mappers.app.ApUserArticleListMapper;
import com.bin.model.mess.app.ArticleVisitStreamDto;
import com.bin.model.user.pojos.ApUser;
import com.bin.model.user.pojos.ApUserArticleList;
import com.bin.utils.threadlocal.AppThreadLocalUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/5 11:01
 */
@Service
@Slf4j
public class ArticleHomeServiceImpl implements ArticleHomeService {

    private static final Integer DEFAULT_SIZE = 20;

    private static final Integer MAX_SIZE = 50;

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Autowired
    private ApUserArticleListMapper apUserArticleListMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ApBehaviorEntryMapper apBehaviorEntryMapper;
    @Autowired
    private ApHotArticlesMapper apHotArticlesMapper;

    /**
     * 文章信息加载业务层
     * @param dto
     * @param type
     * @return
     */
    @Override
    public ResponseResult load(ArticleHomeDto dto, Short type) {
        //数据校验
        if (dto == null){
            dto = new ArticleHomeDto();
        }
        if (dto.getMaxBehotTime() == null){
            dto.setMaxBehotTime(new Date());
        }
        if (dto.getMinBehotTime() == null){
            dto.setMinBehotTime(new Date());
        }
        if (!type.equals(ArticleContants.LOAD_MORE) && !type.equals(ArticleContants.LOAD_NEW)){
            type = ArticleContants.LOAD_MORE;
        }
        Integer size = dto.getSize();
        if (size == null || size <= 0){
            size = DEFAULT_SIZE;
        }
        //相当于 size > MAX_SIZE ? MAX_SIZE : size
        dto.setSize(Math.min(size, MAX_SIZE));

        //频道验证
        if (StringUtils.isEmpty(dto.getTag())){
            dto.setTag(ArticleContants.DEFAULT_TAG);
        }

        //数据加载
        //判断用户是否登录
        ApUser user = AppThreadLocalUtils.getUser();
        if (user == null){
            //用户未登录，加载默认文章
            return ResponseResult.okResult(getDefaultArticle(dto, type));
        }
        //用户已登录，加载推荐文章
        return ResponseResult.okResult(getUserArticle(user, dto, type));
    }
    @Override
    public ResponseResult updateArticleView(ArticleVisitStreamDto dto) {
        int rows = apArticleMapper.updateArticleView(dto.getArticleId(),
                dto.getView(),dto.getCollect(),dto.getCommont(),dto.getLike());
        //LOGGER.info("更新文章阅读数#articleId：{},dto：{}", dto.getArticleId(), JSON.toJSONString(dto),rows);
        return ResponseResult.okResult(rows);
    }

    /**
     * 加载用户推荐文章，若没有推荐文章，则加载默认文章
     * @param dto
     * @param type
     * @return
     */
    private List<ApArticle> getUserArticle(ApUser apUser, ArticleHomeDto dto, Short type){
        //查询app用户文章推荐列表，只能查询出文章id，还需要根据文章id查询文章
        List<ApUserArticleList> articleIdListByUser = apUserArticleListMapper.loadArticleIdListByUser(apUser, dto, type);
        if (articleIdListByUser.isEmpty()){
            //如果用户推荐文章列表为空，加载默认列表
            return getDefaultArticle(dto, type);
        }
        //用户推荐文章列表不为空
        return apArticleMapper.loadArticleListByIdList(articleIdListByUser);
    }


    /**
     * 加载默认文章
     * @param dto
     * @param type
     * @return
     */
    private List<ApArticle> getDefaultArticle(ArticleHomeDto dto, Short type){
        return apArticleMapper.loadArticleListByLocation(dto, type);
    }

    @Override
    public ResponseResult loadV2(Short type, ArticleHomeDto dto, boolean firstPage) {
        if(null == dto){
            dto = new ArticleHomeDto();
        }
        ApUser user = AppThreadLocalUtils.getUser();
        Integer size = dto.getSize();
        String tag = dto.getTag();
        // 分页参数校验
        if (size == null || size <= 0) {
            size = DEFAULT_SIZE;
        }
        size = Math.min(size,MAX_SIZE);
        dto.setSize(size);
        //  类型参数校验
        if (!type.equals(ArticleContants.LOAD_MORE) && !type.equals(ArticleContants.LOAD_NEW)) {
            type = ArticleContants.LOAD_MORE;
        }
        // 文章频道参数验证
        if (StringUtils.isEmpty(tag)) {
            dto.setTag(ArticleContants.DEFAULT_TAG);
        }
        // 最大时间处理
        if(dto.getMaxBehotTime()==null){
            dto.setMaxBehotTime(new Date());
        }
        // 最小时间处理
        if(dto.getMinBehotTime()==null){
            dto.setMinBehotTime(new Date());
        }
        //从缓存中读取 否则数据库查询
        if(firstPage){
            // 数据加载
            List<ApArticle> cacheList = getCacheArticleV2(dto);
            if(cacheList.size()>0){
                log.info("使用缓存加载数据#tag:{}", dto.getTag());
                return ResponseResult.okResult(cacheList);
            }
        }
        // 数据加载
        if(user!=null){
            return ResponseResult.okResult(getUserArticleV2(user,dto,type));
        }else{
            return ResponseResult.okResult(getDefaultArticleV2(dto,type));
        }
    }
    /**
     * 查询缓存首页文章数据
     * @param dto
     * @return
     */
    private List<ApArticle> getCacheArticleV2(ArticleHomeDto dto) {
        log.info("查询缓存热文章数据#tag:{}", dto.getTag());
        String key = ArticleContants.HOT_ARTICLE_FIRST_PAGE + dto.getTag();
        String ret = redisTemplate.opsForValue().get(key);
        if(StringUtils.isEmpty(ret)){
            return new ArrayList<>();
        }
        List<ApArticle> list = JSONArray.parseArray(ret, ApArticle.class);
        log.info("查询缓存热文章数据#tag:{}, size:{}", dto.getTag(), list.size());
        return list;
    }
    /**
     * 先从用户的推荐表中查找文章，如果没有再从大文章列表中获取
     * @param user
     * @param dto
     * @param type
     * @return
     */
    private List<ApArticle> getUserArticleV2(ApUser user, ArticleHomeDto dto, Short type){
        // 用户和设备不能同时为空
        if(user == null){
            return Lists.newArrayList();
        }
        Long userId = user.getId();
        ApBehaviorEntry apBehaviorEntry = apBehaviorEntryMapper.selectByUserIdOrEquipment(userId, null);
        // 行为实体找以及注册了，逻辑上这里是必定有值，除非参数错误
        if(apBehaviorEntry==null){
            return Lists.newArrayList();
        }
        Integer entryId =  apBehaviorEntry.getId();
        //如果没查到 查询全局热文章
        if(entryId==null) {
            entryId=0;
        }
        long time = System.currentTimeMillis();
        List<ApHotArticles> list = apHotArticlesMapper.loadArticleIdListByEntryId(entryId, dto, type);
        System.out.println("==================1=:"+(System.currentTimeMillis()-time));
        //默认从热文章里查询
        if(!list.isEmpty()){
            List<Integer> articleList = list.stream().map(p -> p.getArticleId()).collect(Collectors.toList());
            List<ApArticle> temp = apArticleMapper.loadArticleListByIdListV2(articleList);
            System.out.println("==================2=:"+(System.currentTimeMillis()-time));
            return temp;
        }else{
            return getDefaultArticleV2(dto,type);
        }
    }
    /**
     * 从默认的热数据列表中获取文章
     * @param dto
     * @param type
     * @return
     */
    private List<ApArticle> getDefaultArticleV2(ArticleHomeDto dto,Short type){
        List<ApHotArticles> hotList = apHotArticlesMapper.loadHotListByLocation(dto, type);
        List<ApArticle> articleList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(hotList)){
            for (ApHotArticles hot: hotList) {
                ApArticle article = apArticleMapper.selectById(Long.valueOf(hot.getArticleId()));
                articleList.add(article);
            }
        }
        return articleList;
    }


}
