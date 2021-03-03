package com.bin.model.mappers.app;

import com.bin.model.behavior.pojos.ApUnlikesBehavior;
import org.apache.ibatis.annotations.Param;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/23 18:36
 */
public interface ApUnlikesBehaviorMapper {

    ApUnlikesBehavior selectLastUnLike(@Param("entryId") Integer entryId, @Param("articleId")Integer articleId);

    int insert(ApUnlikesBehavior record);
}
