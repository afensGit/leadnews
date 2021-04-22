package com.bin.common.kafka.messages.app;

import com.bin.common.kafka.KafkaMessage;
import com.bin.model.article.pojos.ApHotArticles;

/**
 * @author huangbin
 */
public class ApHotArticleMessage extends KafkaMessage<ApHotArticles> {

    public ApHotArticleMessage(){

    }

    public ApHotArticleMessage(ApHotArticles data){
        super(data);
    }

    @Override
    public String getType() {
        return "hot-article";
    }
}