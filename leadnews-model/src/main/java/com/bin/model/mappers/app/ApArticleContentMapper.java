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
    /**
     * 根据id查询文章内容
     * @param articleId
     * @return
     */
    ApArticleContent selectArticleById(int articleId);

    /**
     * 添加文章内容
     * @param apArticleContent
     * @return
     */
    Integer insert(ApArticleContent apArticleContent);
}
