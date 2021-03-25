package com.bin.apis.media.controllerApi;

import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.media.dtos.StatisticDto;

/**
 * @author huangbin
 */
public interface StatisticsControllerApi {
    /**
     * 文章数据
     * @param dto
     * @return
     */
    ResponseResult newsData(StatisticDto dto);

    /**
     * 粉丝数据
     * @param dto
     * @return
     */
    ResponseResult fansData(StatisticDto dto);
}
