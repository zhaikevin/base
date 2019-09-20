package com.kevin.base.redis.config;

import com.kevin.base.common.utils.CommonConfigUtils;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.HostAndPort;

import java.util.HashSet;
import java.util.Set;

/**
 * @Description: redis 配置
 * @Author: kevin
 * @Date: 2019/9/12 15:21
 */
public class RedisConfig {

    private static final Configuration ONEDATA_CONFIG = CommonConfigUtils.getConfig("base-application.properties");

    /**
     * redis 地址
     */
    private Set<String> sentinels;

    /**
     * 密码
     */
    private String password;

    public RedisConfig() {
        String[] hostPorts = ONEDATA_CONFIG.getStringArray("base.redis.servers");
        sentinels = new HashSet<>();
        for (String hostPort : hostPorts) {
            String[] hostPortArr = StringUtils.split(hostPort, ":");
            sentinels.add(new HostAndPort(hostPortArr[0], Integer.valueOf(hostPortArr[1])).toString());
        }
        password = ONEDATA_CONFIG.getString("base.redis.password");
    }

    public Set<String> getSentinels() {
        return sentinels;
    }

    public void setSentinels(Set<String> sentinels) {
        this.sentinels = sentinels;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
