package com.kevin.base.common.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * @Description: 本地资源工具测试类
 * @Author: kevin
 * @Date: 2019/9/11 17:27
 */
public class LocalUtilsTest {

    @Test
    public void getLocalIpTest() {
        String ip = LocalUtils.getLocalIp();
        Assert.assertEquals("172.16.65.133",ip);
    }

    @Test
    public void getLocalHostNameTest() {
        String hostName = LocalUtils.getLocalHostName();
        Assert.assertEquals("DESKTOP-S5K0TQP",hostName);
    }
}
