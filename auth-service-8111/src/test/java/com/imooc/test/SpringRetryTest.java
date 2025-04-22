package com.imooc.test;



import com.imooc.retry.RetryComponent;
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
    private RetryComponent retryComponent;

    @Test
    public void retry(){
        boolean res= retryComponent.sendSMSWithRetry();
        log.info("最终的结果是 res:"+res);

    }


}
