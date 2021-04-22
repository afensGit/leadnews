package com.bin.behavior.service.impl;

import com.bin.behavior.kafka.BehaviorMessageSender;
import com.bin.behavior.service.AppLikesBehaviorService;
import com.bin.common.kafka.messages.behavior.UserLikesMessage;
import com.bin.common.zookeeper.sequence.Sequences;
import com.bin.model.behavior.dtos.LikesBehaviorDto;
import com.bin.model.behavior.pojos.ApBehaviorEntry;
import com.bin.model.behavior.pojos.ApLikesBehavior;
import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.common.enums.AppHttpCodeEnum;
import com.bin.model.mappers.app.ApBehaviorEntryMapper;
import com.bin.model.mappers.app.ApLikesBehaviorMapper;
import com.bin.model.user.pojos.ApUser;
import com.bin.utils.common.BurstUtils;
import com.bin.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/24 12:56
 */

@Service
public class AppLikesBehaviorServiceImpl implements AppLikesBehaviorService {

    @Autowired
    private ApBehaviorEntryMapper apBehaviorEntryMapper;

    @Autowired
    private Sequences sequences;

    @Autowired
    private ApLikesBehaviorMapper apLikesBehaviorMapper;

    @Autowired
    private BehaviorMessageSender behaviorMessageSender;

    @Override
    public ResponseResult saveLikesBehavior(LikesBehaviorDto dto) {
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
        ApLikesBehavior likesBehavior = new ApLikesBehavior();
        likesBehavior.setId(sequences.sequenceApLikes());
        likesBehavior.setEntryId(dto.getEntryId());
        likesBehavior.setBehaviorEntryId(apBehaviorEntry.getEntryId());
        likesBehavior.setCreatedTime(new Date());
        likesBehavior.setOperation(dto.getOperation());
        likesBehavior.setType(dto.getType());
        likesBehavior.setBurst(BurstUtils.encrypt(likesBehavior.getId(), likesBehavior.getBehaviorEntryId()));
        int insert = apLikesBehaviorMapper.insert(likesBehavior);
        if (insert == 1){
            if (likesBehavior.getOperation() == ApLikesBehavior.Operation.LIKE.getCode()){
                behaviorMessageSender.sendMessagePlus(new UserLikesMessage(likesBehavior), userId, true );
            }else if (likesBehavior.getOperation() == ApLikesBehavior.Operation.CANCEL.getCode()){
                behaviorMessageSender.sendMessageReduce(new UserLikesMessage(likesBehavior), userId, true);
            }
        }
        return ResponseResult.okResult(insert);
    }
}
