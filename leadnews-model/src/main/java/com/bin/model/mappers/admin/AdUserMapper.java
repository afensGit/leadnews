package com.bin.model.mappers.admin;

import com.bin.model.admin.pojos.AdUser;

/**
 * @author huangbin
 */
public interface AdUserMapper {
    /**
     * 根据用户名查询后台用户
     * @param name
     * @return
     */
    AdUser selectByName(String name);
}
