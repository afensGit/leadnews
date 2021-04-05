package com.bin.model.mappers.app;

import com.bin.model.article.pojos.ApArticleConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/22 22:56
 */
@Mapper
public interface ApArticleConfigMapper {
    /**
     * 根据文章id查询文章信息
     * @param articleId
     * @return
     */
    ApArticleConfig selectConfigByArticleId(int articleId);

    /**
     * 添加文章信息
     * @param articleConfig
     * @return
     */
    Integer insert(ApArticleConfig articleConfig);
}
