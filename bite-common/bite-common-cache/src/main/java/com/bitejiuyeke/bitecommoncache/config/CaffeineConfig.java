package com.bitejiuyeke.bitecommoncache.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CaffeineConfig {

    /**
     * 初始容量
     */
    @Value("${caffeine.initial-capacity:100}")
    private Integer initialCapacity;

    /**
     * 最大容量
     */
    @Value("${caffeine.maximum-capacity:300}")
    private Long maximumCapacity;

    /**
     * 过期时间，单位：秒
     */
    @Value("${caffeine.expire:60}")
    private Long expire;

    @Bean
    public Cache<String, Object> caffeineCache() {
        return Caffeine
                .newBuilder()
                .initialCapacity(initialCapacity)
                .maximumSize(maximumCapacity)
                .expireAfterWrite(expire, TimeUnit.SECONDS)
                .build();
    }


}
