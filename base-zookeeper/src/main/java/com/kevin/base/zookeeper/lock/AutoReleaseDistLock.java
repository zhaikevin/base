package com.kevin.base.zookeeper.lock;

import com.kevin.base.zookeeper.utils.ZkUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @Description: 基于zookeeper的可以自动释放的分布式锁
 * @Author: kevin
 * @Date: 2019/7/2 14:43
 */
public class AutoReleaseDistLock implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoReleaseDistLock.class);

    /**
     * 缓存时间，30秒
     */
    private static final int EXPIRE_TIME_SECONDS = 30;

    /**
     * 锁的唯一名。通常为锁定目标的code.
     */
    private String locker;

    /**
     * 锁获取标志。true为已经获取到锁
     */
    private Boolean lockFlag;

    /**
     * mutex
     */
    private InterProcessMutex mutex;

    /**
     * @param locker
     */
    public AutoReleaseDistLock(String locker) {
        super();
        this.locker = locker;
    }

    @Override
    public void close() throws Exception {
        try {
            if (lockFlag) {
                mutex.release();
            }
        } catch (Exception e) {
            LOGGER.error("release lock exception, but it can be ignored.", e);
        }
    }

    /**
     * 锁方法
     * @return 是否获得锁
     */
    public boolean lock() {
        try {
            CuratorFramework client = ZkUtils.getClient();
            String zkPath = "/lock/" + locker;

            this.mutex = new InterProcessMutex(client, zkPath);
            lockFlag = mutex.acquire(EXPIRE_TIME_SECONDS, TimeUnit.SECONDS);
            return lockFlag;
        } catch (Exception e) {
            LOGGER.error("acquire lock exception.", e);
            return false;
        }
    }
}
