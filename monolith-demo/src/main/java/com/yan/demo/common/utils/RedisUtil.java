package com.yan.demo.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author: sixcolor
 * @Date: 2024-04-19 15:04
 * @Description:
 */
@Component
public class RedisUtil {
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 使用StringRedisTemplate获取下一个自增ID
     */
    public Long getNextId(String key) {
        return redisTemplate.opsForValue().increment(key);
    }
}
