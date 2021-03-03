package com.bin.behavior.service.impl;

import com.bin.behavior.service.AppUnlikesBehaviorService;
import com.bin.common.zookeeper.sequence.Sequences;
import com.bin.model.behavior.dtos.UnLikesBehaviorDto;
import com.bin.model.behavior.pojos.ApBehaviorEntry;
import com.bin.model.behavior.pojos.ApUnlikesBehavior;
import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.common.enums.AppHttpCodeEnum;
import com.bin.model.mappers.app.ApBehaviorEntryMapper;
import com.bin.model.mappers.app.ApUnlikesBehaviorMapper;
import com.bin.model.user.pojos.ApUser;
import com.bin.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/24 13:19
 */

@Service
public class AppUnlikesBehaviorServiceImpl implements AppUnlikesBehaviorService {

    @Autowired
    private ApBehaviorEntryMapper apBehaviorEntryMapper;

    @Autowired
    private ApUnlikesBehaviorMapper apUnlikesBehaviorMapper;

    @Override
    public ResponseResult saveUnlikesBehavior(UnLikesBehaviorDto dto) {
        ApUser user = AppThreadLocalUtils.getUser();
        // 用户和设备不能同时为空
        if(user==null&& dto.getEquipmentId()==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        Long userId = null;
        if(user!=null){
            userId = user.getId();
        }
        ApBehaviorEntry apBehaviorEntry = apBehaviorEntryMapper.selectByUserIdOrEquipment(userId, dto.getEquipmentId());
        // 行为实体找以及注册了，逻辑上这里是必定有值得，除非参数错误
        if(apBehaviorEntry==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        ApUnlikesBehavior unlikesBehavior = new ApUnlikesBehavior();
        unlikesBehavior.setArticleId(dto.getArticleId());
        unlikesBehavior.setCreatedTime(new Date());
        unlikesBehavior.setEntryId(apBehaviorEntry.getId());
        unlikesBehavior.setType(dto.getType());
        return ResponseResult.okResult(apUnlikesBehaviorMapper.insert(unlikesBehavior));
    }
}
