package com.bin.images.kafka.listener;

import com.bin.common.kafka.KafkaListener;
import com.bin.common.kafka.KafkaTopicConfig;
import com.bin.common.kafka.messages.app.ApHotArticleMessage;
import com.bin.images.service.HotArticleImageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HotArticleListener implements KafkaListener<String,String> {

    @Autowired
    KafkaTopicConfig kafkaTopicConfig;

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    HotArticleImageService hotArticleImageService;

    @Override
    public String topic() {
        return kafkaTopicConfig.getHotArticle();
    }

    @Override
    public void onMessage(ConsumerRecord<String, String> data, Consumer<?, ?> consumer) {
        log.info("receive hot article message:{}",data);
        String value = (String)data.value();
        try {
            ApHotArticleMessage message = mapper.readValue(value, ApHotArticleMessage.class);
            hotArticleImageService.handleHotImage(message);
        }catch (Exception e){
            log.error("kafka send message[class:{}] to handleHotImage failed:{}","ApHotArticleMessage.class",e);
        }
    }
}