package com.bin.apis.login.controllerApi;

import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.user.pojos.ApUser;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/25 11:42
 */
public interface LoginControllerApi {

    public ResponseResult login(ApUser user);
}
