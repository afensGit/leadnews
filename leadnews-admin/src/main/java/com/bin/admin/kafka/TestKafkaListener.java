package com.bin.admin.kafka;

import com.bin.common.kafka.KafkaListener;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

/**
 * @author huangbin
 */
@Component
public class TestKafkaListener implements KafkaListener {
    @Override
    public String topic() {
        return "topic.test";
    }

    @Override
    public void onMessage(ConsumerRecord consumerRecord, Consumer consumer) {
        System.out.println("接收到的消息为----》"+consumerRecord);
    }

    @Override
    public void onMessage(Object o) {

    }
}
