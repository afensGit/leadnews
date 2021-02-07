package com.bin.article.service;

import com.bin.model.article.dtos.ArticleHomeDto;
import com.bin.model.common.dtos.ResponseResult;

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
}
