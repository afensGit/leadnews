package com.bin.model.mappers.wemedia;

import com.bin.model.media.dtos.StatisticDto;
import com.bin.model.media.pojos.WmNewsStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author huangbin
 */
public interface WmNewsStatisticsMapper {
    /**
     *
     * @param burst
     * @param userId
     * @param dto
     * @return
     */
    List<WmNewsStatistics> findByTimeAndUserId(@Param("burst") String burst,@Param("userId") long userId,@Param("dto") StatisticDto dto);

}
