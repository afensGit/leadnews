package com.bin.images.service;

/**
 * @author huangbin
 */
public interface CacheImageService {

    /**
     * 缓存图片到redis
     * @param imgUrl
     * @param isCache
     * @return
     */
    byte[] cache2Redis(String imgUrl, boolean isCache);

    /**
     * 延长图片缓存
     * @param imageKey
     */
    void resetCache2Redis(String imageKey);
}