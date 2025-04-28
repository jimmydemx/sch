package com.imooc.retry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
//@Async //Async
public class SMSTask {
    @Autowired
    private RetryComponent retryComponent;

    @Async
    public void asyncSendSMSTask(){
        boolean res = retryComponent.sendSMSWithRetry();
        log.info("异步任务的结果是：{}",res);
    }
}
