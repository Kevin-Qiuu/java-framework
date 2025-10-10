package com.bitejiuyeke.biteadminservice.user.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginPasswordDTO {

    /**
     * 用户手机号码
     */
    @NotBlank(message = "用户手机号码不可为空！")
    private String phone;

    /**
     * 用户登录密码
     */
    @NotBlank(message = "用户登录密码不可为空！")
    private String password;

    /**
     * 登录来源
     */
    private String loginFrom = "sys";

}
