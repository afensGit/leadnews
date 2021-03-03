package com.bin.user.service;

import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.user.dtos.UserRelationDto;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/23 23:29
 */
public interface AppUserRelationService {

    ResponseResult follow(UserRelationDto dto);
}
