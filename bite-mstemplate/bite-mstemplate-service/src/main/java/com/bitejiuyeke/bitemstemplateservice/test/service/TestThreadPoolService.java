package com.bitejiuyeke.bitemstemplateservice.test.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestThreadPoolService {

    @Async("threadPoolTaskExecutor")
    public void info() {
        log.info("Service thread name :{}", Thread.currentThread().getName());
    }

}
