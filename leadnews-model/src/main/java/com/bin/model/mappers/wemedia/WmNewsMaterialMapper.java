package com.bin.model.mappers.wemedia;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/26 15:11
 */
public interface WmNewsMaterialMapper {

    int countByMid(Integer mid);

    int deleteByNewId(int id);

    void saveRelationsByContent(@Param("materials") Map<String, Object> materials, @Param("newsId") Integer newsId,
                                @Param("type") Short type);
}
