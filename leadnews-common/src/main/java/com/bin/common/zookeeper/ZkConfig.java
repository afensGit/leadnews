package com.bin.common.zookeeper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/16 13:50
 */

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "zk")
@PropertySource("classpath:zookeeper.properties")
public class ZkConfig {

    String host;
    String sequencePath;

    @Bean(name = "zookeeperClient")
    public ZookeeperClient zookeeperClient(){
        return new ZookeeperClient(this.host, this.sequencePath);
    }
}
