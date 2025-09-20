package com.bitejiuyeke.biteadminservice.map.service.impl;

import com.bitejiuyeke.biteadminservice.map.constants.MapConstants;
import com.bitejiuyeke.biteadminservice.map.domain.entity.SysRegion;
import com.bitejiuyeke.biteadminservice.map.mapper.RegionMapper;
import com.bitejiuyeke.biteadminservice.map.service.IMapService;
import com.bitejiuyeke.biteadminservice.map.domain.dto.RegionDTO;
import com.bitejiuyeke.bitecommoncache.utils.CacheUtil;
import com.bitejiuyeke.bitecommoncore.utils.BeanCopyUtil;
import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import com.bitejiuyeke.bitecommonredis.service.RedisService;
import com.bitejiuyeke.bitecommonredis.service.RedissonLockService;
import com.bitejiuyeke.bitecommonsecurity.exception.ServiceException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.benmanes.caffeine.cache.Cache;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
@Service
public class MapService implements IMapService {

    @Autowired
    private RegionMapper regionMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private Cache<String, Object> caffeineCache;
    @Autowired
    private RedissonLockService redissonLockService;

    @PostConstruct
    public void initService() {
        try {
            loadCityList();
            loadHotCityList();
        } catch (Exception e) {
            log.error("Failed to initService data: ", e);
        }
    }

    @Override
    public List<RegionDTO> getCityList() {
        return loadCityList();
    }

    @Override
    public List<RegionDTO> getHotCityList() {
        return loadHotCityList();
    }

    private List<RegionDTO> loadHotCityList() {
        List<Integer> hotCityKeyList = List.of(1, 9, 236, 234, 289, 122);
        return loadRegionInfo(MapConstants.CACHE_MAP_HOT_CITY_KEY,
                MapConstants.CACHE_MAP_HOT_CITY_REDISSON_LOCK_KRY, () -> loadHotCityFromMapper(hotCityKeyList));
    }

    private List<RegionDTO> loadHotCityFromMapper(List<Integer> hotCityKeyList) {
        List<SysRegion> sysRegions = regionMapper.selectRegionByIds(hotCityKeyList);
        return BeanCopyUtil.copyListProperties(sysRegions, RegionDTO::new);
    }

    private List<RegionDTO> loadCityList() {
        return loadRegionInfo(MapConstants.CACHE_MAP_CITY_KEY,
                MapConstants.CACHE_MAP_CITY_REDISSON_LOCK_KEY, this::loadCityListFromMapper);
    }

    private List<RegionDTO> loadCityListFromMapper() {
        List<SysRegion> sysRegionCityList = regionMapper.selectAllRegion().stream()
                .filter(sysRegion -> sysRegion.getLevel().equals(MapConstants.CITY_LEVEL))
                .toList();
        return BeanCopyUtil.copyListProperties(sysRegionCityList, RegionDTO::new);
    }

    /**
     * 通过多级缓存和分布式锁机制来防止缓存击穿问题。
     *
     * @param cacheKey      缓存中存放的 key
     * @param redissonLockKey  分布式锁中存放的 key
     * @param supplier 函数式接口，传递从数据库加载数据的相关操作
     * @return 列表
     */
    private List<RegionDTO> loadRegionInfo(String cacheKey, String redissonLockKey, Supplier<List<RegionDTO>> supplier) {
        // 先查看二级缓存，这里不使用互斥锁，保证并发
        List<RegionDTO> infoList = CacheUtil.getL2Cache(cacheKey, new TypeReference<>() {
        }, redisService, caffeineCache);
        if (infoList != null) {
            return infoList;
        }
        // 二级缓存都不存在，为了避免出现缓存击穿，使用分布式互斥锁
        RLock acquire = redissonLockService.acquire(redissonLockKey, 30L);
        try {
            // 先检查二级缓存，保证第一个获取锁的线程在写入二级缓存后，其他的线程可以直接使用缓存
            // 进一步阻止到达数据库的流量，因此再次检查缓存，避免重复加载数据库
            infoList = CacheUtil.getL2Cache(cacheKey, new TypeReference<>() {
            }, redisService, caffeineCache);
            if (infoList != null) {
                return infoList;
            }
            // 查询数据库
            infoList = supplier.get();
            // 写入二级缓存
            CacheUtil.setL2Cache(cacheKey, infoList, redisService, caffeineCache, 120L, TimeUnit.MINUTES);
        } catch (Exception e) {
            throw new ServiceException(ResultCode.ERROR);
        } finally {
            // 保证分布式互斥锁一定会被释放
            redissonLockService.releaseLock(acquire);
        }
        return infoList;
    }


}
