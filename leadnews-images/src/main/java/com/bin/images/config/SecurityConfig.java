package com.bin.images.config;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangbin
 */
@Configuration
@ServletComponentScan("com.bin.common.web.app.security")
public class SecurityConfig {

}