package com.bin.model.mappers.app;

import com.bin.model.article.pojos.ApHotWords;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/25 19:22
 * 热词查询
 */

@Mapper
public interface ApHotWordsMapper {

    List<ApHotWords> queryHotWords(String date);
}
