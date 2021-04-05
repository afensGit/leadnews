package com.bin.model.mappers.app;

import com.bin.model.user.pojos.ApUserMessage;

/**
 * @author huangbin
 */
public interface ApUserMessageMapper  {
    /**
     * 添加记录
     * @param record
     * @return
     */
    int insertSelective(ApUserMessage record);
}
