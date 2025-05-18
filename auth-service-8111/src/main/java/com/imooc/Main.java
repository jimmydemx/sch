package com.imooc;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableRetry
@SpringBootApplication
@MapperScan("com.imooc.mapper")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.imooc.feign")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class,args);
    }
}

