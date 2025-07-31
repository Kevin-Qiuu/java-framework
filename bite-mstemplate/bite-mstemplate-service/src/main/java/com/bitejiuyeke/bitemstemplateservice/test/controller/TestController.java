package com.bitejiuyeke.bitemstemplateservice.test.controller;

import com.bitejiuyeke.bitecommondomain.domain.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/info")
    public String info() {
        log.info("TestController: /test/info 接口调用测试");
        return "TestController: /test/info 接口调用测试";
    }

    @GetMapping("/r1")
    public R<String> r1() {
        log.info("hello mstemplate service test/r1");
        return R.ok("hello mstemplate service test/r1");
    }

    @GetMapping("/r2")
    public R<?> r2() throws Exception {
        log.info("统一异常封装测试");
        throw new Exception();
//        return R.ok(ResultCode.SUCCESS);
    }

}
