package com.kevin.base.redis.utils;

import com.kevin.base.common.exception.SerializeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @Description: 序列号工具类
 * @Author: kevin
 * @Date: 2019/9/16 14:17
 */
public class SerializeUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(SerializeUtils.class);

    /**
     * 序列化操作
     * @param object
     * @param <V>
     * @return
     */
    public static <V> byte[] serialize(V object) {
        if (object == null) {
            return null;
        }
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            return baos.toByteArray();
        } catch (Exception e) {
            LOGGER.error("serialize exception", e);
            throw new SerializeException(e.getMessage());
        }
    }

    /**
     * 反序列化
     * @param bytes
     * @param <V>
     * @return
     */
    public static <V> V deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        ByteArrayInputStream bais = null;
        try {
            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (V) ois.readObject();
        } catch (Exception e) {
            LOGGER.error("deserialize exception", e);
        }
        return null;
    }
}
