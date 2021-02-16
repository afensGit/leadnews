package com.bin.common.zookeeper;

import com.bin.common.zookeeper.sequence.ZkSequence;
import com.bin.common.zookeeper.sequence.ZkSequenceEnum;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/16 12:26
 */

@Setter
@Getter
public class ZookeeperClient {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperClient.class);

    private String host;
    private String sequencePath;
    // 重试休眠时间
    private final int SLEEP_TIME_MS = 1000;
    // 最大重试1000次
    private final int MAX_RETRIES = 1000;
    //会话超时时间
    private final int SESSION_TIMEOUT = 30 * 1000;
    //连接超时时间
    private final int CONNECTION_TIMEOUT = 3 * 1000;
    //创建连接实例
    CuratorFramework client = null;
    //序列化序号
    private Map<String, ZkSequence> zkSequenceMap = Maps.newConcurrentMap();

    public ZookeeperClient(String host, String sequencePath) {
        this.host = host;
        this.sequencePath = sequencePath;
    }

    @PostConstruct
    public void init() throws Exception{
        this.client = CuratorFrameworkFactory.builder()
                .connectString(this.getHost())
                .connectionTimeoutMs(CONNECTION_TIMEOUT)
                .sessionTimeoutMs(SESSION_TIMEOUT)
                .retryPolicy(new ExponentialBackoffRetry(SLEEP_TIME_MS, MAX_RETRIES)).build();
        this.client.start();
        this.initZkSequence();
    }

    public void initZkSequence(){
        ZkSequenceEnum[] values = ZkSequenceEnum.values();
        for (ZkSequenceEnum value : values) {
            String name = value.name();
            String path = this.sequencePath + name;
            ZkSequence zkSequence = new ZkSequence(path, this.client);
            this.zkSequenceMap.put(name, zkSequence);
        }
    }

    /**
     * 获取sequence
     * @param name
     * @return
     */
    public Long sequence(ZkSequenceEnum name){
        ZkSequence zkSequence = zkSequenceMap.get(name.name());
        try {
            if (zkSequence != null) {
                return zkSequence.getSequence();
            }
        } catch (Exception e) {
            //e.printStackTrace();
            logger.error("获取[{}]sequence失败",name);
            logger.error("错误信息[{}]", e);
        }
        return null;
    }


}
