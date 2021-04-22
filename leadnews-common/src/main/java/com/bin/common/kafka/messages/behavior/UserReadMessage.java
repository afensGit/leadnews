package com.bin.common.kafka.messages.behavior;

import com.bin.common.kafka.KafkaMessage;
import com.bin.model.behavior.pojos.ApReadBehavior;

/**
 * @author huangbin
 */
public class UserReadMessage extends KafkaMessage<ApReadBehavior> {

    public UserReadMessage(){}
    public UserReadMessage(ApReadBehavior data) {
        super(data);
    }

    @Override
    public String getType() {
        return "user-read";
    }
}