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
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 缓存切面
 *
 * @author HeXin
 * @date 2024/04/11
 */
@Aspect
@Component
@Slf4j
public class CacheAspect {
    private final RedisUtil redisUtil;

    private final ObjectMapper objectMapper;

    @Autowired
    public CacheAspect(RedisUtil redisUtil, ObjectMapper objectMapper) {
        this.redisUtil = redisUtil;
        this.objectMapper = objectMapper;
    }

    @Pointcut("@annotation(com.code.orientation.utils.cache.Cache)")
    public void pointcut(){
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) {
        Method realMethod = getRealMethod(joinPoint);
        Cache cache = AnnotationUtils.findAnnotation(realMethod, Cache.class);
        String redisKey = generateRedisKey(cache, joinPoint);
        String json = redisUtil.getString(redisKey);

        if (StringUtils.isNotBlank(json)) {
            log.info("命中缓存 <== key：{}", redisKey);
            return readValue(json, realMethod.getReturnType());
        }

        if (json != null) {
            log.info("命中空数据 <== key：{}", redisKey);
            throw new CustomException(CodeEnum.DATA_NOT_EXIST);
        }

        try {
            Object result = joinPoint.proceed();
            if (result == null) {
                throw new CustomException(CodeEnum.DATA_NOT_EXIST);
            }
            log.info("缓存数据 ==> key：{}", redisKey);
            setCache(redisKey, Objects.requireNonNull(cache), result);
            return result;
        } catch (Throwable e) {
            handleException(redisKey, e);
            throw new CustomException(CodeEnum.DATA_NOT_EXIST);
        }
    }

    private Method getRealMethod(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        Method method = ((MethodSignature) signature).getMethod();
        try {
            return joinPoint.getTarget().getClass()
                    .getDeclaredMethod(signature.getName(), method.getParameterTypes());
        } catch (NoSuchMethodException e) {
            log.error("反射错误", e);
            throw new CustomException(e);
        }
    }

    private String generateRedisKey(Cache cache, ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        StringBuilder params = new StringBuilder();
        for (Object arg : args) {
            if (arg != null) {
                try {
                    params.append(objectMapper.writeValueAsString(arg));
                } catch (JsonProcessingException e) {
                    throw new CustomException(e);
                }
            }
        }
        if (StringUtils.isNotBlank(params.toString())) {
            params = new StringBuilder(MD5.create().digestHex(params.toString()));
        }
        String key = cache.key();
        RedisConstants constants = cache.constants();
        boolean isDefault = constants == RedisConstants.DEFAULT;
        return isDefault ? key + ":" + params : constants.getKey() + key + ":" + params;
    }

    private Object readValue(String json, Class<?> returnType) {
        try {
            return objectMapper.readValue(json, returnType);
        } catch (JsonProcessingException e) {
            throw new CustomException(e);
        }
    }

    private void setCache(String redisKey, Cache cache, Object result) {
        RedisConstants constants = cache.constants();
        if (cache.constants() == RedisConstants.DEFAULT) {
            redisUtil.set(redisKey, result, cache.time(), cache.timeUnit());
        } else {
            redisUtil.set(constants, redisKey, result);
        }
    }

    private void handleException(String redisKey, Throwable e) {
        log.error("错误信息：{}",e.getMessage());
        log.info("缓存空数据 ==> key：{}", redisKey);
        redisUtil.set(redisKey, " ", 10L, TimeUnit.SECONDS);
    }

}
