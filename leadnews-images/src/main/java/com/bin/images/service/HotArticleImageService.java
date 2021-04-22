package com.bin.images.service;

import com.bin.common.kafka.messages.app.ApHotArticleMessage;

/**
 * @author huangbin
 */
public interface HotArticleImageService {

    /**
     * 处理热文章消息
     * @param message
     */
    public void handleHotImage(ApHotArticleMessage message);
}