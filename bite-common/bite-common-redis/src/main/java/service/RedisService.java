package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

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




}
