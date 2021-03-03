package com.bin.model.mappers.app;

import com.bin.model.behavior.pojos.ApFollowBehavior;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/23 22:23
 * app关注行为
 */

@Mapper
public interface ApFollowBehaviorMapper {

    int insert(ApFollowBehavior record);
}
