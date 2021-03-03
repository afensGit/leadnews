package com.bin.model.mappers.app;

import com.bin.model.article.pojos.ApCollection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/23 17:41
 */

@Mapper
public interface ApCollectionMapper {

    ApCollection selectForEntryId(@Param("burst") String burst, @Param("entryId")Integer entryId,
                                  @Param("articleId") Integer articleId, @Param("type")Short type);
}
