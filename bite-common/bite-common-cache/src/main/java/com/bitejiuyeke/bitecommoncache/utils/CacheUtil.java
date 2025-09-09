package com.bitejiuyeke.bitecommoncache.utils;

import com.bitejiuyeke.bitecommonredis.service.RedisService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class CacheUtil {

    /**
     * 获取缓存信息
     *
     * @param key           缓存信息 key
     * @param value         缓存信息 value
     * @param redisService  Redis 服务端
     * @param caffeineCache Caffeine 本地缓存服务
     * @return              缓存信息
     * @param <T>           缓存信息类型
     */
    public static <T> T getL2Cache(String key, TypeReference<T> value,
                                   RedisService redisService, Cache<String, Object> caffeineCache) {
        // 查询一级缓存
        T ret = (T) caffeineCache.getIfPresent(key);
        if (ret != null) {
            log.info("本地缓存命中: {}", key);
            return ret;
        }
        // 一级缓存命中失败，查询二级缓存
        ret = redisService.getCacheObject(key, value);
        if (ret != null) {
            // 二级缓存命中，写入一级缓存中
            log.info("Redis 缓存命中: {}", key);
            caffeineCache.put(key, ret);
            return ret;
        }

        return null;
    }

    /**
     * 设置缓存信息
     *
     * @param key           缓存信息 key
     * @param value         缓存信息 value
     * @param redisService  Redis 服务端
     * @param caffeineCache Caffeine 本地缓存服务端
     * @param timeout       Redis 信息过期时间
     * @param timeUnit      时间单位
     * @param <T>           缓存信息类型
     */
    public static <T> void setL2Cache(String key, T value,
                                      RedisService redisService, Cache<String, Object> caffeineCache,
                                      final Long timeout, final TimeUnit timeUnit) {
        caffeineCache.put(key, value);
        redisService.setCacheObject(key, value, timeout, TimeUnit.MILLISECONDS);
        log.info("更新 Redis 与 本地缓存信息: {}", key);
    }

}
