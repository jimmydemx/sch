package com.imooc.aspects.redisCache;


import com.imooc.utils.GsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


@Aspect
@Component
@Slf4j
public class RedisCacheAOP {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final ExpressionParser parser = new SpelExpressionParser();
    private final ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    @Around("@annotation(redisCache) && execution(* com.imooc.service.impl..*.*(..))")
    public Object cacheInRedis(ProceedingJoinPoint proceedingJoinPoint, RedisCache redisCache) throws Throwable {
        String action = redisCache.action();
        String key = getKeywithParamter(proceedingJoinPoint, redisCache.key());
        String value = stringRedisTemplate.opsForValue().get(key);

        if ("search".equalsIgnoreCase(action)) {
            if (value != null) {
                Object result = GsonUtils.stringToBean(value);
                log.info("GET VALUE FROM REDIS : {}", result);
                return result;

            }
            Object result = proceedingJoinPoint.proceed();
            stringRedisTemplate.opsForValue().set(key, GsonUtils.object2String(result));
            log.info("GET VALUE FROM BD:{}", result);
        }

        if ("delete".equalsIgnoreCase(action)) {
            Object result = proceedingJoinPoint.proceed();
            stringRedisTemplate.delete(key);
            return result;
        }
        return proceedingJoinPoint.proceed();
    }


    private String getKeywithParamter(ProceedingJoinPoint joinPoint, String keyWithParams) {

        // Early return if no SpEL placeholders found(optimization)
        if (!keyWithParams.contains("#") && !keyWithParams.contains("T(")) {
            return keyWithParams;
        }


        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // if no parameter found, assign an empty array
        String[] paramNames = nameDiscoverer.getParameterNames(method);
        if (paramNames == null || paramNames.length == 0) {
            paramNames = new String[0];
        }
        Object[] args = joinPoint.getArgs();


        // 构建上下文
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < paramNames.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }

        // 加入try...catch...的原因是如果没有传入key 没有params会报错
        try {
            String parsedKey = parser.parseExpression(keyWithParams).getValue(context, String.class);
            log.info("Parsed Redis cache key: {}", parsedKey);
            return parsedKey;
        } catch (Exception e) {
            log.warn("Failed to parse Redis key expression: '{}'. Falling back to raw key. Error: {}",
                    keyWithParams, e.getMessage());
            return keyWithParams; // Fallback to original key if parsing fails
        }

    }
}
