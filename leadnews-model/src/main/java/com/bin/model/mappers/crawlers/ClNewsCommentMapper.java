package com.bin.model.mappers.crawlers;

import com.bin.model.crawler.pojos.ClNewsComment;

import java.util.List;

/**
 * @author huangbin
 */
public interface ClNewsCommentMapper {
    /**
     * 删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入
     * @param record
     * @return
     */
    int insert(ClNewsComment record);

    /**
     * 判断插入
     * @param record
     * @return
     */
    int insertSelective(ClNewsComment record);

    /**
     * 查询
     * @param id
     * @return
     */
    ClNewsComment selectByPrimaryKey(Integer id);

    /**
     * 判断更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(ClNewsComment record);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(ClNewsComment record);


    /**
     * 按条件查询所有数据
     *
     * @param record
     * @return
     */
    List<ClNewsComment> selectList(ClNewsComment record);
}
