package com.trainingsystem.trainingSystem.aop;


import com.alibaba.fastjson.JSONObject;
import com.trainingsystem.trainingSystem.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Aspect
public class CountOnlineNumberAOP {

    public static final String UID_PREFIX = "ONLINE_";
    public static final int EXPIRE_TIME = 60 * 30;

    @Autowired
    private RedisTemplate redisTemplate;


    @Around(value = "@annotation(com.trainingsystem.trainingSystem.annotation.CountOnlineNumber)")
    public Object around(ProceedingJoinPoint joinPoint) {

        String key = null;

        //获取userId
        //1.这里获取到所有的参数值的数组
        Object[] args = joinPoint.getArgs();
        JSONObject jsonObject = (JSONObject) args[0];
        Long userId = jsonObject.getLong("userId");
        if (userId != null) {
            key = UID_PREFIX + userId;

            //写入redis
            redisTemplate.opsForValue().set(key, userId + "");
            redisTemplate.expire(key, Duration.ofSeconds(EXPIRE_TIME));


            //执行方法
            Object proceed = null;
            try {
                proceed = joinPoint.proceed();
            } catch (Throwable e) {
                e.printStackTrace();
            }

            return proceed;
        }
        return null;
    }


}
