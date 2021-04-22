package com.bin.model.mappers.crawlers;

import com.bin.model.crawler.pojos.ClIpPool;

import java.util.List;

/**
 * @author huangbin
 */
public interface ClIpPoolMapper {
    /**
     * 根据主键删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 添加记录
     * @param record
     * @return
     */
    int insert(ClIpPool record);

    /**
     * 添加记录
     * @param record
     * @return
     */
    int insertSelective(ClIpPool record);

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    ClIpPool selectByPrimaryKey(Integer id);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(ClIpPool record);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(ClIpPool record);

    /**
     * 查询所有数据
     *
     * @param record
     * @return
     */
    List<ClIpPool> selectList(ClIpPool record);

    /**
     * 查询可用的列表
     *
     * @param record
     * @return
     */
    List<ClIpPool> selectAvailableList(ClIpPool record);
}
