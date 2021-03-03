package com.bin.apis.media.controllerApi;

import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.media.dtos.WmMaterialDto;
import com.bin.model.media.dtos.WmMaterialListDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/26 11:14
 */
public interface MaterialManageControllerApi {
    /**
     * 上传图片
     * @param file
     * @return
     */
    ResponseResult uploadPicture(MultipartFile file);

    /**
     * 删除图片
     * @param dto
     * @return
     */
    ResponseResult deletePicture(WmMaterialDto dto);

    /**
     * 图片列表展示
     * @param dto
     * @return
     */
    ResponseResult list(WmMaterialListDto dto);

    /**
     * 收藏图片
     * @param dto
     * @return
     */
    ResponseResult collectionMaterial(WmMaterialDto dto);

    /**
     * 取消收藏
     * @param dto
     * @return
     */
    ResponseResult cancelCollectionMaterial(WmMaterialDto dto);
}
