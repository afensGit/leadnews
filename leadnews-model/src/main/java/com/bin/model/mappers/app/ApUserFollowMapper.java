package com.bin.model.mappers.app;

import com.bin.model.user.pojos.ApUserFollow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/23 17:46
 * app用户关注信息表
 */

@Mapper
public interface ApUserFollowMapper {

    ApUserFollow selectByFollowId(@Param("burst") String burst, @Param("userId")Long userId,
                                  @Param("followId")Integer followId);

    int insert(ApUserFollow record);

    int deleteByFollowId(@Param("burst") String burst,@Param("userId")Long userId, @Param("followId")Integer followId);
}
