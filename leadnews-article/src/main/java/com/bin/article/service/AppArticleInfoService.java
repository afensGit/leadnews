package com.bin.article.service;

import com.bin.model.article.dtos.ArticleInfoDto;
import com.bin.model.article.pojos.ApArticleSDto;
import com.bin.model.common.dtos.ResponseResult;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/22 23:01
 */
public interface AppArticleInfoService {

    ResponseResult loadArticleInfo(Integer articleId);

    ResponseResult loadArticleBehavior(ArticleInfoDto dto);
}
