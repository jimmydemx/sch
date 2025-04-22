package com.imooc.retry;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class SMSUtilsRetry {
    

    /**
     * 模拟发送短信
     *
     * @return
     */
    public static boolean sendSMS() {
        int num = RandomUtils.nextInt(0, 3);
        log.info("产生的 num:"+num);

        return switch (num) {
            case 0 -> throw new IllegalArgumentException("参数有误，不能为0");
            case 1 -> true;
            case 2 -> throw new ArrayIndexOutOfBoundsException("数组越界");
            default ->  false;
        };

    }
}
