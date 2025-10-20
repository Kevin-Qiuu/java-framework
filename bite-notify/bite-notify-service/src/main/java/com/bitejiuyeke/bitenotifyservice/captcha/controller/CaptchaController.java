package com.bitejiuyeke.bitenotifyservice.captcha.controller;


import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitenotifyapi.captcha.domain.dto.LoginByPhoneReqDTO;
import com.bitejiuyeke.bitenotifyapi.captcha.feign.CaptchaFeignClient;
import com.bitejiuyeke.bitenotifyservice.captcha.service.ICaptchaService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/captcha")
public class CaptchaController implements CaptchaFeignClient {

    @Autowired
    private ICaptchaService captchaService;


    /**
     * 发送手机验证码
     *
     * @param phoneNumber 手机号
     * @return 验证码
     */
    @Override
    @GetMapping("/send/{phoneNumber}")
    public R<String> sendCaptchaCode(@PathVariable("phoneNumber") @NotBlank(message = "电话号码为空！") String phoneNumber){
        return R.ok(captchaService.sendCaptchaCode(phoneNumber));
    }

    /**
     * 校验手机验证码是否正确
     *
     * @param reqDTO 验证码校验请求体
     * @return true = 校验成功 ； false = 校验失败
     */
    @Override
    @PostMapping("/verify")
    public R<Boolean> verifyCaptchaCode(@RequestBody @Validated LoginByPhoneReqDTO reqDTO) {
        return R.ok(captchaService.verifyCaptchaCode(reqDTO));
    }


}
