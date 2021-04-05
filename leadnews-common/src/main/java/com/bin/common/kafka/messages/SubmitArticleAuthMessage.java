package com.bin.common.kafka.messages;

import com.bin.common.kafka.KafkaMessage;
import com.bin.model.mess.admin.SubmitArticleAuto;

/**
 * @author huangbin
 */
public class SubmitArticleAuthMessage extends KafkaMessage<SubmitArticleAuto> {

    public SubmitArticleAuthMessage(){

    }

    public SubmitArticleAuthMessage(SubmitArticleAuto data){
        super(data);
    }

    @Override
    protected String getType() {
        return "submit-article-auth";
    }
}
