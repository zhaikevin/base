package com.kevin.base.common.utils;

import org.apache.commons.configuration.Configuration;
import org.junit.Assert;
import org.junit.Test;

/**
 * @Description: 读取配置文件工具测试类
 * @Author: kevin
 * @Date: 2019/7/2 10:50
 */
public class CommonConfigUtilsTest {

    @Test
    public void getConfigTest() {
        Configuration configuration = CommonConfigUtils.getConfig("base.properties");
        String projectName= configuration.getString("base.project.name");
        Assert.assertEquals("base",projectName);
    }
}
