package com.bitejiuyeke.biteadminservice.map.constants;

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
     * 热门城市缓存 key
     */
    public final static String CACHE_MAP_HOT_CITY_KEY = "map:city:hot";

    /**
     * 访问热门城市列表分布式锁 key
     */
    public final static String CACHE_MAP_HOT_CITY_REDISSON_LOCK_KRY = "map:city:lock:hot";


}
