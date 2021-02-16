package com.bin.test;

import com.bin.behavior.BehaviorJarApplication;
import com.bin.common.zookeeper.sequence.Sequences;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/16 14:01
 */
@SpringBootTest(classes = BehaviorJarApplication.class)
@RunWith(SpringRunner.class)
public class ZkTest {

    @Autowired
    private Sequences sequences;

    @Test
    public void test1(){
        Long aLong = sequences.sequenceApReadBehavior();
        System.out.println(aLong);
    }
}
