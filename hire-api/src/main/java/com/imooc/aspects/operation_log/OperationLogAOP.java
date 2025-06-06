package com.imooc.aspects.operation_log;

import com.imooc.utils.GsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class OperationLogAOP {

    @Around("@annotation(operationLog) && execution(* com.imooc.service.impl..*.*(..))")
    public Object calculateOperation(ProceedingJoinPoint joinPoint, OperationLog operationLog) throws Throwable {

        long start = System.currentTimeMillis();

        OperationOutput output = new OperationOutput();
        output.setTitle(operationLog.title());
        output.setBusinessType(operationLog.businessType());

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 获取类似于com.example.service.ProductService.addProduct的带路径的方法
        String declaringTypeName = methodSignature.getDeclaringTypeName();
        output.setMethod(declaringTypeName + "." + methodSignature.getName());

        String[] parameterNames = methodSignature.getParameterNames();
        output.setParams(parameterNames);

        try {
            Object result = joinPoint.proceed();

            output.setResult(result != null ? result.toString() : "null");

            long time = System.currentTimeMillis() - start;
            output.setDuration(time + "ms");
            output.setSuccess(true);
            log.info(GsonUtils.object2String(output));

            return result;
        } catch (Exception e) {
            long time = System.currentTimeMillis() - start;
            output.setDuration(time + "ms");
            output.setSuccess(false);
            log.error(GsonUtils.object2String(output));
            throw new RuntimeException(e);
        }


    }
}
