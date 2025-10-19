package com.bitejiuyeke.bitecommonrabbitmq.handler;

@FunctionalInterface
public interface TaskHandler<T>{

    /**
     * 任务处理
     *
     * @param payload String 类型的载荷
     * @return T
     */
    T handleTask(String payload);

}
