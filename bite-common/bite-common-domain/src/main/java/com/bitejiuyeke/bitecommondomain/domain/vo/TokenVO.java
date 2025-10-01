package com.bitejiuyeke.bitecommondomain.domain.vo;

import lombok.Data;

@Data
public class TokenVO {

    /**
     * token
     */
    private String accessToken;

    /**
     * token 过期时间
     */
    private Long expireTime;

}
