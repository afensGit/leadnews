package com.bin.apis.user.controllerApi;

import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.user.dtos.UserRelationDto;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/24 10:35
 */
public interface UserRelationControllerApi {

    ResponseResult follow(UserRelationDto dto);
}
