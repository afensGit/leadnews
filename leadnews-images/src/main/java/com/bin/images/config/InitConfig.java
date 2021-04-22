package com.bin.images.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangbin
 */
@Configuration
@ComponentScan({"com.bin.common.common.init"})
public class InitConfig {
    public static String PREFIX = "http://47.118.64.33";
}
