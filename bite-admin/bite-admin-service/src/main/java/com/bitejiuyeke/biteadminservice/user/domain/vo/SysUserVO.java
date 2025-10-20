package com.bitejiuyeke.biteadminservice.user.domain.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class SysUserVO implements Serializable {

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 用户昵称
     */
    @NotBlank(message = "昵称不可为空！")
    private String nickName;

    /**
     * 用户电话号码
     */
    private String phoneNumber;

    /**
     * 用户身份
     */
    private String identity;

    /**
     * 用户状态
     */
    @NotBlank(message = "用户状态不可为空！")
    private String status;

    /**
     * 用户备注
     */
    private String remark;


}
