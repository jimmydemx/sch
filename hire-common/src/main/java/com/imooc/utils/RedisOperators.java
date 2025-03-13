package com.imooc.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisOperators {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void setString(String key, String value, long timeout) {
        stringRedisTemplate.opsForValue().set(key,value,timeout, TimeUnit.SECONDS);
    }


    /**
     * 如果key不存在，则设置，如果存在，则不操作
     * @param key
     * @param value
     */
    public void  setnx60s(String key,String value){
        stringRedisTemplate.opsForValue().setIfAbsent(key,value,60,TimeUnit.SECONDS);
    }


    public String getValue(String key){
       return stringRedisTemplate.opsForValue().get(key);
    }



}
