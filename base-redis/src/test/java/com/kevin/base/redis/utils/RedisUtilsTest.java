package com.kevin.base.redis.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * @Description: redis测试类
 * @Author: kevin
 * @Date: 2019/9/16 16:47
 */
public class RedisUtilsTest {

    @Test
    public void setTest() {
        RedisUtils.set("test","test");
    }

    @Test
    public void getTest() {
        String value = RedisUtils.get("test");
        Assert.assertEquals("test",value);
    }
}
