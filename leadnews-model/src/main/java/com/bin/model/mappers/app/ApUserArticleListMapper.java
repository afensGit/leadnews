package com.bin.model.mappers.app;

import com.bin.model.article.dtos.ArticleHomeDto;
import com.bin.model.user.pojos.ApUser;
import com.bin.model.user.pojos.ApUserArticleList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/5 21:58
 */
@Mapper
public interface ApUserArticleListMapper {

    List<ApUserArticleList> loadArticleIdListByUser(@Param("user") ApUser user, @Param("dto") ArticleHomeDto dto, @Param("type") Short type);
}
