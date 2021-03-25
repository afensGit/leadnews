package com.bin.model.mappers.wemedia;

import com.bin.model.media.dtos.StatisticDto;
import com.bin.model.media.pojos.WmFansStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author huangbin
 */
public interface WmFansStatisticsMapper {
    /**
     * 查询粉丝数据
     * @param burst
     * @param userId
     * @param dto
     * @return
     */
    List<WmFansStatistics> findByTimeAndUserId(@Param("burst") String burst, @Param("userId") Long userId, @Param("dto") StatisticDto dto);

}
