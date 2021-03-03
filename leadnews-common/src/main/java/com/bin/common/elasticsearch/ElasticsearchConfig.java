package com.bin.common.elasticsearch;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/3/2 17:41
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.elasticsearch.jest")
@PropertySource("classpath:elasticsearch.properties")
public class ElasticsearchConfig {

    private String url;
    private Integer readTimeout;
    private Integer connectionTimeout;

    @Bean(name = "jestClient")
    public JestClient getJestClient(){
        JestClientFactory clientFactory = new JestClientFactory();
        clientFactory.setHttpClientConfig(new HttpClientConfig
                .Builder(this.url)
                .multiThreaded(true)
                .connTimeout(this.connectionTimeout)
                .readTimeout(this.readTimeout)
                .build());
        return clientFactory.getObject();
    }
}
