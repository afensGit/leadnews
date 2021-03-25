package com.bin.admin.controller.v1;

import com.bin.admin.service.UserLoginService;
import com.bin.apis.admin.controllerApi.LoginControllerApi;
import com.bin.model.admin.pojos.AdUser;
import com.bin.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangbin
 */

@RestController
@RequestMapping("/login")
public class LoginController implements LoginControllerApi {

    @Autowired
    private UserLoginService userLoginService;

    @Override
    @PostMapping("/in")
    public ResponseResult login(@RequestBody AdUser adUser) {
        return userLoginService.login(adUser);
    }
}
