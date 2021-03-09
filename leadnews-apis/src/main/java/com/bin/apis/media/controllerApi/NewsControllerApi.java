package com.bin.apis.media.controllerApi;

import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.media.dtos.WmNewsDto;
import com.bin.model.media.dtos.WmNewsPageReqDto;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/3/8 17:37
 */
public interface NewsControllerApi {

    /**
     * 提交文章
     * @param dto
     * @return
     */
    ResponseResult submitNews(WmNewsDto dto);

    /**
     * 保存草稿
     * @param dto
     * @return
     */
    ResponseResult saveDraftNews(WmNewsDto dto);

    /**
     * 获取文章列表
     * @param dto
     * @return
     */
    ResponseResult getNewsList(WmNewsPageReqDto dto);

    /**
     * 获取文章详情
     * @param dto
     * @return
     */
    ResponseResult getNewsById(WmNewsDto dto);

    /**
     * 删除文章
     * @param dto
     * @return
     */
    ResponseResult deleteNewsById(WmNewsDto dto);
}
