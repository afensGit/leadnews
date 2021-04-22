package com.bin.article.kafka.listener;

import com.bin.article.service.ArticleHomeService;
import com.bin.common.kafka.KafkaListener;
import com.bin.common.kafka.KafkaTopicConfig;
import com.bin.common.kafka.messages.app.ArticleVisitStreamMessage;
import com.bin.model.mess.app.ArticleVisitStreamDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 增量文章状态处理
 * @author huangbin
 */
@Component
public class ArticleIncrHandleListener implements KafkaListener<String,String> {
    static Logger logger  = LoggerFactory.getLogger(ArticleIncrHandleListener.class);

    @Autowired
    private KafkaTopicConfig kafkaTopicConfig;

    ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private ArticleHomeService articleHomeService;

    @Override
    public String topic() {
        return kafkaTopicConfig.getArticleIncrHandle();
    }

    @Override
    public void onMessage(ConsumerRecord<String, String> data, Consumer<?, ?> consumer) {
        logger.info("receive Article Incr Handle message:{}",data);
        String value = data.value();
        try {
            ArticleVisitStreamMessage message = mapper.readValue(value, ArticleVisitStreamMessage.class);
            ArticleVisitStreamDto dto = message.getData();
            articleHomeService.updateArticleView(dto);
        }catch (Exception e){
            logger.error("kafka send message[class:{}] to Article Incr Handle failed:{}","ArticleIncrHandle.class",e);
        }
    }
}