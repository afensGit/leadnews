package com.bin.apis.media.controllerApi;

import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.media.pojos.WmUser;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/25 23:09
 */
public interface LoginControllerApi {

    ResponseResult login(WmUser wmUser);
}
