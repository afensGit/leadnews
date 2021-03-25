package com.bin.model.mappers.wemedia;

import com.bin.model.media.pojos.WmUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/25 22:49
 * 查询自媒体用户
 */

@Mapper
public interface WmUserMapper {

    /**
     * 根据自媒体用户名查询自媒体用户，实现登录验证
     * @param name
     * @return
     */
    WmUser selectByName(String name);

    WmUser selectById(long id);
}
