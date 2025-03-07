package com.imooc.reflection.aop;

import org.springframework.stereotype.Component;

@Component
public class LogExecutionTimeAopApplication {

    @LogExecutionTime
    public int add(int init,int times) throws InterruptedException {
        int sum = 0;
        Thread.sleep(500);
        for(int i=0;i<times;i++){
            sum = sum+init;
        }
        return sum;
    }
}
