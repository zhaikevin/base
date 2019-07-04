package com.kevin.base.zookeeper.lock;

import com.kevin.base.common.utils.ThreadUtils;
import com.kevin.base.zookeeper.utils.ZkUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Description: 基于zookeeper的可以自动释放的分布式锁测试类
 * @Author: kevin
 * @Date: 2019/7/2 14:49
 */
public class AutoReleaseDistLockTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoReleaseDistLockTest.class);

    @Before
    public void prepare() throws Exception {

        List<String> children = ZkUtils.getClient().getChildren().forPath("/");
        for (String child : children) {
            ZkUtils.getClient().delete().deletingChildrenIfNeeded().forPath("/" + child);
        }
    }

    @Test
    public void testLock() throws InterruptedException {

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try (AutoReleaseDistLock lock = new AutoReleaseDistLock("dummy")) {
                    boolean flag = lock.lock();
                    LOGGER.info("thread {} ,lock flag:{}", Thread.currentThread().getName(), flag);

                    ThreadUtils.sleepQuietly(5 * 1000);
                } catch (Exception e) {
                    LOGGER.error("some exception", e);
                }
            }).start();
        }

        Thread.currentThread().join(60 * 1000);
    }

}
