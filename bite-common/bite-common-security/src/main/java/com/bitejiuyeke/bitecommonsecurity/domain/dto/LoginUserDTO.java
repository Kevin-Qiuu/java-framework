package com.bitejiuyeke.bitecommonsecurity.domain.dto;

import com.bitejiuyeke.bitecommondomain.constants.LoginUserConstants;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class LoginUserDTO {

    /**
     * 用户 key（uuid）
     */
    private String userKey;

    /**
     * 用户 id（数据库）
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户来源
     */
    private String userFrom;

    /**
     * 登录时间（时间戳）
     */
    private Long loginTime;

    /**
     * 登录过期时间（分钟）
     */
    private Long expireTime;

    /**
     * 登录过期时间戳
     */
    private Long expireTimeStamp;

    /**
     * 将用户登录信息转换成 Token 的 claim
     * @return Map<String, Object>
     */
    public Map<String, Object> convert2TokenClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put(LoginUserConstants.USER_ID, getUserId());
        claims.put(LoginUserConstants.USER_KEY, getUserKey());
        claims.put(LoginUserConstants.USERNAME, getUsername());
        claims.put(LoginUserConstants.USER_FROM, getUserFrom());
        return claims;
    }

    /**
     * 获取用户过期时间戳
     * @param expireTime 过期时间（分钟）
     */
    public void setExpireTimeStamp(Long expireTime) {
        setExpireTime(expireTime);
        this.expireTimeStamp = expireTime * 60 * 1000 + loginTime;
    }

}
