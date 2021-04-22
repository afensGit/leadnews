package com.bin.model.crawler.core.callback;

/**
 * 数据校验接口
 * @author huangbin
 */
public interface DataValidateCallBack {
    /**
     * 数据校验
     * @param content
     * @return
     */
    public boolean validate(String content);
}
