package com.bin.media.service;

import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.media.dtos.StatisticDto;

/**
 * @author huangbin
 */
public interface StatisticsService {
    /**
     * 查询图文的统计数据
     * @param dto
     * @return
     */
    ResponseResult findNewsStatistics(StatisticDto dto);

    /**
     * 查询粉丝统计数据
     * @param dto
     * @return
     */
    ResponseResult findFansStatistics(StatisticDto dto);
}
