package com.bin.model.mappers.app;

import com.bin.model.article.pojos.ApAuthor;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/23 18:41
 */
public interface ApAuthorMapper {
    /**
     * 根据id查询作者
     * @param id
     * @return
     */
    ApAuthor selectById(Integer id);

    /**
     * 根据名称查询作者
     * @param name
     * @return
     */
    ApAuthor selectByName(String name);

    /**
     * 添加作者
     * @param author
     * @return
     */
    Integer insert(ApAuthor author);
}
