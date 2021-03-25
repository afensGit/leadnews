package com.bin.admin.controller.v1;

import com.bin.admin.service.CommonService;
import com.bin.apis.admin.controllerApi.CommonControllerApi;
import com.bin.model.admin.dtos.CommonDto;
import com.bin.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangbin
 */
@RestController
@RequestMapping("/api/v1/admin/common")
public class CommonController implements CommonControllerApi {

    @Autowired
    private CommonService commonService;

    @Override
    @PostMapping("/list")
    public ResponseResult list(@RequestBody CommonDto dto) {
        return commonService.list(dto);
    }

    @Override
    @PostMapping("/update")
    public ResponseResult update(@RequestBody CommonDto dto) {
        return commonService.update(dto);
    }

    @Override
    @PostMapping("/delete")
    public ResponseResult delete(@RequestBody CommonDto dto) {
        return commonService.delete(dto);
    }
}
