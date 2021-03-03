package com.bin.media.service;

import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.media.pojos.WmUser;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/25 22:56
 */
public interface WmUserLoginService {
    /**
     * 自媒体用户登录
     * @param wmUser
     * @return
     */
    ResponseResult login(WmUser wmUser);
}
