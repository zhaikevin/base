package com.kevin.base.zookeeper.election;

import com.google.common.base.Preconditions;
import com.kevin.base.common.exception.ZKOperationException;
import com.kevin.base.zookeeper.utils.ZkUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: 选举委员会，负责系统的选主操作
 * @Author: kevin
 * @Date: 2019/7/4 15:36
 */
public final class ElectionCommittee {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElectionCommittee.class);

    private ElectionCommittee() {
    }

    /**
     * 选主
     * @param leaderName leader name
     * @param listener listener
     * @return leader node的zk path
     */
    public static String startLeaderLatch(String leaderName, LeaderLatchListener listener) {
        Preconditions.checkArgument(StringUtils.isNotBlank(leaderName));
        String zkPath = "/leader/" + leaderName;
        try {
            CuratorFramework client = ZkUtils.getClient();
            @SuppressWarnings("resource")
            final LeaderLatch leader = new LeaderLatch(client, zkPath);
            leader.addListener(listener);
            leader.start();
            return zkPath;
        } catch (Exception e) {
            LOGGER.error("election leader exception", e);
            throw new ZKOperationException(e.getMessage());
        }
    }
}
