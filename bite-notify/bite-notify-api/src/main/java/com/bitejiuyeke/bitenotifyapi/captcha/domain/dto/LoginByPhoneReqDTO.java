package com.bitejiuyeke.bitenotifyapi.captcha.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginByPhoneReqDTO {

    @NotBlank(message = "电话号码为空！")
    private String phoneNumber;

    @NotBlank(message = "验证码为空！")
    private String captchaCode;

    @NotBlank(message = "用户来源为空！")
    private String userFrom="sys";

}
