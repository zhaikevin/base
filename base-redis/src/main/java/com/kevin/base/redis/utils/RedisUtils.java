package com.kevin.base.redis.utils;

import com.kevin.base.redis.config.RedisConfig;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

/**
 * @Description: redis工具类
 * @Author: kevin
 * @Date: 2019/9/12 16:47
 */
public class RedisUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisUtils.class);

    private static volatile JedisSentinelPool jedisPool;

    private static Object lock = new Object();

    private RedisUtils() {

    }

    public static JedisSentinelPool getJedisPool() {
        if (jedisPool == null) {
            synchronized (lock) {
                if (jedisPool == null) {
                    RedisConfig config = new RedisConfig();
                    jedisPool = new JedisSentinelPool("mymaster", config.getSentinels(), config.getPassword());
                }
            }
        }
        return jedisPool;
    }

    /**
     * 获取key的value值
     * @param key
     * @return
     */
    public static String get(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        try (Jedis jedis = getJedisPool().getResource()) {
            return jedis.get(key);
        }
    }

    /**
     * 获取key的value值，是反序列化之后的Java对象
     * @param key
     * @param <V>
     * @return
     */
    public static <V> V getObject(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        try (Jedis jedis = getJedisPool().getResource()) {
            byte[] bytes = jedis.get(key.getBytes());
            if (bytes == null) {
                LOGGER.warn("the value of key:{} is empty.", key);
                return null;
            }
            return (V) SerializeUtils.deserialize(bytes);
        }
    }

    /**
     * 设置key的值
     * @param key
     * @param value
     * @return
     */
    public static String set(String key, String value) {
        if (StringUtils.isBlank(key)) {
            return null;
        }

        try (Jedis jedis = getJedisPool().getResource()) {
            return jedis.set(key, value);
        }
    }

    /**
     * 设置key的值，并设置过期时间
     * @param key
     * @param seconds
     * @param value
     * @return
     */
    public static String setex(String key, int seconds, String value) {
        if (StringUtils.isBlank(key)) {
            return null;
        }

        try (Jedis jedis = getJedisPool().getResource()) {
            return jedis.setex(key, seconds, value);
        }
    }

    /**
     * 设置key的值，值序列化后存储为byte[]
     * @param key
     * @param value
     * @param <V>
     * @return
     */
    public static <V> String setObject(String key, V value) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        try (Jedis jedis = getJedisPool().getResource()) {
            return jedis.set(key.getBytes(), SerializeUtils.serialize(value));
        }
    }

    /**
     * 设置key的值，并设置过期时间，值序列化后存储为byte[]
     * @param key
     * @param seconds
     * @param value
     * @param <V>
     * @return
     */
    public static <V> String setexObject(String key, int seconds, V value) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        try (Jedis jedis = getJedisPool().getResource()) {
            return jedis.setex(key.getBytes(), seconds, SerializeUtils.serialize(value));
        }
    }

    /**
     * 删除key
     * @param key
     * @return
     */
    public static Long del(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        try (Jedis jedis = getJedisPool().getResource()) {
            return jedis.del(key);
        }
    }

}
