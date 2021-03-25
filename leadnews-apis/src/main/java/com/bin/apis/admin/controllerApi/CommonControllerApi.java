package com.bin.apis.admin.controllerApi;

import com.bin.model.admin.dtos.CommonDto;
import com.bin.model.common.dtos.ResponseResult;

/**
 * @author huangbin
 */
public interface CommonControllerApi {
    /**
     * 通用列表查询接口
     * @param dto
     * @return
     */
    ResponseResult list(CommonDto dto);

    /**
     * 通用更新插入接口
     * @param dto
     * @return
     */
    ResponseResult update(CommonDto dto);

    /**
     * 通用删除接口
     * @param dto
     * @return
     */
    ResponseResult delete(CommonDto dto);
}
