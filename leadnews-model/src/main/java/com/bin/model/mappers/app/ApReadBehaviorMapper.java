package com.bin.model.mappers.app;

import com.bin.model.behavior.pojos.ApReadBehavior;
import org.apache.ibatis.annotations.Param;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/24 16:48
 */
public interface ApReadBehaviorMapper {

    int insert(ApReadBehavior record);

    int update(ApReadBehavior record);

    ApReadBehavior selectByEntryId(@Param("burst")String burst, @Param("entryId")Integer entryId, @Param("articleId")Integer articleId);
}
