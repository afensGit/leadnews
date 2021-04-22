package com.bin.common.kafka.messages.app;

import com.bin.common.kafka.KafkaMessage;
import com.bin.model.mess.app.ArticleVisitStreamDto;

/**
 * @author huangbin
 */
public class ArticleVisitStreamMessage extends KafkaMessage<ArticleVisitStreamDto> {

    @Override
    public String getType() {
        return "article-visit-stream";
    }
}