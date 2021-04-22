package com.bin.common.kafka.messages.app;

import com.bin.common.kafka.KafkaMessage;
import com.bin.model.mess.app.UpdateArticle;

/**
 * @author huangbin
 */
public class UpdateArticleMessage extends KafkaMessage<UpdateArticle> {

    public UpdateArticleMessage(){}

    public UpdateArticleMessage(UpdateArticle data) {
        super(data);
    }
    @Override
    public String getType() {
        return "update-article";
    }
}