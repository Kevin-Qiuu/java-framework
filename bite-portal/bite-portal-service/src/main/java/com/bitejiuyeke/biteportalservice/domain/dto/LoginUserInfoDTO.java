package com.bitejiuyeke.biteportalservice.domain.dto;

import com.bitejiuyeke.bitecommoncore.utils.BeanCopyUtil;
import com.bitejiuyeke.biteportalservice.domain.vo.LoginUserInfoVO;
import lombok.Data;

import java.io.Serializable;

@Data
public class LoginUserInfoDTO implements Serializable {

    /**
     * redis 中存储用户的 key
     */
    private String userKey;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 用户名（昵称）
     */
    private String username;

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * 数据库中存储用户的 id
     */
    private String userId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * dto -> vo
     * @return LoginUserInfoVO
     */
    public LoginUserInfoVO convertToVO() {
        LoginUserInfoVO loginUserInfoVO = new LoginUserInfoVO();
        BeanCopyUtil.copyProperties(this, loginUserInfoVO);
        return loginUserInfoVO;
    }

}
