package com.bin.apis.media.controllerApi;

import com.bin.model.common.dtos.ResponseResult;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/3/8 17:36
 */
public interface AdChannelControllerApi {
    /**
     * 查询所有频道数据
     * @return
     */
    ResponseResult selectAll();
}
