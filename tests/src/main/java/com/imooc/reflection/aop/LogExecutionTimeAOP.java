package com.imooc.reflection.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 *  1，使用@Aspect标注
 *  2, Around来截取 annotation
 *  3， 使用join 来调用原函数.
 */

@Aspect
@Component
public class LogExecutionTimeAOP {


    @Around("execution(@com.imooc.reflection.aop.LogExecutionTime * *(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();

        long end = System.currentTimeMillis();

        System.out.println("方法"+joinPoint.getSignature().getName()+" 执行时间："+(end-start)+"ms");

        return result;

    }
}
