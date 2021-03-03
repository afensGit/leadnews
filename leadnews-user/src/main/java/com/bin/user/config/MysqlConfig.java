package com.bin.user.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/23 23:24
 */
@Configuration
@ComponentScan(value = "com.bin.common.mysql.core")
public class MysqlConfig {
}
