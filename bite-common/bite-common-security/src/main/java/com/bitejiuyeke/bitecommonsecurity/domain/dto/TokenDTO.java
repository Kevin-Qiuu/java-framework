package com.bitejiuyeke.bitecommonsecurity.domain.dto;

import com.bitejiuyeke.bitecommoncore.utils.BeanCopyUtil;
import com.bitejiuyeke.bitecommondomain.domain.vo.TokenVO;
import lombok.Data;

@Data
public class TokenDTO {

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
     * 转换成 TokenVO
     * @return TokenVO
     */
    public TokenVO convert2TokenVO() {
        TokenVO tokenVO = new TokenVO();
        BeanCopyUtil.copyProperties(this, tokenVO);
        return tokenVO;
    }

}
