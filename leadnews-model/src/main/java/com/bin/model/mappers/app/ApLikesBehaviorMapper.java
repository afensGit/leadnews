package com.bin.model.mappers.app;

import com.bin.model.behavior.pojos.ApLikesBehavior;
import org.apache.ibatis.annotations.Param;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/23 18:35
 */
public interface ApLikesBehaviorMapper {

    ApLikesBehavior selectLastLike(@Param("burst") String burst, @Param("entryId")Integer entryId,
                                   @Param("articleId")Integer articleId, @Param("type")Short type);

    int insert(ApLikesBehavior record);
}
