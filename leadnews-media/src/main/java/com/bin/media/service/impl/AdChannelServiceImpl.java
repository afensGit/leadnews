package com.bin.media.service.impl;

import com.bin.media.service.AdChannelService;
import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.mappers.admin.AdChannelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/3/8 17:33
 */

@Service
public class AdChannelServiceImpl implements AdChannelService {

    @Autowired
    private AdChannelMapper adChannelMapper;

    @Override
    public ResponseResult selectAll() {
        return ResponseResult.okResult(adChannelMapper.selectAll());
    }
}
