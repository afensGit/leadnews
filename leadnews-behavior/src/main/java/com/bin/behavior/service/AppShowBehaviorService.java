package com.bin.behavior.service;

import com.bin.model.behavior.dtos.ShowBehaviorDto;
import com.bin.model.common.dtos.ResponseResult;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/6 21:41
 */
public interface AppShowBehaviorService {
    /**
     * 保存用户行为
     * @param dto
     */
    ResponseResult saveShowBehavior(ShowBehaviorDto dto);
}
