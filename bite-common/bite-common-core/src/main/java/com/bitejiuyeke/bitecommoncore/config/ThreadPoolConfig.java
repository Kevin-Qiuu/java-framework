package com.bitejiuyeke.bitecommoncore.config;

import com.bitejiuyeke.bitecommoncore.enums.RejectedExecutionType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

// @EnableAsync 注解会与 @Async 注解搭配使用
// 只有被 @EnableAsync 注解修饰的线程池对象才可以接受 @Async 修饰的方法任务
// 否则提交到 Spring 的默认线程池中
@EnableAsync
@Configuration
public class ThreadPoolConfig {

    /**
     * 核心线程数
     */
    @Value("${thread.pool-executor.corePoolSize:8}")
    private Integer corePoolSize;

    /**
     * 最大线程数
     */
    @Value("${thread.pool-executor.maxPoolSize:20}")
    private Integer maxPoolSize;

    /**
     * 阻塞队列容量
     */
    @Value("${thread.pool-executor.queueCapacity:30}")
    private Integer queueCapacity;

    /**
     * 拒绝策略类型
     */
    @Value("${thread.pool-executor.rejectedExecutionType:2}")
    private Integer rejectedExecutionType;

    /**
     * 空闲线程存活时间（单位：秒）
     */
    @Value("${thread.pool-executor.keepAliveSeconds:60}")
    private Integer keepAliveSeconds;

    /**
     * 线程名称前缀
     */
    @Value("${thread.pool-executor.threadNamePrefix:thread-service-}")
    private String threadNamePrefix;


    @Bean("threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setRejectedExecutionHandler(getRejectedExecutionHandler(rejectedExecutionType));
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix(threadNamePrefix);

        return executor;

    }

    private RejectedExecutionHandler getRejectedExecutionHandler(Integer rejectedExecutionType) {

        RejectedExecutionType type = RejectedExecutionType.forValue(rejectedExecutionType);

        if (null == type) {
            return new ThreadPoolExecutor.CallerRunsPolicy();
        }

        return switch (type) {
            case AbortPolicy -> new ThreadPoolExecutor.AbortPolicy();
            case CallerRunsPolicy -> new ThreadPoolExecutor.CallerRunsPolicy();
            case DiscardPolicy -> new ThreadPoolExecutor.DiscardPolicy();
            case DiscardOldestPolicy -> new ThreadPoolExecutor.DiscardOldestPolicy();
        };

    }

}
