package com.yan.demo.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(RedisUtil.class);
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 使用StringRedisTemplate获取下一个自增ID
     * Long nextId = redisTemplate.opsForValue().increment(key);
     */
    public Long getNextId(String key) {
        Long nextId = redisTemplate.boundValueOps(key).increment(1L);
        log.info("Redis获取缓存自增ID {}: {}", key, nextId);
        return nextId;
    }
}
