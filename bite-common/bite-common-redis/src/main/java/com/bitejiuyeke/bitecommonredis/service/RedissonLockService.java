package com.bitejiuyeke.bitecommonredis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class RedissonLockService {
    /**
     * redis操作客户端
     */
    private final RedissonClient redissonClient;

    /**
     * 获取锁
     *
     * @param lockKey        锁的key，唯一标识，建议模块名+唯一键
     * @param expire         超时时间，单位毫秒，传入-1自动续期
     * @return          创建的锁的实例对象（通过这个对象对锁进行释放，猜测内部包含了 lockKey）null 则获取锁失败
     */
    public RLock acquire(String lockKey, Long expire) {
        try {
            final RLock lockInstance = redissonClient.getLock(lockKey);
            // expire < 0 则启动过期时间续期（看门狗）
            lockInstance.lock(expire, TimeUnit.MILLISECONDS);
            return lockInstance;
        } catch (Exception e){
            return null;
        }
    }

    /**
     * 释放锁。注意：必须和获取锁在一个线程中
     *
     * @param lockInstance 锁的实例，acquire返回的
     * @return 释放成功返回true，否则返回false
     */
    public boolean releaseLock(RLock lockInstance) {
        if(!lockInstance.isHeldByCurrentThread()) {
            return false;
        }
        lockInstance.unlock();
        return true;
    }

}