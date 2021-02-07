package com.bin.model.mappers.app;

import com.bin.model.behavior.pojos.ApShowBehavior;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/6 21:47
 */
@Mapper
public interface AppShowBehaviorMapper {

    List<ApShowBehavior> selectListByEntryIdAndArticleIds(@Param("entryId") Integer entryId, @Param("articleIds") List<Integer> articleIds);

    void saveBehaviors(@Param("entryId") Integer entryId, @Param("articleIds") List<Integer> articleIds);
}
