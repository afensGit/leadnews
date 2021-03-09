package com.bin.model.mappers.admin;

import com.bin.model.admin.pojos.AdChannel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/3/8 17:24
 */

@Mapper
public interface AdChannelMapper {
    /**
     * 查询所有频道
     * @return
     */
    List<AdChannel> selectAll();

}
