package com.bin.behavior.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/25 11:51
 */

@Configuration
@ComponentScan({"com.bin.common.common.init","com.bin.common.kafka"})
public class InitConfig {
}
