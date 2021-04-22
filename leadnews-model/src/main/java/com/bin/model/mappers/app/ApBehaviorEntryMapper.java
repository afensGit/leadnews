package com.bin.model.mappers.app;

import com.bin.model.behavior.pojos.ApBehaviorEntry;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/6 23:57
 */

@Mapper
public interface ApBehaviorEntryMapper {

    ApBehaviorEntry selectByUserIdOrEquipment(@Param("userId") Long userId, @Param("equipmentId") Integer equipmentId);

    List<ApBehaviorEntry> selectAllEntry();
}
