package com.kevin.base.zookeeper.utils;

import com.kevin.base.common.utils.CommonConfigUtils;
import org.apache.commons.configuration.Configuration;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: zk工具类
 * @Author: kevin
 * @Date: 2019/7/2 14:15
 */
public final class ZkUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZkUtils.class);

    private static final Configuration ONEDATA_CONFIG = CommonConfigUtils.getConfig("base-application.properties");

    private static CuratorFramework client;

    private ZkUtils() {
    }

    static {
        init();
    }

    private static void init() {
        String namespace = ONEDATA_CONFIG.getString("base.zk.namespace") + "/"
                + ONEDATA_CONFIG.getString("base.project.name");
        client = CuratorFrameworkFactory.builder().connectString(ONEDATA_CONFIG.getString("base.zk.servers"))
                .sessionTimeoutMs(30000).connectionTimeoutMs(30000).canBeReadOnly(false)
                .retryPolicy(new ExponentialBackoffRetry(2000, Integer.MAX_VALUE)).namespace(namespace)
                .defaultData(null).build();
        client.start();
    }

    /**
     * 获取zk连接客户端
     * @return zk连接客户端
     */
    public static CuratorFramework getClient() {
        return client;
    }
}
