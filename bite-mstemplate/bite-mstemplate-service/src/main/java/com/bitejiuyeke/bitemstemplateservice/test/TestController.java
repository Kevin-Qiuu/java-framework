package com.bitejiuyeke.bitemstemplateservice.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/info")
    public String info() {
        log.info("TestController: /test/info 接口调用测试");
        return "TestController: /test/info 接口调用测试";
    }

}
