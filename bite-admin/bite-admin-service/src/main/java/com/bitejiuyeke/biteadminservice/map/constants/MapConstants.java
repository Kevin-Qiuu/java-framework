package com.bitejiuyeke.biteadminservice.map.constants;

import java.util.List;

public class MapConstants {

    /**
     * 城市级别代码
     */
    public final static Integer CITY_LEVEL = 2;

    /**
     * 城市列表缓存 key
     */
    public final static String CACHE_MAP_CITY_KEY = "map:city:id";

    /**
     * 访问城市列表分布式锁 key
     */
    public final static String CACHE_MAP_CITY_REDISSON_LOCK_KEY = "map:city:lock:id";

    /**
     * 热门城市 region code
     */
    public final static String CACHE_MAP_CITY_HOT_REGION_CODE_CONFIG_KEY = "sys_hot_city_region_code";

    /**
     * 热门城市缓存 key
     */
    public final static String CACHE_MAP_HOT_CITY_KEY = "map:city:hot";

    /**
     * 访问热门城市列表分布式锁 key
     */
    public final static String CACHE_MAP_HOT_CITY_REDISSON_LOCK_KRY = "map:city:lock:hot";

    /**
     * 城市列表（根据拼音排序）key
     */
    public final static String CACHE_MAP_CITY_PINYIN_KEY = "map:city:pinyin";

    /**
     * 访问城市列表分布式锁 key
     */
    public final static String CACHE_MAP_CITY_PINYIN_REDISSON_LOCK_KEY = "map:city:lock:pinyin";

    /**
     * 地区子列表 key
     */
    public final static String CACHE_MAP_REGION_PARENT_KEY = "map:region:parentId:";

    /**
     * 访问地区子列表分布式锁 key
     */
    public final static String CACHE_MAP_REGION_PARENT_REDISSON_LOCK_KEY = "map:region:lock:parentId:";

    /**
     * 腾讯地图关键词输入提示 子域名
     */
    public final static String TENCENT_MAP_API_PLACE_SUGGESTION = "/ws/place/v1/suggestion";

    public final static String TENCENT_MAP_APT_GEO_CODER = "/ws/geocoder/v1";

}
