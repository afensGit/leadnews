package com.bin.login.service.impl;

import com.bin.login.service.ApUserLoginService;
import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.common.enums.AppHttpCodeEnum;
import com.bin.model.mappers.app.ApUserMapper;
import com.bin.model.user.pojos.ApUser;
import com.bin.utils.jwt.AppJwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/25 11:31
 */

@Service
public class ApUserLoginServiceImpl implements ApUserLoginService {

    @Autowired
    private ApUserMapper apUserMapper;

    @Override
    public ResponseResult loginAuth(ApUser user) {
        if (StringUtils.isEmpty(user.getPhone()) || StringUtils.isEmpty(user.getPassword())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        ApUser apUser = apUserMapper.selectByApPhone(user.getPhone());
        if (apUser == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.AP_USER_DATA_NOT_EXIST);
        }
        if (!apUser.getPassword().equals(user.getPassword())){
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        apUser.setPassword("");
        Map<String, Object> data = new HashMap<>();
        data.put("token", AppJwtUtil.getToken(apUser));
        data.put("user", apUser);

        return ResponseResult.okResult(data);
    }
}
