package com.imooc;

import com.imooc.reflection.LogExecutionTime.LogExecutionApplication;
import com.imooc.reflection.LogExecutionTime.LogExecutionTimeProcessor;
import com.imooc.reflection.aop.LogExecutionTimeAopApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main implements CommandLineRunner {

    @Autowired
    private LogExecutionTimeAopApplication logExecutionTimeAopApplication;
    public static void main(String[] args) {
//        Object executionTime = LogExecutionTimeProcessor.getExecutionTime(LogExecutionApplication.class, 10, 1000);
//        System.out.print("time sum-up:"+executionTime);
        SpringApplication.run(Main.class,args);



    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🚀 执行任务...");
        int add = logExecutionTimeAopApplication.add(1, 1000);
        System.out.println("🚀 结果..."+ add);
    }
}
