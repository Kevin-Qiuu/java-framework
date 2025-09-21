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
import java.util.Map;
import java.util.TreeMap;
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
        // 缓存预热
        try {
            loadCityList();
            loadHotCityList();
            loadCityPinyinMap();
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

    @Override
    public Map<String, List<RegionDTO>> getCityPinyinMap() {
        return loadCityPinyinMap();
    }

    @Override
    public List<RegionDTO> getRegionChildrenList(Long parentId) {
        return loadRegionChildrenList(parentId);
    }

    public List<RegionDTO> loadRegionChildrenList(Long parentId) {
        if (parentId == null) {
            throw new ServiceException(ResultCode.INVALID_PARA);
        }
        return loadRegionInfo(MapConstants.CACHE_MAP_REGION_PARENT_KEY + parentId,
                MapConstants.CACHE_MAP_REGION_PARENT_REDISSON_LOCK_KEY + parentId,
                () -> loadRegionChildrenFromMapper(parentId), new TypeReference<>() {});
    }

    private List<RegionDTO> loadRegionChildrenFromMapper(Long parentId) {
        List<SysRegion> childrenRegion = regionMapper.selectRegionByParentId(parentId);
        return BeanCopyUtil.copyListProperties(childrenRegion, RegionDTO::new);
    }


    private Map<String, List<RegionDTO>> loadCityPinyinMap() {
        return loadRegionInfo(MapConstants.CACHE_MAP_CITY_PINYIN_KEY,
                MapConstants.CACHE_MAP_CITY_PINYIN_REDISSON_LOCK_KEY, this::loadCityPinyinMapFromMapper,
                new TypeReference<>() {});
    }

    private Map<String, List<RegionDTO>> loadCityPinyinMapFromMapper() {
        final List<RegionDTO> cityList = loadCityList();
        Map<String, List<RegionDTO>> cityPinyinMap = new TreeMap<>(); // 索引速度快
        for (RegionDTO regionDTO : cityList) {
            String headLetter = regionDTO.getPinyin().toUpperCase().substring(0, 1);
            if (!cityPinyinMap.containsKey(headLetter)) {
                cityPinyinMap.put(headLetter, new ArrayList<>());
            }
            cityPinyinMap.get(headLetter).add(regionDTO);
        }
        return cityPinyinMap;
    }

    private List<RegionDTO> loadHotCityList() {
        List<Long> hotCityIdList = List.of(1L, 9L, 236L, 234L, 289L, 122L);
        return loadRegionInfo(MapConstants.CACHE_MAP_HOT_CITY_KEY,
                MapConstants.CACHE_MAP_HOT_CITY_REDISSON_LOCK_KRY,
                () -> loadHotCityFromMapper(hotCityIdList), new TypeReference<>() {
                });
    }

    private List<RegionDTO> loadHotCityFromMapper(List<Long> hotCityKeyList) {
        return loadCityList().stream().filter(regionDTO -> hotCityKeyList.contains(regionDTO.getId())).toList();
    }

    private List<RegionDTO> loadCityList() {
        return loadRegionInfo(MapConstants.CACHE_MAP_CITY_KEY,
                MapConstants.CACHE_MAP_CITY_REDISSON_LOCK_KEY,
                this::loadCityListFromMapper, new TypeReference<>() {
                });
    }

    private List<RegionDTO> loadCityListFromMapper() {
        List<SysRegion> sysRegionCityList = regionMapper.selectAllRegion().stream()
                .filter(sysRegion -> sysRegion.getLevel().equals(MapConstants.CITY_LEVEL))
                .toList();
        return BeanCopyUtil.copyListProperties(sysRegionCityList, RegionDTO::new);
    }

    /**
     * 通过多级缓存和分布式锁机制来防止缓存击穿问题。
     *  todo: 查看 TypeReference 为什么在方法中使用 TypeReference<T> 的时候会出现 null 的问题，泛型擦除
     *  todo: 再详细总结泛型擦除
     * @param cacheKey        缓存中存放的 key
     * @param redissonLockKey 分布式锁中存放的 key
     * @param supplier        函数式接口，传递从数据库加载数据的相关操作
     * @return 列表
     */
    private <T> T loadRegionInfo(String cacheKey, String redissonLockKey, Supplier<T> supplier, TypeReference<T> typeReference) {
        // 先查看二级缓存，这里不使用互斥锁，保证并发
        T infoList = CacheUtil.getL2Cache(cacheKey, typeReference, redisService, caffeineCache);
        if (infoList != null) {
            return infoList;
        }
        // 二级缓存都不存在，为了避免出现缓存击穿，使用分布式互斥锁
        RLock acquire = redissonLockService.acquire(redissonLockKey, 30L);
        try {
            // 先检查二级缓存，保证第一个获取锁的线程在写入二级缓存后，其他的线程可以直接使用缓存
            // 进一步阻止到达数据库的流量，因此再次检查缓存，避免重复加载数据库
            infoList = CacheUtil.getL2Cache(cacheKey, typeReference, redisService, caffeineCache);
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
