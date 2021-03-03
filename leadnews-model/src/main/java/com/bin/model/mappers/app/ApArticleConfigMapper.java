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

    ApArticleConfig selectConfigByArticleId(int articleId);
}
