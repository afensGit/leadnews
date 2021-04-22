package com.bin.crawler.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author huangbin
 */
@Configuration
@ComponentScan({"com.bin.common.common.init", "com.bin.common.mysql.core", "com.bin.common.kafka"})
@EnableScheduling
public class InitConfig {
}
