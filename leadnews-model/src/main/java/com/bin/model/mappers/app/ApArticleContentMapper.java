package com.bin.model.mappers.app;

import com.bin.model.article.pojos.ApArticleContent;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/22 22:47
 */
@Mapper
public interface ApArticleContentMapper {

    ApArticleContent selectArticleById(int articleId);
}
