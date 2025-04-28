package com.imooc.test;



import com.imooc.retry.RetryComponent;
import com.imooc.retry.SMSTask;
import com.imooc.retry.SMSUtilsRetry;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@SpringBootTest
public class SpringRetryTest {

    @Autowired
    private SMSTask smsTask;

    @Test
    public void sendAsyncSMSTask(){
        smsTask.asyncSendSMSTask();
        log.info("sendAsyncSMSTask 调用结束");

    }


}
