package com.bin.admin.kafka;

import com.bin.admin.service.ReviewMediaArticleService;
import com.bin.common.kafka.KafkaListener;
import com.bin.common.kafka.KafkaTopicConfig;
import com.bin.common.kafka.messages.SubmitArticleAuthMessage;
import com.bin.model.mess.admin.SubmitArticleAuto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author huangbin
 */

@Component
@Slf4j
public class AutoReviewArticleListener implements KafkaListener<String, String> {

    @Autowired
    private KafkaTopicConfig kafkaTopicConfig;

    @Autowired
    private ReviewMediaArticleService reviewMediaArticleService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String topic() {
        return kafkaTopicConfig.getSubmitArticleAuth();
    }

    @Override
    public void onMessage(ConsumerRecord<String, String> consumerRecord, Consumer<?, ?> consumer) {
        String value = consumerRecord.value();
        log.info("接收到文章审核消息");
        log.info("消息内容：[{}]", value);
        try {
            SubmitArticleAuthMessage articleAuthMessage = objectMapper.readValue(value, SubmitArticleAuthMessage.class);
            if (articleAuthMessage != null){
                SubmitArticleAuto.ArticleType type = articleAuthMessage.getData().getType();
                if (type == SubmitArticleAuto.ArticleType.WEMEDIA){
                    Integer articleId = articleAuthMessage.getData().getArticleId();
                    if (articleId != null){
                        reviewMediaArticleService.autoReviewMediaArticle(articleId);
                    }
                }
            }
        }catch (Exception e){
            log.error("文章审核出现异常");
            log.error("异常信息：[{}]", e);
        }
    }
}
