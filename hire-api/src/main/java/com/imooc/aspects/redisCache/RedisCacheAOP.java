package com.imooc.aspects.redisCache;


import com.imooc.utils.GsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;


@Aspect
@Component
@Slf4j
public class RedisCacheAOP {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Around("@annotation(redisCache) && execution(* com.imooc.service.impl..*.*(..))")
    public Object cacheInRedis(ProceedingJoinPoint proceedingJoinPoint, RedisCache redisCache) throws Throwable {
        String action = redisCache.action();
        String key = redisCache.key();
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
}
