package com.bin.admin.service.impl;

import com.bin.admin.service.UserLoginService;
import com.bin.model.admin.pojos.AdUser;
import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.common.enums.AppHttpCodeEnum;
import com.bin.model.mappers.admin.AdUserMapper;
import com.bin.utils.jwt.AppJwtUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Hashtable;
import java.util.Map;

/**
 * @author huangbin
 */

@Service
public class UserLoginServiceImpl implements UserLoginService {

    @Autowired
    private AdUserMapper adUserMapper;

    @Override
    public ResponseResult login(AdUser user) {
        if (StringUtils.isEmpty(user.getName()) && StringUtils.isEmpty(user.getPassword())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE, "用户名或密码不能为空！");
        }
        AdUser adUser = adUserMapper.selectByName(user.getName());
        if (adUser == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "用户不存在");
        }else {
            if (!user.getPassword().equals(adUser.getPassword())){
                return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
            }else {
                Map<String, Object> map = new Hashtable<>();
                adUser.setPassword("");
                adUser.setSalt("");
                map.put("token", AppJwtUtil.getToken(adUser));
                map.put("user", adUser);
                return ResponseResult.okResult(map);
            }
        }
    }
}
