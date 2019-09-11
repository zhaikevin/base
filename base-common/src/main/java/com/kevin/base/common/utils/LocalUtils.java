package com.kevin.base.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * @Description: 本地资源工具类
 * @Author: kevin
 * @Date: 2019/9/11 17:23
 */
public class LocalUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalUtils.class);

    public static final String LOCAL_IP = getLocalIp();

    public static final String HOST_NAME = getLocalHostName();

    private LocalUtils() {
    }

    /**
     * 获取本机ip地址
     * 此方法为重量级的方法，不要频繁调用
     * @return 本机ip地址
     */
    public static String getLocalIp() {
        try {
            //根据网卡取本机配置的IP
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            String ip = null;
            a: while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    InetAddress ipObj = ips.nextElement();
                    if (ipObj.isSiteLocalAddress()) {
                        ip = ipObj.getHostAddress();
                        break a;
                    }
                }
            }
            return ip;
        } catch (Exception e) {
            LOGGER.error("get ip exception", e);
            return null;
        }
    }

    /**
     * 获取本地机器名
     * 此方法为重量级的方法，不要频繁调用
     * 一般耗时在百毫秒，缓存使用
     * @return 本地机器名
     */
    public static String getLocalHostName() {

        String hostName = null;
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            LOGGER.error("get hostname error", e);
        }
        return hostName;
    }
}
