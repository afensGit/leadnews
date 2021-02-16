package com.bin.common.zookeeper.sequence;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicLong;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/16 12:17
 */
public class ZkSequence {

    RetryPolicy retryPolicy = new ExponentialBackoffRetry(500, 3);

    DistributedAtomicLong distAtomicLong;

    public ZkSequence(String sequenceName, CuratorFramework client) {
        distAtomicLong = new DistributedAtomicLong(client, sequenceName, retryPolicy);
    }

    /**
     * 生成序列
     * @return
     * @throws Exception
     */
    public Long getSequence() throws Exception {
        AtomicValue<Long> increment = this.distAtomicLong.increment();
        if (increment.succeeded()){
            return increment.postValue();
        }else {
            return null;
        }
    }
}
