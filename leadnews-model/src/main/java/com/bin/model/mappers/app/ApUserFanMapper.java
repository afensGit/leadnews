package com.bin.model.mappers.app;

import com.bin.model.user.pojos.ApUserFan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/23 22:49
 */

@Mapper
public interface ApUserFanMapper {

    int insert(ApUserFan apUserFan);

    ApUserFan selectFansById(@Param("burst") String burst, @Param("userId")Integer userId , @Param("fansId")Long fansId);

    int deleteFansById(@Param("burst")String burst ,@Param("userId")Integer userId ,@Param("fansId")Long fansId);
}
