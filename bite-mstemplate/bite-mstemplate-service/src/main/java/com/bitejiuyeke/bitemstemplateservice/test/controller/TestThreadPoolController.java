package com.bitejiuyeke.bitemstemplateservice.test.controller;

import com.bitejiuyeke.bitemstemplateservice.test.service.TestThreadPoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test/thread/")
public class TestThreadPoolController {

    @Autowired
    private TestThreadPoolService threadPoolService;

    @GetMapping("/info")
    public void info() {
        log.info("Controller thread name :{}", Thread.currentThread().getName());
        threadPoolService.info();
    }


}
