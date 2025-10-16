package com.bitejiuyeke.biteadminapi.user.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginByPhoneReqDTO {

    @NotBlank(message = "电话号码为空！")
    private String phoneNumber;

    private String captchaCode;

    @NotBlank(message = "用户来源为空！")
    private String userFrom="sys";

}
