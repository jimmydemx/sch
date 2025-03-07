package com.imooc;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class ServiceLogAspect {

    @Around("execution(* com.imooc.service.impl..*.*(..))")
    public Object LogDataBaseExecutionTime(ProceedingJoinPoint joint) throws Throwable {
        long begin = System.currentTimeMillis();
        Object proceed = joint.proceed();
        String point = joint.getTarget().getClass().getName() + "." + joint.getSignature().getName();
        long end = System.currentTimeMillis();
        long takeTime = end - begin;
        if (takeTime > 3000) {
            log.error("执行方法：{} 执行时间太长了，耗费了{}毫秒", point, takeTime);
        } else if (takeTime > 2000) {
            log.warn("执行方法：{} 执行时间稍微有点长，耗费了{}毫秒", point, takeTime);
        } else {
            log.info("执行方法：{} 执行时间，耗费了{}毫秒", point, takeTime);
        }

        return proceed;
    }
}
