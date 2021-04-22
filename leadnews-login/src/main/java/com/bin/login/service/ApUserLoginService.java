package com.bin.login.service;

import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.user.pojos.ApUser;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/25 11:28
 * 登录业务层接口
 */

public interface ApUserLoginService {
    /**
     * 登录
     * @param user
     * @return
     */
    ResponseResult loginAuth(ApUser user);

    /**
     * 用户登录验证V2
     * @param user
     * @return
     */
    ResponseResult loginAuthV2(ApUser user);
}
