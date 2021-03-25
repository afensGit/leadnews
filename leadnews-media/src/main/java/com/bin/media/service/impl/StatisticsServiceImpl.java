package com.bin.media.service.impl;


import com.bin.media.constans.WmMediaConstans;
import com.bin.media.service.StatisticsService;
import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.common.enums.AppHttpCodeEnum;
import com.bin.model.mappers.wemedia.WmFansStatisticsMapper;
import com.bin.model.mappers.wemedia.WmNewsStatisticsMapper;
import com.bin.model.mappers.wemedia.WmUserMapper;
import com.bin.model.media.dtos.StatisticDto;
import com.bin.model.media.pojos.WmFansStatistics;
import com.bin.model.media.pojos.WmNewsStatistics;
import com.bin.model.media.pojos.WmUser;
import com.bin.utils.common.BurstUtils;
import com.bin.utils.threadlocal.WmThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huangbin
 */

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private WmUserMapper wmUserMapper;

    @Autowired
    private WmNewsStatisticsMapper wmNewsStatisticsMapper;

    @Autowired
    private WmFansStatisticsMapper wmFansStatisticsMapper;

    @Override
    public ResponseResult findNewsStatistics(StatisticDto dto) {
        ResponseResult result = check(dto);
        if (result != null){
            return result;
        }
        WmUser wmUser = queryWmUser();
        String burst = BurstUtils.groudOne(wmUser.getApUserId());
        List<WmNewsStatistics> wmNewsStatistics = wmNewsStatisticsMapper.findByTimeAndUserId(burst, wmUser.getApUserId(), dto);
        result = ResponseResult.okResult(wmNewsStatistics);
        return result;
    }

    @Override
    public ResponseResult findFansStatistics(StatisticDto dto) {
        ResponseResult result = check(dto);
        if (result != null){
            return result;
        }
        WmUser user = queryWmUser();
        Long userId = user.getApUserId();
        String burst = BurstUtils.groudOne(userId);
        List<WmFansStatistics> fansStatistics = wmFansStatisticsMapper.findByTimeAndUserId(burst, userId, dto);
        result = ResponseResult.okResult(fansStatistics);
        return result;
    }

    private WmUser queryWmUser(){
        WmUser wmUser = WmThreadLocalUtils.getUser();
        WmUser user = wmUserMapper.selectById(wmUser.getId());
        return user;
    }

    private ResponseResult check(StatisticDto dto){
        if (dto == null || dto.getType() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        if (dto.getType() != WmMediaConstans.WM_NEWS_STATISTIC_CUR &&
        dto.getEtime() == null || dto.getStime() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        return null;
    }
}
