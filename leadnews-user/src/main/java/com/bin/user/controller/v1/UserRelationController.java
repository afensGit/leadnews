package com.bin.user.controller.v1;

import com.bin.apis.user.controllerApi.UserRelationControllerApi;
import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.user.dtos.UserRelationDto;
import com.bin.user.service.AppUserRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/24 10:36
 */

@RestController
@RequestMapping("/api/v1/user")
public class UserRelationController implements UserRelationControllerApi {

    @Autowired
    private AppUserRelationService appUserRelationService;

    @Override
    @PostMapping("/user_follow")
    public ResponseResult follow(@RequestBody UserRelationDto dto) {
        return appUserRelationService.follow(dto);
    }
}
