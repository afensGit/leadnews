package com.bin.apis.article.controllerApi;

import com.bin.model.article.dtos.ArticleHomeDto;
import com.bin.model.article.pojos.ApArticle;
import com.bin.model.common.dtos.ResponseResult;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/5 10:54
 */
public interface ArticleHomeControllerApi {
    /**
     * 加载首页文章
     * @param dto
     * @return
     */
    public ResponseResult load(ArticleHomeDto dto);

    /**
     * 加载更多文章
     * @param dto
     * @param type
     * @return
     */
    public ResponseResult loadMore( ArticleHomeDto dto, Short type);

    /**
     * 加载最新文章
     * @param dto
     * @param type
     * @return
     */
    public ResponseResult loadNews(ArticleHomeDto dto, Short type);
}
