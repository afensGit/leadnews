package com.bin.user.service;

import com.bin.model.behavior.dtos.FollowBehaviorDto;
import com.bin.model.common.dtos.ResponseResult;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/23 23:11
 */
public interface FollowBehaviorService {
    /**
     * 保存关注行为
     * @param dto
     * @return
     */
    ResponseResult saveFollowBehavior(FollowBehaviorDto dto);
}
