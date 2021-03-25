package com.bin.apis.article.controllerApi;

import com.bin.model.article.dtos.ArticleInfoDto;
import com.bin.model.common.dtos.ResponseResult;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/22 23:15
 */
public interface AppArticleInfoControllerApi {
    /**
     * 加载文章详细信息
     * @param dto
     * @return
     */
    ResponseResult loadArticleInfo(ArticleInfoDto dto);

    ResponseResult loadArticleBehavior(ArticleInfoDto dto);
}
