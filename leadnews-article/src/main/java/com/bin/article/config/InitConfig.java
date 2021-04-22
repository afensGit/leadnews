package com.bin.article.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author huangbin
 */
@Configuration
@ComponentScan({"com.bin.common.mysql.core","com.bin.common.common.init","com.bin.common.quartz","com.bin.common.kafka","com.bin.common.kafkastream"})
@EnableScheduling
public class InitConfig {
}
