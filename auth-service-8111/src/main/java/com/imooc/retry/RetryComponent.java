package com.imooc.retry;


import com.imooc.exceptions.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class RetryComponent {

    @Retryable(
            value = IllegalArgumentException.class,
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000, multiplier = 2) // 重试间隔1s，后续重试次数为2 1/2/4
    )
    public boolean  sendSMSWithRetry() {
      log.info("当前时间 Time={}", LocalDateTime.now());
      return SMSUtilsRetry.sendSMS();
    }

    /**
     *  达到最大重试异常，就会执行这一个
     * @param e
     * @return
     */

    @Recover
    public boolean recover(RuntimeException e) {
        GraceException.display(ResponseStatusEnum.SYSTEM_SMS_FALLBACK_ERROR);
        return false;
    }
}
