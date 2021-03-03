package com.bin.media.service.impl;

import com.bin.media.service.WmUserLoginService;
import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.common.enums.AppHttpCodeEnum;
import com.bin.model.mappers.wemedia.WmUserMapper;
import com.bin.model.media.pojos.WmUser;
import com.bin.utils.jwt.AppJwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/25 22:58
 */
@Service
public class WmUserLoginServiceImpl implements WmUserLoginService {

    @Autowired
    private WmUserMapper wmUserMapper;

    @Override
    public ResponseResult login(WmUser wmUser) {
        if (StringUtils.isEmpty(wmUser.getName()) || StringUtils.isEmpty(wmUser.getPassword())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE, "用户名或密码不能为空");
        }
        WmUser user = wmUserMapper.selectByName(wmUser.getName());
        if (user != null){
            if (user.getPassword().equals(wmUser.getPassword())){
                Map<String, Object> data = new HashMap<>();
                user.setPassword("");
                user.setSalt("");
                data.put("token", AppJwtUtil.getToken(user));
                data.put("user", user);
                return ResponseResult.okResult(data);
            }else {
                return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
            }
        }else {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "用户不存在");
        }

    }
}
