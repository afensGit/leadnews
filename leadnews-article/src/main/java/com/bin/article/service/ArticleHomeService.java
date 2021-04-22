package com.bin.article.service;

import com.bin.model.article.dtos.ArticleHomeDto;
import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.mess.app.ArticleVisitStreamDto;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/5 10:59
 */
public interface ArticleHomeService {
    /**
     * 加载文章
     * type 1 加载更多
     * type 2 加载更多
     * @param dto
     * @param type
     * @return
     */
    public ResponseResult load(ArticleHomeDto dto, Short type);

    /**
     * 更新阅读数
     * @param dto
     * @return
     */
    ResponseResult updateArticleView(ArticleVisitStreamDto dto);

    /**
     * 加载文章列表数据
     * @param type 1 加载更多  2 加载更新
     * @param dto 封装数据
     * @return 数据列表
     */
    ResponseResult loadV2(Short type, ArticleHomeDto dto, boolean firstPage);
}
