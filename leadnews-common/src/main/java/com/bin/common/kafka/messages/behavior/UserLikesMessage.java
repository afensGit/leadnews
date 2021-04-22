package com.bin.common.kafka.messages.behavior;

import com.bin.common.kafka.KafkaMessage;
import com.bin.model.behavior.pojos.ApLikesBehavior;

/**
 * @author huangbin
 */
public class UserLikesMessage extends KafkaMessage<ApLikesBehavior> {

    public  UserLikesMessage(){}
    public UserLikesMessage(ApLikesBehavior data) {
        super(data);
    }

    @Override
    public String getType() {
        return "user-likes";
    }
}