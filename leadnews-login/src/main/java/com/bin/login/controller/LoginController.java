package com.bin.login.controller;

import com.bin.apis.login.controllerApi.LoginControllerApi;
import com.bin.login.service.ApUserLoginService;
import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.user.pojos.ApUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/25 11:43
 */
@RestController
@RequestMapping("/api/v1/login")
public class LoginController implements LoginControllerApi {

    @Autowired
    private ApUserLoginService apUserLoginService;

    @Override
    @PostMapping("/login_auth")
    public ResponseResult login(@RequestBody ApUser user) {
        return apUserLoginService.loginAuth(user);
    }
}
