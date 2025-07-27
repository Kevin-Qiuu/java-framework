package com.bitejiuyeke.bitemstemplateservice.domain;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ValidationUserDTO {

    @NotNull(message = "昵称内容不可为空")
    private String name;

    @NotBlank(message = "用户账号不可为空")
    private String userAccount;

    @NotEmpty(message = "用户密码不可为空")
    @Size(min = 5, max = 10, message = "用户密码需至少为六位，不能大于 10位")
    private String password;

//    @Size(max = 60, message = "用户年龄需大于 0，小于 60")  @Size 是用来管理字符串长度的
    @Min(value = 0, message = "年龄不可小于 0 岁")
    @Max(value = 60, message = "年龄不可超过 60 岁")
    private int age;

    @Pattern(regexp = "^(13[0-9]|14[01456789]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$",
             message = "手机号码格式不正确")
    private String phone;

    @Email(message = "邮箱格式不正确")
    private String email;

    @Past(message = "开始日期需为以前日期")
    private LocalDateTime startDate;

    @Future(message = "结束日期需为未来日期")
    private LocalDateTime endDate;

}
