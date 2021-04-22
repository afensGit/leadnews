package com.bin.article.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangbin
 */
@Configuration
@ComponentScan("com.bin.common.redis")
public class RedisConfig {
}
