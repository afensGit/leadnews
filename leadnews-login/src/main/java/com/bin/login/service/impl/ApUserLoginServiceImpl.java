package com.bin.login.service.impl;

import com.alibaba.fastjson.JSON;
import com.bin.common.kafka.KafkaSender;
import com.bin.login.service.ApUserLoginService;
import com.bin.login.service.ValidateService;
import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.common.enums.AppHttpCodeEnum;
import com.bin.model.mappers.app.ApUserMapper;
import com.bin.model.user.pojos.ApUser;
import com.bin.utils.jwt.AppJwtUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
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

    @Autowired
    private ValidateService validateService;

    //@Autowired
    //private StringRedisTemplate redisTemplate;

//    @Autowired
//    private KafkaSender kafkaSender;

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

    @Override
    public ResponseResult loginAuthV2(ApUser user) {
        //验证参数
        if(StringUtils.isEmpty(user.getPhone()) || StringUtils.isEmpty(user.getPassword())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //查询用户
        ApUser dbUser = apUserMapper.selectByApPhone(user.getPhone());
        if(dbUser==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.AP_USER_DATA_NOT_EXIST);
        }

        //选择不同的加密算法实现
        boolean isValid = validateService.validMD5(user, dbUser);
        //        boolean isValid = validateService.validMD5WithSalt(user, dbUser);
        //        boolean isValid = validateService.validDES(user, dbUser);

        if(!isValid){
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        //登录处理
        //设置redis
        //redisTemplate.opsForValue().set("ap-user-"+user.getId(), JSON.toJSONString(user), USER_EXPIRE);
        //登录成功发送消息
        //kafkaSender.sendUserLoginMessage(user);
        dbUser.setPassword("");
        Map<String,Object> map = Maps.newHashMap();
        map.put("token", AppJwtUtil.getToken(dbUser));
        map.put("user",dbUser);
        return ResponseResult.okResult(map);
    }
}
