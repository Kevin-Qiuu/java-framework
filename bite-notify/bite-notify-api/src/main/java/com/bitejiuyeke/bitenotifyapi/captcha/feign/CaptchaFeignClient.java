package com.bitejiuyeke.bitenotifyapi.captcha.feign;

import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitenotifyapi.captcha.domain.dto.LoginByPhoneReqDTO;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@ConditionalOnProperty(name = "feign.bite-notify.feignEnabled", havingValue = "true")
@FeignClient(contextId = "captchaFeignClient", name = "bite-notify", path = "/captcha")
public interface CaptchaFeignClient {

    /**
     * 发送手机验证码
     *
     * @param phoneNumber 手机号
     * @return 验证码
     */
    @GetMapping("/send/{phoneNumber}")
    R<String> sendCaptchaCode(@PathVariable("phoneNumber") @NotBlank(message = "电话号码为空！") String phoneNumber);

    /**
     * 校验手机验证码是否正确
     *
     * @param reqDTO 验证码校验请求体
     * @return true = 校验成功 ； false = 校验失败
     */
    @PostMapping("/verify")
    R<Boolean> verifyCaptchaCode(@RequestBody @Validated LoginByPhoneReqDTO reqDTO);

}
