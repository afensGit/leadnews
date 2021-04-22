package com.bin.common.kafka;

import com.bin.common.kafka.messages.SubmitArticleAuthMessage;
import com.bin.common.kafka.messages.app.ApHotArticleMessage;
import com.bin.model.article.pojos.ApHotArticles;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author huangbin
 */
@Component
public class KafkaSender {

    Logger logger = LoggerFactory.getLogger(KafkaSender.class);

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    KafkaTopicConfig kafkaTopicConfig;

    /**
     * 发送一个消息
     * @param topic
     * @param key
     * @param message
     */
    public void sendMessage(String topic,String key,KafkaMessage<?> message){
        try {
            this.kafkaTemplate.send(topic, key, mapper.writeValueAsString(message));
        }catch (Exception e){
            logger.error("send message to [{}] error:",topic,e);
        }
    }

    /**
     * 发送一个不包装的消息
     * 只能是内部使用，拒绝业务上使用
     * @param topic
     * @param key
     * @param message
     */
    public void sendMessageNoWrap(String topic,String key,String message){
        try {
            this.kafkaTemplate.send(topic, key, message);
        }catch (Exception e){
            logger.error("send message to [{}] error:",topic,e);
        }
    }

    public void sendSubmitArticleAuthMessage(SubmitArticleAuthMessage message){
        this.sendMessage(kafkaTopicConfig.getSubmitArticleAuth(), UUID.randomUUID().toString(), message);
    }

    /**
     * 发送修改文章请求消息
     *
     * @param message
     */
    public void sendArticleUpdateBus(KafkaMessage message) {
        this.sendMessage(kafkaTopicConfig.getArticleUpdateBus(), UUID.randomUUID().toString(), message);
    }

    /**
     * 发送处理热文章图片信息
     *
     * @param message
     */
    public void sendHotArticleMessage(ApHotArticles message) {
        ApHotArticleMessage temp = new ApHotArticleMessage();
        temp.setData(message);
        this.sendMessage(kafkaTopicConfig.getHotArticle(), UUID.randomUUID().toString(), temp);
    }
}