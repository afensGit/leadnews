package com.bin.common.kafka;

import org.springframework.kafka.listener.ConsumerAwareMessageListener;

/**
 * 消息监听实现接口
 * @author huangbin
 */
public interface KafkaListener<K,V> extends ConsumerAwareMessageListener<K,V> {

    String topic();

    default String factory(){
        return "defaultKafkaListenerContainerFactory";
    }

    default  String group(){ return "default";}

}