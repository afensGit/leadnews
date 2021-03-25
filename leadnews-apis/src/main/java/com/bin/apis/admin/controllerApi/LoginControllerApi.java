package com.bin.apis.admin.controllerApi;

import com.bin.model.admin.pojos.AdUser;
import com.bin.model.common.dtos.ResponseResult;

/**
 * @author huangbin
 */
public interface LoginControllerApi {
    /**
     * 登录接口
     * @param adUser
     * @return
     */
    ResponseResult login(AdUser adUser);
}
