package com.bin.apis.images.apis;

import java.awt.image.BufferedImage;

/**
 * @author huangbin
 */
public interface ImagesControllerApi {

    /**
     * 访问缓存图片
     * @param imagePath
     * @return
     * @throws Exception
     */
    BufferedImage getImage(String imagePath) throws Exception;
}
