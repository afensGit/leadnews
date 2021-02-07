package com.bin.article.service.impl;

import com.bin.article.service.ArticleHomeService;
import com.bin.common.article.contants.ArticleContants;
import com.bin.model.article.dtos.ArticleHomeDto;
import com.bin.model.article.pojos.ApArticle;
import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.mappers.app.ApArticleMapper;
import com.bin.model.mappers.app.ApUserArticleListMapper;
import com.bin.model.user.pojos.ApUser;
import com.bin.model.user.pojos.ApUserArticleList;
import com.bin.utils.threadlocal.AppThreadLocalUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/5 11:01
 */
@Service
public class ArticleHomeServiceImpl implements ArticleHomeService {

    private static final Integer DEFAULT_SIZE = 20;

    private static final Integer MAX_SIZE = 50;

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Autowired
    private ApUserArticleListMapper apUserArticleListMapper;

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

}
