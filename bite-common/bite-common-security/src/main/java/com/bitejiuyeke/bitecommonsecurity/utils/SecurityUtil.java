package com.bitejiuyeke.bitecommonsecurity.utils;

import com.bitejiuyeke.bitecommondomain.constants.LoginUserConstants;
import com.bitejiuyeke.bitecommondomain.constants.SecurityConstants;
import com.bitejiuyeke.bitecommondomain.constants.TokenConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

public class SecurityUtil {

    /**
     * 从 Http 报文中获取 token 内容
     * @param request Http 请求对象
     * @return token
     */
    public static String getToken(HttpServletRequest request) {
        if (request == null) return null;
       String token = request.getHeader(SecurityConstants.AUTHORIZATION);
       return cutTokenPrefix(token);
    }

    /**
     * 裁剪 Token 的前缀（前提是前端设置了令牌的前缀）
     * @param token 令牌
     * @return token
     */
    public static String cutTokenPrefix(String token) {
        // 假设前端设置了令牌的前缀，则进行裁剪替换
        if(StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX)) {
            token = token.replaceFirst(TokenConstants.PREFIX, "");
        }
        return token;
    }

}
