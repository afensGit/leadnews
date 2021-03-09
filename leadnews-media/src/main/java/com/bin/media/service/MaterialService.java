package com.bin.media.service;

import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.media.dtos.WmMaterialDto;
import com.bin.model.media.dtos.WmMaterialListDto;
import com.bin.model.media.dtos.WmNewsDto;
import com.bin.model.media.pojos.WmNews;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/26 10:54
 */
public interface MaterialService {
    /**
     * 上传图片接口
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
     * 查询图片列表
     * @param dto
     * @return
     */
    ResponseResult findList(WmMaterialListDto dto);

    /**
     * 收藏或取消收藏图片
     * @param dto
     * @param type
     * @return
     */
    ResponseResult changeUserMaterialStatus(WmMaterialDto dto, Short type);

}
