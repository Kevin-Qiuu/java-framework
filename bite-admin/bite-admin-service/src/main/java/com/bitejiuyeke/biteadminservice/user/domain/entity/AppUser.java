package com.bitejiuyeke.biteadminservice.user.domain.entity;

import com.bitejiuyeke.bitecommondomain.domain.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class AppUser extends BaseDO implements Serializable {

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 手机号
     */
    private Encrypt phoneNumber;

    /**
     * 微信ID
     */
    private String openId;

    /**
     * 用户头像
     */
    private String avatar;

}
