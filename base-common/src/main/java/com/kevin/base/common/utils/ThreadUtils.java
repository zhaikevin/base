package com.kevin.base.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: 线程工具类
 * @Author: kevin
 * @Date: 2019/7/2 14:55
 */
public final class ThreadUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadUtils.class);

    private ThreadUtils() {
    }

    /**
     * 让当前线程安静的sleep指定的毫秒数，
     * 不会有异常抛出 。
     * @param millis 毫秒数
     */
    public static void sleepQuietly(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            LOGGER.error("thread sleep exception.", e);
        }
    }
}
