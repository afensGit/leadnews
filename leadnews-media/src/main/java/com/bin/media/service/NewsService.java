package com.bin.media.service;

import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.media.dtos.WmNewsDto;
import com.bin.model.media.dtos.WmNewsPageReqDto;
import com.bin.model.media.pojos.WmNews;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/3/8 9:23
 */
public interface NewsService {
    /**
     * 保存文章
     * @param dto
     * @param type
     * @return
     */
    ResponseResult saveNews(WmNewsDto dto, Short type);

    /**
     * 查询文章列表
     * @param dto
     * @return
     */
    ResponseResult selectNewsList(WmNewsPageReqDto dto);

    /**
     * 查询文章详情
     * @param dto
     * @return
     */
    ResponseResult selectNewsById(WmNewsDto dto);

    /**
     * 删除文章
     * @param dto
     * @return
     */
    ResponseResult deleteNewsById(WmNewsDto dto);
}
