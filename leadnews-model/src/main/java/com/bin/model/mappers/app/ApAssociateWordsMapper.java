package com.bin.model.mappers.app;

import com.bin.model.article.pojos.ApAssociateWords;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/25 19:35
 * 联想词查询
 */

@Mapper
public interface ApAssociateWordsMapper {

    List<ApAssociateWords> selectByAssociateWords(@Param("searchWord")String searchWord, @Param("limit")Integer limit);
}
