package com.bin.behavior.config;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/25 11:55
 */
@Configuration
@ServletComponentScan("com.bin.common.web.app.security")
public class SecurityConfig {
}
