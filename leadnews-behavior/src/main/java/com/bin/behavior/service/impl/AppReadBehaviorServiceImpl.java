package com.bin.behavior.service.impl;

import com.bin.behavior.kafka.BehaviorMessageSender;
import com.bin.behavior.service.AppReadBehaviorService;
import com.bin.common.kafka.messages.behavior.UserReadMessage;
import com.bin.common.zookeeper.sequence.Sequences;
import com.bin.model.behavior.dtos.ReadBehaviorDto;
import com.bin.model.behavior.pojos.ApBehaviorEntry;
import com.bin.model.behavior.pojos.ApReadBehavior;
import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.common.enums.AppHttpCodeEnum;
import com.bin.model.mappers.app.ApBehaviorEntryMapper;
import com.bin.model.mappers.app.ApReadBehaviorMapper;
import com.bin.model.user.pojos.ApUser;
import com.bin.utils.common.BurstUtils;
import com.bin.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/24 17:02
 */
@Service
public class AppReadBehaviorServiceImpl implements AppReadBehaviorService {

    @Autowired
    private ApBehaviorEntryMapper apBehaviorEntryMapper;

    @Autowired
    private ApReadBehaviorMapper apReadBehaviorMapper;

    @Autowired
    private Sequences sequences;

    @Autowired
    private BehaviorMessageSender behaviorMessageSender;

    @Override
    public ResponseResult saveReadBehavior(ReadBehaviorDto dto) {
        ApUser user = AppThreadLocalUtils.getUser();
        // 用户和设备不能同时为空
        if(user==null&& dto.getEquipmentId()==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        Long userId = null;
        if(user!=null){
            userId = user.getId();
        }
        ApBehaviorEntry entry = apBehaviorEntryMapper.selectByUserIdOrEquipment(userId, dto.getEquipmentId());
        // 行为实体找以及注册了，逻辑上这里是必定有值得，除非参数错误
        if(entry==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        ApReadBehavior readBehavior = apReadBehaviorMapper.selectByEntryId(BurstUtils.groudOne(entry.getId()), entry.getId(), dto.getArticleId());
        boolean flag = false;
        int count = -1;
        if (readBehavior == null){
            flag = true;
            readBehavior.setId(sequences.sequenceApReadBehavior());
            readBehavior.setEntryId(entry.getId());
            readBehavior.setArticleId(dto.getArticleId());
            readBehavior.setCount(dto.getCount());
            readBehavior.setCreatedTime(new Date());
            readBehavior.setUpdatedTime(new Date());
            readBehavior.setReadDuration(dto.getReadDuration());
            readBehavior.setBurst(BurstUtils.encrypt(readBehavior.getId(), readBehavior.getEntryId()));

            if (flag){
                count = apReadBehaviorMapper.insert(readBehavior);
                if (count == 1){
                    behaviorMessageSender.sendMessageReduce(new UserReadMessage(readBehavior), userId, true);
                }
            }else{
                count = apReadBehaviorMapper.update(readBehavior);
            }

        }
        return ResponseResult.okResult(count);
    }
}
