package com.kevin.base.zookeeper.election;

import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: 选主操作测试
 * @Author: kevin
 * @Date: 2019/7/4 15:44
 */
public class ElectionCommitteeTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElectionCommitteeTest.class);

    @Test
    public void testStartLeaderLatchStringLeaderLatchListener() throws InterruptedException {

        ElectionCommittee.startLeaderLatch("test-leader", new LeaderLatchListener() {

            @Override
            public void notLeader() {
                LOGGER.info("I am not leader");
            }

            @Override
            public void isLeader() {
                LOGGER.info("I am leader");
            }
        });

        Thread.currentThread().join(5000);
    }
}
