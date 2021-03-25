package com.bin.admin.service;

import com.bin.model.admin.pojos.AdUser;
import com.bin.model.common.dtos.ResponseResult;

/**
 * @author huangbin
 */
public interface UserLoginService {
    /**
     * 登录业务逻辑
     * @param user
     * @return
     */
    ResponseResult login(AdUser user);
}
