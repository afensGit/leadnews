package com.bin.admin.service;

import com.bin.admin.dao.CommonDao;
import com.bin.model.admin.dtos.CommonDto;
import com.bin.model.common.dtos.ResponseResult;

/**
 * @author huangbin
 */
public interface CommonService {
    /**
     * 通用列表查询方法
     * @param dto
     * @return
     */
    ResponseResult list(CommonDto dto);

    /**
     * 通用的插入更新方法
     * @param dto
     * @return
     */
    ResponseResult update(CommonDto dto);

    /**
     * 通用的删除方法
     * @param dto
     * @return
     */
    ResponseResult delete(CommonDto dto);
}
