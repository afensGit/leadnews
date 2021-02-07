package com.bin.behavior.controller;

import com.bin.apis.article.controllerApi.BehaviorControllerApi;
import com.bin.behavior.service.AppShowBehaviorService;
import com.bin.model.behavior.dtos.ShowBehaviorDto;
import com.bin.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


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
}
