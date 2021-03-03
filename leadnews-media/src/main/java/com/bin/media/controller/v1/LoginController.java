package com.bin.media.controller.v1;

import com.bin.apis.media.controllerApi.LoginControllerApi;
import com.bin.media.service.WmUserLoginService;
import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.media.pojos.WmUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/25 23:10
 */

@RestController
@RequestMapping("/login")
public class LoginController implements LoginControllerApi {

    @Autowired
    private WmUserLoginService wmUserLoginService;

    @Override
    @PostMapping("/in")
    public ResponseResult login(@RequestBody WmUser wmUser) {
        return wmUserLoginService.login(wmUser);
    }
}
