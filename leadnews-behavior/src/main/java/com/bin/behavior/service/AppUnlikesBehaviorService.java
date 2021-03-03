package com.bin.behavior.service;

import com.bin.model.behavior.dtos.UnLikesBehaviorDto;
import com.bin.model.common.dtos.ResponseResult;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/24 13:17
 */
public interface AppUnlikesBehaviorService {

    ResponseResult saveUnlikesBehavior(UnLikesBehaviorDto dto);
}
