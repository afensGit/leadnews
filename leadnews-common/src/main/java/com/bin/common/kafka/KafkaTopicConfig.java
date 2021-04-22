package com.bin.common.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author huangbin
 */
@Data
@Configuration
@ConfigurationProperties(prefix="kafka.topic")
@PropertySource("classpath:kafka.properties")
public class KafkaTopicConfig {
    //String adminTest;

    private String submitArticleAuth;
    private String articleAuditSuccess;
    private String articleUpdateBus;
    private String articleIncrHandle;
    private String hotArticle;
}