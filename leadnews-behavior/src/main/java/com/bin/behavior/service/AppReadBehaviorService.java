package com.bin.behavior.service;

import com.bin.model.behavior.dtos.ReadBehaviorDto;
import com.bin.model.common.dtos.ResponseResult;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/24 17:00
 */
public interface AppReadBehaviorService {
    /**
     * 保存用户阅读行为
     * @param dto
     * @return
     */
    ResponseResult saveReadBehavior(ReadBehaviorDto dto);
}
