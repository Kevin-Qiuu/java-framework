package com.bitejiuyeke.bitecommonsecurity.utils;

import com.bitejiuyeke.bitecommondomain.constants.LoginUserConstants;
import com.bitejiuyeke.bitecommondomain.constants.TokenConstants;
import com.bitejiuyeke.bitecommonsecurity.domain.dto.LoginUserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * JWT 工具
 * 这个工具中的 token 本身不具备过期的时效性
 * 如果想对 token 进行过期校验，需要将过期的时效性检验放入数据载荷中，另外写方法在数据载荷中进行校验
 */
public class JwtUtil {

    /**
     * 令牌密钥
     */
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(TokenConstants.SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    /**
     * 从原始数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    public static String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 根据令牌获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 校验 token 是否有效
     * @param token token 内容
     * @return true = 有效 ； false = 无效
     */
    public static boolean verifyTokenValid(String token) {
        Claims claims = parseToken(token);
        return ! (claims == null
                || !claims.containsKey(LoginUserConstants.USER_ID)
                || !claims.containsKey(LoginUserConstants.USER_KEY)
                || !claims.containsKey(LoginUserConstants.USERNAME)
                || !claims.containsKey(LoginUserConstants.USER_FROM)
                || !claims.containsKey(LoginUserConstants.EXPIRE_TIME));
    }

    /**
     * 获取整个用户对象 LoginUserDTO
     *
     * @param token 令牌
     * @return LoginUserDTO
     */
    public static LoginUserDTO getLoginUserDTO(String token) {
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        loginUserDTO.setUserKey(getUserKey(token));
        loginUserDTO.setUserId(getUserId(token));
        loginUserDTO.setUsername(getUserName(token));
        loginUserDTO.setUserFrom(getUserFrom(token));
        loginUserDTO.setLoginTime(getUserLoginTime(token));
        loginUserDTO.setExpireTime(getUserExpireTime(token));
        return loginUserDTO;
    }

    /**
     * 获取用户 key
     *
     * @param token 令牌
     * @return 用户 key
     */
    public static String getUserKey(String token) {
        Claims claims = parseToken(token);
        return getValue(claims, LoginUserConstants.USER_KEY);
    }

    /**
     * 获取用户 key
     *
     * @param claims 数据载荷
     * @return 用户 key
     */
    public static String getUserKey(Claims claims) {
        return getValue(claims, LoginUserConstants.USER_KEY);
    }

    /**
     * 根据令牌获取用户ID
     *
     * @param token 令牌
     * @return 用户ID
     */
    public static String getUserId(String token) {
        Claims claims = parseToken(token);
        return getValue(claims, LoginUserConstants.USER_ID);
    }

    /**
     * 根据数据声明获取用户ID
     *
     * @param claims 数据声明
     * @return 用户ID
     */
    public static String getUserId(Claims claims) {
        return getValue(claims, LoginUserConstants.USER_ID);
    }

    /**
     * 根据令牌获取用户名称
     *
     * @param token 令牌
     * @return 用户名称
     */
    public static String getUserName(String token) {
        Claims claims = parseToken(token);
        return getValue(claims, LoginUserConstants.USERNAME);
    }

    /**
     * 根据数据声明获取用户名称
     *
     * @param claims 数据声明
     * @return 用户名称
     */
    public static String getUserName(Claims claims) {
        return getValue(claims, LoginUserConstants.USERNAME);
    }

    /**
     * 根据令牌获取用户来源
     *
     * @param token 令牌
     * @return 用户来源
     */
    public static String getUserFrom(String token) {
        Claims claims = parseToken(token);
        return getValue(claims, LoginUserConstants.USER_FROM);
    }

    /**
     * 根据数据声明获取用户来源
     *
     * @param claims 数据声明
     * @return 用户来源
     */
    public static String getUserFrom(Claims claims) {
        return getValue(claims, LoginUserConstants.USER_FROM);
    }

    /**
     * 根据令牌获取用户登录时间戳（单位：毫秒）
     *
     * @param token 令牌
     * @return 用户登录时间戳
     */
    public static Long getUserLoginTime(String token) {
        Claims claims = parseToken(token);
        return Long.parseLong(getValue(claims, LoginUserConstants.LOGIN_TIME));
    }

    /**
     * 根据令牌获取用户过期时间戳（单位：毫秒）
     *
     * @param token 令牌
     * @return 用户过期时间戳
     */
    public static Long getUserExpireTime(String token) {
        Claims claims = parseToken(token);
        return Long.parseLong(getValue(claims, LoginUserConstants.EXPIRE_TIME));
    }

    /**
     * 根据数据声明获取用户过期时间戳（单位：毫秒）
     *
     * @param claims 数据生命
     * @return 用户过期时间戳
     */
    public static Long getUserExpireTime(Claims claims) {
        return Long.parseLong(getValue(claims, LoginUserConstants.EXPIRE_TIME));
    }

    /**
     * 根据 key 获取 claim（数据载荷）的值
     *
     * @param claims    数据载荷
     * @param claimsKey 键
     * @return 值
     */
    public static String getValue(Claims claims, String claimsKey) {
        Object value = claims.get(claimsKey);
        return value != null ? value.toString() : "";
    }

}
