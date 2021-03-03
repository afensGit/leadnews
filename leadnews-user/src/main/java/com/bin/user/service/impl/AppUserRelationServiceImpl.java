package com.bin.user.service.impl;

import com.bin.common.zookeeper.sequence.Sequences;
import com.bin.model.article.pojos.ApAuthor;
import com.bin.model.behavior.dtos.FollowBehaviorDto;
import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.common.enums.AppHttpCodeEnum;
import com.bin.model.mappers.app.ApAuthorMapper;
import com.bin.model.mappers.app.ApUserFanMapper;
import com.bin.model.mappers.app.ApUserFollowMapper;
import com.bin.model.mappers.app.ApUserMapper;
import com.bin.model.user.dtos.UserRelationDto;
import com.bin.model.user.pojos.ApUser;
import com.bin.model.user.pojos.ApUserFan;
import com.bin.model.user.pojos.ApUserFollow;
import com.bin.user.service.AppUserRelationService;
import com.bin.user.service.FollowBehaviorService;
import com.bin.utils.common.BurstUtils;
import com.bin.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/24 9:15
 */

@Service
public class AppUserRelationServiceImpl implements AppUserRelationService {

    @Autowired
    private ApAuthorMapper apAuthorMapper;

    @Autowired
    private ApUserFollowMapper apUserFollowMapper;

    @Autowired
    private ApUserFanMapper apUserFanMapper;

    @Autowired
    private Sequences sequences;

    @Autowired
    private ApUserMapper apUserMapper;

    @Autowired
    private FollowBehaviorService followBehaviorService;
    /**
     * 关注或取消关注
     * @param dto
     * @return
     */
    @Override
    public ResponseResult follow(UserRelationDto dto) {
        if (dto.getOperation() == null || dto.getOperation() < 0 || dto.getOperation() > 1){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "无效的operation参数");
        }
        Integer followId = dto.getUserId();
        if (followId == null && dto.getAuthorId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE, "followId或articleId不能为空");
        }else if (followId == null){
            ApAuthor author = apAuthorMapper.selectById(dto.getAuthorId());
            if (author != null){
                followId = author.getUserId().intValue();
            }
        }
        if (followId == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "关注人不存在");
        }else {
            ApUser user = AppThreadLocalUtils.getUser();
            if (user != null){
                if (dto.getOperation() == 0){
                    return followByUserId(user, followId, dto.getArticleId());
                }else {
                    return followCancelByUserId(user, followId);
                }
            }else {
                return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            }
        }
        //return null;
    }

    /**
     * 处理关注业务逻辑
     * @param user
     * @param followId
     * @param articleId
     * @return
     */
    private ResponseResult followByUserId(ApUser user, Integer followId, Integer articleId){
        ApUser follower = apUserMapper.selectById(followId);
        if (follower == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "关注人不存在");
        }
        ApUserFollow userFollow = apUserFollowMapper.selectByFollowId(BurstUtils.groudOne(user.getId()), user.getId(), followId);
        if (userFollow == null){
            ApUserFan userFan = apUserFanMapper.selectFansById(BurstUtils.groudOne(followId), followId, user.getId());
            if (userFan == null){
                userFan.setId(sequences.sequenceApUserFan());
                userFan.setFansId(user.getId());
                userFan.setFansName(user.getName());
                userFan.setUserId(followId);
                userFan.setCreatedTime(new Date());
                userFan.setBurst(BurstUtils.encrypt(userFan.getId(), userFan.getUserId()));
                userFan.setLevel((short) 0);
                userFan.setIsDisplay(true);
                userFan.setIsShieldComment(false);
                userFan.setIsShieldLetter(false);
                apUserFanMapper.insert(userFan);
            }
            userFollow = new ApUserFollow();
            userFollow.setId(sequences.sequenceApUserFollow());
            userFollow.setFollowId(followId);
            userFollow.setFollowName(follower.getName());
            userFollow.setUserId(user.getId());
            userFollow.setCreatedTime(new Date());
            userFollow.setBurst(BurstUtils.encrypt(userFollow.getId(), userFollow.getUserId()));
            userFollow.setLevel((short) 0);
            userFollow.setIsNotice(true);
            //记录关注行为
            FollowBehaviorDto followBehaviorDto = new FollowBehaviorDto();
            followBehaviorDto.setArticleId(articleId);
            followBehaviorDto.setEquipmentId(user.getId().intValue());
            followBehaviorDto.setFollowId(followId);
            followBehaviorService.saveFollowBehavior(followBehaviorDto);
            return ResponseResult.okResult(apUserFollowMapper.insert(userFollow));
        }else {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST, "已关注");
        }
    }

    /**
     * 处理取消关注
     * @param user
     * @param followId
     * @return
     */
    private ResponseResult followCancelByUserId(ApUser user, Integer followId){
        ApUserFollow userFollow = apUserFollowMapper.selectByFollowId(BurstUtils.groudOne(user.getId()), user.getId(), followId);
        if (userFollow == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "未关注");
        }else {
            ApUserFan userFan = apUserFanMapper.selectFansById(BurstUtils.groudOne(followId), followId, user.getId());
            if (userFan != null){
                apUserFanMapper.deleteFansById(BurstUtils.groudOne(followId), followId, user.getId());
            }
            return ResponseResult.okResult(apUserFollowMapper.deleteByFollowId(BurstUtils.groudOne(user.getId()), user.getId(), followId));
        }
    }
}
