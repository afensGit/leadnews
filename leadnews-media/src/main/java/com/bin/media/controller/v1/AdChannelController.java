package com.bin.media.controller.v1;

import com.bin.apis.media.controllerApi.AdChannelControllerApi;
import com.bin.media.service.AdChannelService;
import com.bin.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/3/8 17:41
 */

@RestController
@RequestMapping("/api/v1/channel")
public class AdChannelController implements AdChannelControllerApi {

    @Autowired
    private AdChannelService adChannelService;

    @Override
    @GetMapping("/channels")
    public ResponseResult selectAll() {
        return adChannelService.selectAll();
    }
}
