package com.bin.behavior.service.impl;

import com.bin.behavior.service.AppShowBehaviorService;
import com.bin.model.article.pojos.ApArticle;
import com.bin.model.behavior.dtos.ShowBehaviorDto;
import com.bin.model.behavior.pojos.ApBehaviorEntry;
import com.bin.model.behavior.pojos.ApShowBehavior;
import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.common.enums.AppHttpCodeEnum;
import com.bin.model.mappers.app.ApBehaviorEntryMapper;
import com.bin.model.mappers.app.AppShowBehaviorMapper;
import com.bin.model.user.pojos.ApUser;
import com.bin.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/6 21:42
 */
@Service
@SuppressWarnings("all")
public class AppShowBehaviorServiceImpl implements AppShowBehaviorService {
    @Autowired
    private ApBehaviorEntryMapper apBehaviorEntryMapper;

    @Autowired
    private AppShowBehaviorMapper appShowBehaviorMapper;

    @Override
    public ResponseResult saveShowBehavior(ShowBehaviorDto dto) {
        //获取用户信息，获取设备id
        ApUser user = AppThreadLocalUtils.getUser();
        //根据当前的用户id或设备id查询行为主体（主要是保存了用户id、或用户的设备id（手机的编号信息））
        //判断当前的用户信息登录
        if (user == null && dto.getEquipmentId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        Long userId = null;
        if (user != null){
            userId = user.getId();
        }
        //查询行为实体
        ApBehaviorEntry apBehaviorEntry = apBehaviorEntryMapper.selectByUserIdOrEquipment(userId, dto.getEquipmentId());
        if (apBehaviorEntry == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //获取传递过来的文章列表id
        List<Integer> articleIds = dto.getArticleIds().stream().map(ApArticle::getId).collect(Collectors.toList());
        //根据行为实体id和文章列表id查询app行为表
        List<ApShowBehavior> showBehaviorList = appShowBehaviorMapper.selectListByEntryIdAndArticleIds(apBehaviorEntry.getEntryId(), articleIds);
        //数据的过滤,需要删除表中已存在的文章id
        if (!showBehaviorList.isEmpty()){
            showBehaviorList.forEach(item ->{
                articleIds.remove(item.getArticleId());
            });
        }
        //保存操作
        if (!articleIds.isEmpty()){
            appShowBehaviorMapper.saveBehaviors(apBehaviorEntry.getEntryId(), articleIds);
        }
        return ResponseResult.okResult(0);
    }
}
