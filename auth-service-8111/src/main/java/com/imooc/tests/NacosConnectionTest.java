package com.imooc.tests;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;

import java.util.Properties;

// todo: learn why it can be connected.
public class NacosConnectionTest {
    public static void main(String[] args) {
        String serverAddr = "127.0.0.1:8848";
        Properties properties = new Properties();
        properties.put("serverAddr", serverAddr);

        try {
            NamingService naming = NacosFactory.createNamingService(properties);
            System.out.println("Nacos连接状态: " + naming.getServerStatus());
            Thread.sleep(3000);  // 等待连接建立
            System.out.println("服务列表: " + naming.getServicesOfServer(0, 10));
        } catch (NacosException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}