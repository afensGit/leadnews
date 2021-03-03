package com.bin.model.mappers.app;

import com.bin.model.article.dtos.UserSearchDto;
import com.bin.model.article.pojos.ApHotWords;
import com.bin.model.user.pojos.ApUserSearch;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/25 16:40
 */

@Mapper
public interface ApUserSearchMapper {

    List<ApUserSearch> selectByEntryId(@Param("entryId")Integer entryId, @Param("limit")Integer limit);

    int deleteUserSearch(@Param("entryId")Integer entryId, @Param("historyLists")List<Integer> historyLists);

    int clearUserSearch(Integer entryId);

    int insertUserSearch(ApUserSearch record);

    int checkSearchRecord(@Param("entryId") Integer entryId, @Param("keyword") String keyword);

}
