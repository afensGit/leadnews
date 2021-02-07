package com.bin.apis.article.controllerApi;

import com.bin.model.behavior.dtos.ShowBehaviorDto;
import com.bin.model.common.dtos.ResponseResult;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/6 21:25
 */
public interface BehaviorControllerApi {

    ResponseResult saveShowBehavior(ShowBehaviorDto dto);
}
