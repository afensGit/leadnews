package com.bin.model.mappers.app;

import com.bin.model.user.pojos.ApUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/23 23:01
 */

@Mapper
public interface ApUserMapper {

    /**
     * 根据用户id查询用户信息
     */

    ApUser selectById(Integer id);

    /**
     * 根据手机号查询用户
     * @param phone
     * @return
     */
    ApUser selectByApPhone(String phone);
}
