package com.bitejiuyeke.bitecommonredis.service;

import com.bitejiuyeke.bitecommoncore.utils.JsonUtil;
import com.bitejiuyeke.bitecommoncore.utils.StringUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis 的五大数据结构：String、List、HashSet、Set、ZSet
 * Redis 所有其实都是一个键值对，key -> value
 * Redis 的数据结构都是根据 value 去划分的，换句话说所有的数据结构都是通过一个 key 来去索引的
 */

@Component
public class RedisService {

    /**
     * redis 操作模板类
     */
    @Autowired
    private RedisTemplate redisTemplate;

    //***************************** 基本操作 *****************************
    /**
     * 设置键-值对的有效时间（时间单位：秒）
     *
     * @param key       Redis 键
     * @param timeout   有效时间（时间单位：秒）
     * @return  true=设置成功；false=设置失败
     */
    public Boolean expire(final String key, final long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置键-值对的有效时间（可指定时间单位）
     *
     * @param key       Redis 键
     * @param timeout   有效时间
     * @param unit      有效时间的时间单位
     * @return  true=设置成功；false=设置失败
     */
    public Boolean expire(final String key, final long timeout, final TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获取键-值对的有效时间
     *
     * @param key   Redis 键
     * @return      有效时间
     */
    public long getExpire(final String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     *  判断 key 是否存在
     *
     * @param key   Redis 键
     * @return      true=存在；false=不存在
     */
    public Boolean hasKey(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 根据提供的键模式查找 Redis 中匹配的键
     *
     * @param pattern   要查找的键的模式
     * @return   键列表
     */
    public Collection<String> keys(final String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 重命名 key
     *
     * @param oldKey    原来的 key
     * @param newKey    新 keu
     */
    public void renameKey(String oldKey, String newKey) {
        redisTemplate.rename(oldKey, newKey);
    }

    //***************************** String 类型 *****************************
    public <T> void setCacheObject(final String key, final T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public <T> void setCacheObject(final String key, final T value, long timeOut) {
        redisTemplate.opsForValue().set(key, value, timeOut);
    }

    public <T> void setCacheObject(final String key, final T value, long timeOut, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeOut, timeUnit);
    }

    /**
     * 缓存 String 数据，如果指定键不存在则存储，如果指定键存在则跳过
     * @param key Redis 键
     * @param value 缓存的值
     * @param <T> 缓存的值的对象泛型
     * @return 是否缓存了对象，缓存成功则返回 true， 返回失败则返回 false
     */
    public <T> Boolean setCacheObjectIfAbsent(final String key, final T value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /**
     * 缓存 String 数据，如果指定键不存在则存储，如果指定键存在则跳过
     * @param key Redis 键
     * @param value 缓存的值
     * @param timeOut 有效时间
     * @param timeUnit 时间的单位
     * @param <T> 缓存的值的对象泛型
     * @return 是否缓存了对象，缓存成功则返回 true， 返回失败则返回 false
     */
    public <T> Boolean setCacheObjectIfAbsent(final String key, final T value, long timeOut, TimeUnit timeUnit) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, timeOut, timeUnit);
    }

    public <T> T getCacheObject(final String key, Class<T> tClass) {
        Object object = redisTemplate.opsForValue().get(key);
        if (null == object) {
            return null;
        }
        return JsonUtil.string2Obj(object.toString(), tClass);
    }

    public <T> T getCacheObject(final String key, TypeReference<T> tClass) {
        Object object = redisTemplate.opsForValue().get(key);
        if (null == object) {
            return null;
        }
        return JsonUtil.string2Obj(object.toString(), tClass);
    }

    //***************************** 操作 List 数据结构 *****************************

    /**
     * 缓存 List 数据
     * @param key Redis 键
     * @param dataList 待缓存的 List 数据
     * @return 添加 dataList 数据后，Redis 中的列表的长度
     * @param <T> List 的元素类型
     */
    public <T> long setCacheList(final String key, final List<T> dataList) {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return null == count ? 0 : count;
    }

    /**
     * 向指定 List 左侧插入数据（头插，入队）
     * @param key Redis 键
     * @param value 待插入的数据
     * @param <T> 待插入的数据类型
     */
    public <T> void leftPushForList(final String key, final T value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 向指定 List 右侧插入数据（尾插）
     * @param key Redis 键
     * @param value 待插入的数据
     * @param <T> 待插入的数据类型
     */
    public <T> void rightPushForList(final String key, final T value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 删除左侧第一个数据（头删、出队）
     * @param key Redis 键
     */
    public void leftPopForList(final String key) {
        redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 删除右侧第一个数据（尾删）
     * @param key Redis 键
     */
    public void rightPopForList(final String key) {
        redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 移除 List 中的第一个匹配的元素（从左往右）
     * @param key Redis 键
     * @param value 所要匹配的元素
     * @param <T> 所匹配元素的类型
     */
    public <T> void removeForList(final String key, T value) {
        redisTemplate.opsForList().remove(key, 1L, value);
    }

    /**
     * 移除 List 中的所有匹配的元素
     * @param key Redis 键
     * @param value 所要匹配的元素
     * @param <T> 所匹配元素的类型
     */
    public <T> void removeAllForList(final String key, T value) {
        redisTemplate.opsForList().remove(key, 0, value);
    }

    /**
     * 移除 List 中的所有元素
     * @param key Redis 键
     */
    public void removeForAllList(final String key) {
        redisTemplate.opsForList().trim(key, -1, 0);
    }

    /**
     * 修改 List 指定下标的数据
     * @param key Redis 键
     * @param index 下标
     * @param value 修改后的值
     * @param <T> 修改后的值的类型
     */
    public <T> void setElementAtIndex(final String key, Integer index, T value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    /**
     * 获取缓存的 List 对象（全部元素）
     * @param key Redis 键
     * @param elementClass 元素类型
     * @return 返回值
     * @param <T> 返回值类型
     */
    public <T> List<T> getCacheList(final String key, Class<T> elementClass) {
        return getCacheListByRange(key, 0, -1, elementClass);
    }

    /**
     * 获取缓存的 List 对象（支持复杂的泛型嵌套）
     * @param key Redis 键
     * @param typeReference 元素类型
     * @return 返回值
     * @param <T> 返回值类型
     */
    public <T> List<T> getCacheList(final String key, TypeReference<List<T>> typeReference) {
        return getCacheListByRange(key, 0, -1, typeReference);
    }

    /**
     * 获取 List 指定范围的元素
     * @param key Redis 键
     * @param start 起始位置
     * @param end 终点位置
     * @param elementClass 元素类型
     * @return 返回值
     * @param <T> 返回值类型
     */
    public <T> List<T> getCacheListByRange(final String key, long start, long end, Class<T> elementClass) {
        List list = redisTemplate.opsForList().range(key, start, end);
        return JsonUtil.string2List(JsonUtil.obj2String(list), elementClass);
    }

    /**
     * 获取 List 指定范围的元素（支持复杂嵌套泛型）
     * @param key Redis 键
     * @param start 起始位置
     * @param end 终点位置
     * @param typeReference 元素类型
     * @return 返回值
     * @param <T> 返回值类型
     */
    public <T> List<T> getCacheListByRange(final String key, long start, long end,
                                           TypeReference<List<T>> typeReference) {
        List list = redisTemplate.opsForList().range(key, start, end);
        return JsonUtil.string2Obj(JsonUtil.obj2String(list), typeReference);
    }

    /**
     * 获取指定 List 的长度
     * @param key Redis 键
     * @return 返回值
     */
    public long getCacheListSize(final String key) {
        Long size = redisTemplate.opsForList().size(key);
        return null == size ? 0 : size;
    }

    //***************************** 操作 Set 数据结构 *****************************

    /**
     * 向指定 set 添加元素（批量添加或者添加单个元素）
     * @param key Redis 键
     * @param members 元素
     */
    public void addMember(final String key, Object... members) {
        redisTemplate.opsForSet().add(key, members);
    }

    /**
     * 向指定 set 删除元素（批量删除或者删除单个元素）
     * @param key Redis 键
     * @param members 元素
     */
    public void delMember(final String key, Object... members) {
        redisTemplate.opsForSet().remove(key, members);
    }

    /**
     * 获取 Set 对象（支持复杂泛型嵌套）
     * @param key Redis 键
     * @param typeReference 复杂嵌套的泛型
     * @return Set 对象
     * @param <T> Set 元素类型
     */
    public <T> Set<T> getCacheSet(final String key, TypeReference<Set<T>> typeReference) {
        Set set = redisTemplate.opsForSet().members(key);
        return JsonUtil.string2Obj(JsonUtil.obj2String(set), typeReference);
    }

    //***************************** 操作 ZSet（有序 Set） 数据结构 *****************************

    /**
     * 向指定 ZSet 中添加元素
     * @param key Redis 键
     * @param value 待添加的元素
     * @param valueScore 待添加的元素分数
     */
    public void addMemberZSet(final String key, final Object value, double valueScore) {
        redisTemplate.opsForZSet().add(key, value, valueScore);
    }

    /**
     * 从 ZSet 中删除某一个元素
     * @param key Redis 键
     * @param value 待删除的元素
     */
    public void delMemberZSet(final String key, Object value) {
        redisTemplate.opsForZSet().remove(key, value);
    }

    /**
     * 根据分数范围删除 ZSet 中元素内容
     * @param key Redis 键
     * @param minScore 最小分数
     * @param maxScore 最大分数
     */
    public void removeZSetRangeByScore(final String key, double minScore, double maxScore) {
        redisTemplate.opsForZSet().removeRangeByScore(key, minScore, maxScore);
    }

    /**
     * 获取有序 ZSet 对象 (ZSet 默认升序排列)
     * @param key Redis 键
     * @param typeReference 复杂嵌套泛型
     * @return ZSet 对象
     * @param <T> ZSet 对象元素类型
     */
    public <T> Set<T> getCacheZSet(final String key, TypeReference<LinkedHashSet<T>> typeReference) {
        Set set = redisTemplate.opsForZSet().range(key, 0, -1);
        return JsonUtil.string2Obj(JsonUtil.obj2String(set), typeReference);
    }

    /**
     * 获取降序 ZSet 对象
     * @param key Redis 键
     * @param typeReference 复杂嵌套泛型
     * @return ZSet 对象
     * @param <T> ZSet 对象元素类型
     */
    public <T> Set<T> getCacheZSetDesc(final String key, TypeReference<LinkedHashSet<T>> typeReference) {
        Set set = redisTemplate.opsForZSet().reverseRange(key, 0, -1);
        return JsonUtil.string2Obj(JsonUtil.obj2String(set), typeReference);
    }

    //***************************** 操作 Hash 数据结构 *****************************
    /*
     1、缓存 Java 中的 Map 数据
     2、往 Hash 中存入单个数据
     3、删除 Hash 中的某个数据
     4、获取 Hash 中的单条数据
     5、获取 Hash 中的多条数据（需要传递一个 Key 的集合）
     */

    /**
     * 缓存 java 中的 Map 数据
     * @param key       Redis 的 key
     * @param dataMap   存入的 HashMap
     * @param <T>       存入的 HashMap 的值类型
     */
    public <T> void setCacheMap(String key, final Map<String, T> dataMap) {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    /**
     * 向 Redis 中的某个 HashMap 中添加元素
     * @param key       Redis 中的键
     * @param hashKey   HashMap 中的键
     * @param value     存入的 HashMap 的值
     * @param <T>       存入值的类型
     */
    public <T> void setCacheMapValue(String key, String hashKey, final T value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 删除某个 HashMap 中的某个值
     * @param key       Redis 中的键
     * @param hashKey   HashMap 中的键
     * @return          删除成功返回 True，失败返回 False
     */
    public boolean deleteCacheMapValue(String key, String hashKey) {
        return redisTemplate.opsForHash().delete(key, hashKey) > 0;
    }

    /**
     * 获取 HashMap 中的某一个值
     * @param key       Redis 中的键
     * @param hashKey   HashMap 中的键
     * @return          HashMap 中的值
     * @param <T>       值类型
     */
    public <T> T getCacheMapValue(String key, String hashKey) {
        HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();
        return hashOperations.get(key, hashKey);
    }

    /**
     * 获取 HashMap 中的某一个值（支持复杂类型嵌套）
     * @param key       Redis 中的键
     * @param hashKey   HashMap 中的键
     * @param valueType 复杂嵌套的类型
     * @return          HashMap 中的值
     * @param <T>       值类型
     */
    public <T> T getCacheMapValue(String key, String hashKey, TypeReference<T> valueType) {
        HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();
        return JsonUtil.string2Obj(JsonUtil.obj2String(hashOperations.get(key, hashKey)), valueType);
    }

    /**
     * 获取整个 HashMap 对象
     * @param key   Redis 中的键
     * @return      Map<String, T>
     * @param <T>   HashMap 中存放的元素类型
     */
    public <T> Map<String, T> getCacheMap(String key) {
        return (Map<String, T>) redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取整个 HashMap 对象（支持复杂嵌套）
     * @param key   Redis 中的键
     * @return      Map<String, T>
     * @param <T>   HashMap 中存放的元素类型
     */
    public <T> Map<String, T> getCacheMap(String key, TypeReference<Map<String, T>> valueType) {
        Map entries = redisTemplate.opsForHash().entries(key);
        return JsonUtil.string2Obj(JsonUtil.obj2String(entries), valueType);
    }

    public <T> List<T> getMultiCacheMapValue(String key, List<String> hashKeys) {
        return redisTemplate.opsForHash().multiGet(key, hashKeys);
    }

    public <T> List<T> getMultiCacheMapValue(String key, List<String> hashKeys, TypeReference<List<T>> valueType) {
        List list = redisTemplate.opsForHash().multiGet(key, hashKeys);
        return JsonUtil.string2Obj(JsonUtil.obj2String(list), valueType);
    }

    //******************************** LUA脚本 ***********************************
    /**
     * 删除指定值对应的 Redis 中的键值（compare and delete）
     *
     * @param key   缓存key
     * @param value value
     * @return 是否完成了比较并删除
     */
    public boolean cad(String key, String value) {
//        if (key.contains(StringUtils.SPACE) || value.contains(StringUtils.SPACE)) {
//            return false;
//        }

        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

        // 通过lua脚本原子验证令牌和删除令牌
        Long result = (Long) redisTemplate.execute(new DefaultRedisScript<>(script, Long.class),
                Collections.singletonList(key),
                value);
        return !Objects.equals(result, 0L);
    }
}
