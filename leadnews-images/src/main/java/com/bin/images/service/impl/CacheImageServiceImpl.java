package com.bin.images.service.impl;

import com.bin.common.fastdfs.FastDfsClient;
import com.bin.images.service.CacheImageService;
import com.bin.utils.common.Base64Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author huangbin
 */
@Service
@Slf4j
public class CacheImageServiceImpl implements CacheImageService {

    @Autowired
    private FastDfsClient fastDfsClient;
    @Autowired
    private StringRedisTemplate redisTemplate;
    /**
     * 24小时
     */
    final long EXPIRE = 60 * 60 * 24L;

    @Override
    public byte[] cache2Redis(String imgUrl, boolean isCache) {
        byte[] ret = null;
        log.info("缓存图片到redis#imgUrl:{},isCache:{}", imgUrl, isCache);
        //http://47.94.7.85/group1/M00/00/00/rBENvl02ZtKAEgFqAACNdiGk7IM981.jpg
        Map<String,String> map = formatPath(imgUrl);
        String group = map.get("group");
        String file = map.get("file");
        String baseString = "";
        try {
            byte[] fileByte = fastDfsClient.downGroupFile(group, file);
            ret = fileByte;
            baseString = Base64Utils.encode(fileByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(isCache){
            redisTemplate.opsForValue().set(imgUrl, baseString, EXPIRE, TimeUnit.SECONDS);
        }
        return ret;
    }

    @Override
    public void resetCache2Redis(String imageKey) {
        redisTemplate.expire(imageKey, EXPIRE, TimeUnit.SECONDS);
    }

    /**
     * 解析图片URL
     * @param imgUrl
     * @return
     */
    private Map<String,String> formatPath(String imgUrl){
        Map<String,String> map = new HashMap<>();
        String groupString = imgUrl.substring(imgUrl.indexOf("group"),imgUrl.length());
        int index = groupString.indexOf("/");
        map.put("group", groupString.substring(0,index));
        map.put("file", groupString.substring(index+1,groupString.length()));
        return map;
    }
}