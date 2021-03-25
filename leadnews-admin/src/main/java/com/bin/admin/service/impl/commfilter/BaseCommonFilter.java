package com.bin.admin.service.impl.commfilter;

import com.bin.model.admin.dtos.CommonDto;
import com.bin.model.admin.dtos.CommonWhereDto;
import com.bin.model.admin.pojos.AdUser;

/**
 * @author huangbin
 */
public interface BaseCommonFilter {
    /**
     * 查询增强方法
     * @param adUser
     * @param dto
     */
    void doListAfter(AdUser adUser, CommonDto dto);

    /**
     * 更新增强方法
     * @param adUser
     * @param dto
     */
    void doUpdateAfter(AdUser adUser, CommonDto dto);

    /**
     * 插入增强方法
     * @param adUser
     * @param dto
     */
    void doInsertAfter(AdUser adUser, CommonDto dto);

    /**
     * 删除增强方法
     * @param adUser
     * @param dto
     */
    void doDeleteAfter(AdUser adUser, CommonDto dto);

    /**
     * 获取更新字段的值
     * @param filed
     * @param dto
     * @return
     */
    default CommonWhereDto findUpdateFiled(String filed, CommonDto dto){
        if (dto != null){
            for (CommonWhereDto dtoSet : dto.getSets()) {
                if (filed.equals(dtoSet)){
                    return dtoSet;
                }
            }
        }
        return null;
    }

    default CommonWhereDto findWhereFiled(String filed, CommonDto dto){
        if (dto != null){
            for (CommonWhereDto whereDto : dto.getWhere()) {
                if (whereDto.equals(filed)){
                    return whereDto;
                }
            }
        }
        return null;
    }
}
