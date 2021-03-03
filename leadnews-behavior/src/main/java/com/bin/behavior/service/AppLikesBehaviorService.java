package com.bin.behavior.service;

import com.bin.model.behavior.dtos.LikesBehaviorDto;
import com.bin.model.common.dtos.ResponseResult;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/24 12:54
 */
public interface AppLikesBehaviorService {

    ResponseResult saveLikesBehavior(LikesBehaviorDto dto);
}
