package com.bin.media.controller.v1;

import com.bin.apis.media.controllerApi.MaterialManageControllerApi;
import com.bin.media.constans.WmMediaConstans;
import com.bin.media.service.MaterialService;
import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.media.dtos.WmMaterialDto;
import com.bin.model.media.dtos.WmMaterialListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/26 11:16
 */

@RestController
@RequestMapping("/api/v1/media/material")
public class MaterialManageController implements MaterialManageControllerApi {

    @Autowired
    private MaterialService materialService;

    @Override
    @PostMapping("/upload_picture")
    public ResponseResult uploadPicture(MultipartFile file) {
        return materialService.uploadPicture(file);
    }

    @Override
    @PostMapping("/del_picture")
    public ResponseResult deletePicture(@RequestBody WmMaterialDto dto) {
        return materialService.deletePicture(dto);
    }

    @Override
    @PostMapping("/list")
    public ResponseResult list(@RequestBody WmMaterialListDto dto) {
        return materialService.findList(dto);
    }

    @Override
    @PostMapping("/collect")
    public ResponseResult collectionMaterial(@RequestBody WmMaterialDto dto) {
        return materialService.changeUserMaterialStatus(dto, WmMediaConstans.COLLECT_MATERIAL);
    }

    @Override
    @PostMapping("/cancel_collect")
    public ResponseResult cancelCollectionMaterial(@RequestBody WmMaterialDto dto) {
        return materialService.changeUserMaterialStatus(dto, WmMediaConstans.CANCEL_COLLECT_MATERIAL);
    }
}
