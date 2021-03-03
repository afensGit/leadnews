package com.bin.user.service.impl;

import com.bin.model.behavior.dtos.FollowBehaviorDto;
import com.bin.model.behavior.pojos.ApBehaviorEntry;
import com.bin.model.behavior.pojos.ApFollowBehavior;
import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.common.enums.AppHttpCodeEnum;
import com.bin.model.mappers.app.ApBehaviorEntryMapper;
import com.bin.model.mappers.app.ApFollowBehaviorMapper;
import com.bin.model.user.pojos.ApUser;
import com.bin.user.service.FollowBehaviorService;
import com.bin.utils.threadlocal.AppThreadLocalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/23 23:13
 */
@Service
public class FollowBehaviorServiceImpl implements FollowBehaviorService {

    private static final Logger logger = LoggerFactory.getLogger(FollowBehaviorServiceImpl.class);

    @Autowired
    private ApBehaviorEntryMapper apBehaviorEntryMapper;

    @Autowired
    private ApFollowBehaviorMapper apFollowBehaviorMapper;

    @Override
    @Async
    public ResponseResult saveFollowBehavior(FollowBehaviorDto dto) {
        logger.info("异步存储关注信息[{}]", dto);
        ApUser user = AppThreadLocalUtils.getUser();
        if (user == null && dto.getEquipmentId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        Long userId = null;
        if (user != null){
            userId = user.getId();
        }
        ApBehaviorEntry entry = apBehaviorEntryMapper.selectByUserIdOrEquipment(userId, dto.getEquipmentId());
        if (entry == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        ApFollowBehavior followBehavior = new ApFollowBehavior();
        followBehavior.setArticleId(dto.getArticleId());
        followBehavior.setCreatedTime(new Date());
        followBehavior.setEntryId(entry.getEntryId());
        followBehavior.setFollowId(dto.getFollowId());
        int count = apFollowBehaviorMapper.insert(followBehavior);

        return ResponseResult.okResult(count);
    }
}
