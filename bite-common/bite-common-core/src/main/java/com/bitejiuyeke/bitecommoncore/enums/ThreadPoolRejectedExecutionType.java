package com.bitejiuyeke.bitecommoncore.enums;

import lombok.Getter;

@Getter
public enum ThreadPoolRejectedExecutionType {
    /**
     * 1. AbortPolicy（默认策略）
     * 直接抛出 RejectedExecutionException 异常，阻止系统正常运行。
     */
    AbortPolicy(1),
    /**
     * 2. CallerRunsPolicy
     * 由调用线程（提交任务的线程）执行该任务，这会减缓新任务的提交速度。
     */
    CallerRunsPolicy(2),
    /**
     * 3. DiscardPolicy
     * 直接丢弃新提交的任务，不抛出任何异常。
     */
    DiscardPolicy(3),
    /**
     * 4. DiscardOldestPolicy
     * 丢弃队列中最老的任务（即将被执行的任务），然后尝试提交新任务。
     */
    DiscardOldestPolicy(4);

    private Integer value;

    ThreadPoolRejectedExecutionType(Integer value) {this.value = value;}

    public static ThreadPoolRejectedExecutionType forValue(Integer value) {

        for (ThreadPoolRejectedExecutionType type : ThreadPoolRejectedExecutionType.values()) {
            if (type.value.equals(value))
                return type;
        }

        return null;

    }
}
