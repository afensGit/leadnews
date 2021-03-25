package com.bin.behavior.controller;

import com.bin.apis.behavior.controllerApi.BehaviorControllerApi;
import com.bin.behavior.service.AppLikesBehaviorService;
import com.bin.behavior.service.AppReadBehaviorService;
import com.bin.behavior.service.AppShowBehaviorService;
import com.bin.behavior.service.AppUnlikesBehaviorService;
import com.bin.model.behavior.dtos.LikesBehaviorDto;
import com.bin.model.behavior.dtos.ReadBehaviorDto;
import com.bin.model.behavior.dtos.ShowBehaviorDto;
import com.bin.model.behavior.dtos.UnLikesBehaviorDto;
import com.bin.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/6 21:27
 */
@RestController
@RequestMapping("/api/v1/behavior")
public class BehaviorController implements BehaviorControllerApi {

    @Autowired
    private AppShowBehaviorService appShowBehaviorService;

    @Autowired
    private AppLikesBehaviorService appLikesBehaviorService;

    @Autowired
    private AppUnlikesBehaviorService appUnlikesBehaviorService;

    @Autowired
    private AppReadBehaviorService appReadBehaviorService;

    /**
     * 保存用户的文章行为
     * @param dto
     */
    @Override
    @PostMapping("/show_behavior")
    public ResponseResult saveShowBehavior(@RequestBody ShowBehaviorDto dto) {
        ResponseResult result = appShowBehaviorService.saveShowBehavior(dto);
        return result;
    }

    /**
     * 保存喜欢行为
     * @param dto
     * @return
     */
    @Override
    @PostMapping("/like_behavior")
    public ResponseResult saveLikesBehavior(@RequestBody LikesBehaviorDto dto) {
        return appLikesBehaviorService.saveLikesBehavior(dto);
    }

    /**
     * 保存不喜欢行为
     * @param dto
     * @return
     */
    @Override
    @PostMapping("/unlike_behavior")
    public ResponseResult saveUnlikesBehavior(@RequestBody UnLikesBehaviorDto dto) {
        return appUnlikesBehaviorService.saveUnlikesBehavior(dto);
    }

    /**
     * 保存用户阅读行为
     * @param dto
     * @return
     */
    @Override
    @PostMapping("/read_behavior")
    public ResponseResult saveReadBehavior(@RequestBody ReadBehaviorDto dto) {
        return appReadBehaviorService.saveReadBehavior(dto);
    }


}
