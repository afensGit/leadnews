package com.bin.article.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/5 10:35
 */
@Configuration
@ComponentScan(value = "com.bin.common.mysql.core")
public class MysqlConfig {
}
