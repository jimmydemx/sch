package com.imooc.reflection.LogExecutionTime;

public class LogExecutionApplication {

    @LogExecutionTime
    public int add(int init, int times){
        int sum = 0;
        try{
            Thread.sleep(500);
            for(int i =0;i<times;i++){
                sum = sum+ init;
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return sum;
    }
}
