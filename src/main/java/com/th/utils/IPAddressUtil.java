package com.th.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author TanHaooo
 * @date 2021/5/7 23:40
 */
@Component
public class IPAddressUtil {
    private static int port;

    public static int getPort() {
        return port;
    }

    @Value("${server.port}")
    public void setPort(int port) {
        this.port = port;
    }

    public static String getServeURL() {
        // 生成url地址，返回
        InetAddress localHost = null;
        try {
            localHost = Inet4Address.getLocalHost();
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        }
        String ip = localHost.getHostAddress();  // 返回格式为：xxx.xxx.xxx
        // localHost.getHostName() 一般是返回电脑用户名
        return "http://" + ip + ":" + port ;
    }
}
