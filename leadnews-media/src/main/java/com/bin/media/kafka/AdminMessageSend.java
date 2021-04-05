package com.bin.media.kafka;

import com.bin.common.kafka.KafkaSender;
import com.bin.common.kafka.messages.SubmitArticleAuthMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author huangbin
 */
@Component
public class AdminMessageSend {

    @Autowired
    private KafkaSender kafkaSender;

    /**
     * 只发送行为消息
     * @param message
     */
    @Async
    public void sendMessage(SubmitArticleAuthMessage message){
        kafkaSender.sendSubmitArticleAuthMessage(message);
    }
}
