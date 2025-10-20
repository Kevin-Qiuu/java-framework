package com.bitejiuyeke.biteadminapi.user.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EditUserReqDTO {

    /**
     * 用户 id
     */
    @NotBlank(message = "用户 id 不可为空！")
    private String userId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String avatar;

}
