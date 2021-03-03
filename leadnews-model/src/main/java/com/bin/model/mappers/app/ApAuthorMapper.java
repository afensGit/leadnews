package com.bin.model.mappers.app;

import com.bin.model.article.pojos.ApAuthor;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/23 18:41
 */
public interface ApAuthorMapper {

    ApAuthor selectById(Integer id);
}
