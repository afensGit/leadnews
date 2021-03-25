package com.bin.media.controller.v1;

import com.bin.apis.media.controllerApi.StatisticsControllerApi;
import com.bin.media.service.StatisticsService;
import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.media.dtos.StatisticDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author huangbin
 */
@RestController
@RequestMapping("/api/v1/statistic")
public class StatisticsController implements StatisticsControllerApi {

    @Autowired
    private StatisticsService statisticsService;

    @Override
    @PostMapping("/news")
    public ResponseResult newsData(@RequestBody StatisticDto dto) {
        return statisticsService.findNewsStatistics(dto);
    }

    @Override
    @PostMapping("/fans")
    public ResponseResult fansData(@RequestBody StatisticDto dto) {
        return statisticsService.findFansStatistics(dto);
    }
}
