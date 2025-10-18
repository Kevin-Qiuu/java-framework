package com.bitejiuyeke.bitecommondomain.domain.vo;

import com.bitejiuyeke.bitecommondomain.domain.dto.TokenDTO;
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

    /**
     * token 过期时间戳（单位：毫秒）
     */
    private Long expireTimeStamp;

    /**
     * 类型转换成 TokenDTO
     */
    public TokenDTO conver2TokenDTO() {
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setAccessToken(this.accessToken);
        tokenDTO.setExpireTime(this.expireTime);
        tokenDTO.setExpireTimeStamp(this.expireTimeStamp);
        return tokenDTO;
    }

}
