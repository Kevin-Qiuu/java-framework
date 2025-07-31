package com.bitejiuyeke.bitemstemplateservice.test.controller;

import com.bitejiuyeke.bitecommoncore.utils.JsonUtil;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitemstemplateservice.domain.ValidationUserDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/vail")
@Validated
public class TestValidationController {

    @PostMapping("/r1")
    public R<String> r1(@RequestBody @Validated ValidationUserDTO userDTO) {
        log.info(JsonUtil.obj2StringPretty(userDTO));
        return R.ok("r1");
    }

    @PostMapping("/r2")
    public R<String> r2(@Validated ValidationUserDTO userDTO) {
        log.info(JsonUtil.obj2StringPretty(userDTO));
        return R.ok("r2");
    }

    @GetMapping("/r3/{id}/{age}")
    public R<String> r3(@PathVariable("id") @NotBlank(message = "用户 id 不可为空")
                            String id,
                        @PathVariable("age") @Max(value = 60, message = "用户年龄不可超过 60 岁")
                                @Min(value = 0, message = "用户年龄不可小于 0 岁")
                        Integer age) {
        log.info("userId: {}, userAge: {}", id, age);
        return R.ok("r3");
    }

}
