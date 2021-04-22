package com.bin.images.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangbin
 */
@Configuration
@ComponentScan({"com.bin.common.kafka","com.bin.common.kafkastream"})
public class KafkaConfig {
}
