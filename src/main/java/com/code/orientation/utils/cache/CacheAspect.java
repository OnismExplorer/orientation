package com.code.orientation.utils.cache;

import cn.hutool.crypto.digest.MD5;
import com.code.orientation.constants.CodeEnum;
import com.code.orientation.constants.RedisConstants;
import com.code.orientation.exception.CustomException;
import com.code.orientation.utils.RedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.springframework.core.annotation.AnnotationUtils;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class CacheAspect {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Pointcut("@annotation(com.code.orientation.utils.cache.Cache)")
    public void pointcut(){
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
        Method method = ((MethodSignature) signature).getMethod();
        Method realMethod;
        try {
            realMethod = joinPoint.getTarget().getClass()
                    .getDeclaredMethod(signature.getName(),method.getParameterTypes());
        } catch (NoSuchMethodException e) {
            log.error("反射错误");
            e.printStackTrace();
            throw new CustomException(e);
        }
        Class<?> returnType = method.getReturnType();
        // 获取注解
        Cache cache = AnnotationUtils.findAnnotation(realMethod, Cache.class);
        String key = cache.key();
        long time = cache.time();
        TimeUnit timeUnit = cache.timeUnit();
        RedisConstants constants = cache.constants();
        boolean isDefault = constants == RedisConstants.DEFAULT;
        // 获取参数
        Object[] args = joinPoint.getArgs();
        StringBuilder params = new StringBuilder();
        for (Object arg : args) {
            if(arg != null){
                String value = null;
                try {
                    objectMapper.writeValueAsString(arg);
                } catch (JsonProcessingException e) {
                    throw new CustomException(e);
                }
                params.append(value);
            }
        }
        if(StringUtils.isNotBlank(params.toString())){
            // 参数加密：防止出现key过长以及字符转义获取不到的情况
            params = new StringBuilder(MD5.create().digestHex(params.toString()));
        }
        String redisKey;
        if(isDefault){
            redisKey = key + ":"+ params;
        }else {
            redisKey = constants.getKey() + key + ":" + params;
        }
        String json = redisUtil.getString(redisKey);
        if(StringUtils.isNotBlank(json)){
            log.info("命中缓存 <== key：{}",redisKey);
            try {
                return objectMapper.readValue(json,returnType);
            } catch (JsonProcessingException e) {
                throw new CustomException(e);
            }
        }
        if(json != null){
            // 空数据
            log.info("命中空数据 <== key：{}",redisKey);
            throw new CustomException(CodeEnum.DATA_NOT_EXIST);
        }
        // 缓存未命中，将数据存入缓存
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
            // 防止缓存穿透
            log.info("缓存空数据 ==> key：{}",redisKey);
            redisUtil.set(redisKey," ",10L,TimeUnit.SECONDS);
            throw new CustomException(CodeEnum.DATA_NOT_EXIST);
        }
        if(result == null){
            // 缓存空数据
            redisUtil.set(redisKey," ",10L,TimeUnit.SECONDS);
            throw new CustomException(CodeEnum.DATA_NOT_EXIST);
        }
        log.info("缓存数据 ==> key：{}",redisKey);
        if(isDefault){
            redisUtil.set(redisKey,result,time,timeUnit);
        } else {
            redisUtil.set(constants,redisKey,result);
        }
        return result;
    }
}
