package com.bin.admin.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author huangbin
 */
@Configuration
@ComponentScan({"com.bin.common.mysql.core","com.bin.common.common.init","com.bin.common.quartz"})
@MapperScan("com.bin.admin.dao")
@EnableScheduling
public class InitConfig {
}
