package com.bin.model.mappers.crawlers;

import com.bin.model.crawler.pojos.ClNewsAdditional;

import java.util.Date;
import java.util.List;

/**
 * @author huangbin
 */
public interface ClNewsAdditionalMapper {
    /**
     * 删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 添加
     * @param record
     * @return
     */
    int insert(ClNewsAdditionalMapper record);

    /**
     * 根据条件添加
     * @param record
     * @return
     */
    int insertSelective(ClNewsAdditional record);

    /**
     * 查询
     * @param id
     * @return
     */
    ClNewsAdditionalMapper selectByPrimaryKey(Integer id);

    /**
     * 根据条件更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(ClNewsAdditional record);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(ClNewsAdditionalMapper record);


    /**
     * 按条件查询所有数据
     *
     * @param record
     * @return
     */
    List<ClNewsAdditional> selectList(ClNewsAdditional record);

    /**
     * 获取需要更新的数据
     * @return
     */
    List<ClNewsAdditional> selectListByNeedUpdate(Date currentDate);

}
