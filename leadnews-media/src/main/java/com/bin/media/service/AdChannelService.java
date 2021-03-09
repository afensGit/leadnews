package com.bin.media.service;

import com.bin.model.common.dtos.ResponseResult;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/3/8 17:31
 */
public interface AdChannelService {
    /**
     * 查询所有频道
     * @return
     */
    ResponseResult selectAll();
}
