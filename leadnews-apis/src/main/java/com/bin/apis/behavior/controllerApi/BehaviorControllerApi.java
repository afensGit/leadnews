package com.bin.apis.behavior.controllerApi;

import com.bin.model.behavior.dtos.LikesBehaviorDto;
import com.bin.model.behavior.dtos.ReadBehaviorDto;
import com.bin.model.behavior.dtos.ShowBehaviorDto;
import com.bin.model.behavior.dtos.UnLikesBehaviorDto;
import com.bin.model.behavior.pojos.ApLikesBehavior;
import com.bin.model.behavior.pojos.ApUnlikesBehavior;
import com.bin.model.common.dtos.ResponseResult;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/6 21:25
 */
public interface BehaviorControllerApi {

    ResponseResult saveShowBehavior(ShowBehaviorDto dto);

    ResponseResult saveLikesBehavior(LikesBehaviorDto dto);

    ResponseResult saveUnlikesBehavior(UnLikesBehaviorDto dto);

    ResponseResult saveReadBehavior(ReadBehaviorDto dto);
}
