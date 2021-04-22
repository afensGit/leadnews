package com.bin.behavior.kafka;

import com.bin.common.kafka.KafkaMessage;
import com.bin.common.kafka.KafkaSender;
import com.bin.common.kafka.messages.app.UpdateArticleMessage;
import com.bin.common.kafka.messages.behavior.UserLikesMessage;
import com.bin.common.kafka.messages.behavior.UserReadMessage;
import com.bin.model.article.pojos.ApCollection;
import com.bin.model.behavior.pojos.ApLikesBehavior;
import com.bin.model.mess.app.UpdateArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author huangbin
 */
@Component
public class BehaviorMessageSender {

    @Autowired
    KafkaSender kafkaSender;

    /**
     * 发送+1的消息
     * @param message
     * @param isSendToArticle
     */
    @Async
    public void sendMessagePlus(KafkaMessage message, Long apUserId, boolean isSendToArticle){
        if(isSendToArticle){
            UpdateArticleMessage temp = parseMessage(message,apUserId,1);
            if(temp!=null)
                kafkaSender.sendArticleUpdateBus(temp);
        }
    }

    /**
     * 发送-1的消息
     * @param message
     * @param isSendToArticle
     */
    @Async
    public void sendMessageReduce(KafkaMessage message,Long apUserId,boolean isSendToArticle){
        if(isSendToArticle){
            UpdateArticleMessage temp = parseMessage(message,apUserId,-1);
            if(temp!=null)
                kafkaSender.sendArticleUpdateBus(temp);
        }
    }

    /**
     * 转换行为消息为修改位置的消息
     * @param message
     * @param step
     * @return
     */
    private UpdateArticleMessage parseMessage(KafkaMessage message,Long apUserId,int step){
        UpdateArticle ua = new UpdateArticle();
        if(apUserId!=null) {
            ua.setApUserId(apUserId.intValue());
        }
        if(message instanceof UserLikesMessage){
            UserLikesMessage m = (UserLikesMessage)message;
            // 只发文章的喜欢消息
            if(m.getData().getType()== ApLikesBehavior.Type.ARTICLE.getCode()){
                ua.setType(UpdateArticle.UpdateArticleType.LIKES);
                ua.setAdd(step);
                ua.setArticleId(m.getData().getEntryId());
                ua.setBehaviorId(m.getData().getBehaviorEntryId());
            }
        }else if(message instanceof UserReadMessage){
            UserReadMessage m = (UserReadMessage) message;
            ua.setType(UpdateArticle.UpdateArticleType.VIEWS);
            ua.setAdd(step);
            ua.setArticleId(m.getData().getArticleId());
            ua.setBehaviorId(m.getData().getEntryId());
        }
        if(ua.getArticleId()!=null){
            return new UpdateArticleMessage(ua);
        }
        return null;
    }


}