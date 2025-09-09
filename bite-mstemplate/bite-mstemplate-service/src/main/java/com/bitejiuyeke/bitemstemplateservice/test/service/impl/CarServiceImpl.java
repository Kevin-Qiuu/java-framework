package com.bitejiuyeke.bitemstemplateservice.test.service.impl;

import com.bitejiuyeke.bitecommoncache.utils.CacheUtil;
import com.bitejiuyeke.bitecommonredis.service.RedisService;
import com.bitejiuyeke.bitemstemplateservice.test.service.CarService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private Cache<String, Object> caffeineCache;
    @Autowired
    private RedisService redisService;

    @Override
    public Integer getCarValue(Integer carId) {
        String key = "car:" + carId;
        Integer carValue = CacheUtil.getL2Cache(key, new TypeReference<>() {}, redisService, caffeineCache);
        if (carValue != null) {
            return carValue;
        }
        // 缓存未命中，查询数据库
        carValue = getCarValueDb(carId);
        // 写入缓存中
        setCarValue(carId, carValue);
        return carValue;
    }

    @Override
    public void setCarValue(Integer carId, Integer carValue) {
        String key = "car:" + carId;
        CacheUtil.setL2Cache(key, carValue, redisService, caffeineCache, 120L, TimeUnit.SECONDS);
    }

    private Integer getCarValueDb(Integer carId) {
        return carId * 200;
    }
}
