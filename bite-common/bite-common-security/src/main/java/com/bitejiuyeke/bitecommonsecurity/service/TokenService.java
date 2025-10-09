package com.bitejiuyeke.bitecommonsecurity.service;

import com.bitejiuyeke.bitecommoncore.utils.ServletUtil;
import com.bitejiuyeke.bitecommondomain.constants.CacheConstants;
import com.bitejiuyeke.bitecommondomain.constants.LoginUserConstants;
import com.bitejiuyeke.bitecommondomain.constants.TokenConstants;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import com.bitejiuyeke.bitecommonredis.service.RedisService;
import com.bitejiuyeke.bitecommonsecurity.domain.dto.LoginUserDTO;
import com.bitejiuyeke.bitecommonsecurity.domain.dto.TokenDTO;
import com.bitejiuyeke.bitecommonsecurity.utils.JwtUtil;
import com.bitejiuyeke.bitecommonsecurity.utils.SecurityUtil;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * token 的创建服务类
 * 用于 为登录端提供创建 Token 服务
 * Token 存放到 Redis 中
 * 超管设置用户登录状态
 * 1、创建 token
 * 2、根据 token 获取用户信息
 * 3、为用户创建 Token 信息，并将用户信息存入 Redis 缓存以校验用户登录状态
 * Redis 中的用户存储格式为 ACCESS_KEY_UUID:LoginUserDTO
 * 4、验证 token 有效期，不足规定有效期会及时进行刷新
 * 5、获取 token key 信息
 */

@Slf4j
@Component
public class TokenService {

    /**
     * Redis 中用户信息存放 key
     */
    public final String LOGIN_TOKEN_KEY = TokenConstants.LOGIN_TOKEN_KEY;

    /**
     * 用户缓存信息刷新时间
     */
    public final Long refreshTime = CacheConstants.REFRESH_TIME;

    /**
     * 用户缓存信息过期时间
     */
    public final Long EXPIRE_TIME = CacheConstants.EXPIRATION;

    /**
     * Redis 服务端
     */
    @Autowired
    private RedisService redisService;

    /**
     * 创建用户登录信息，同时为用户分配 UUID，需要在 LoginUSerDTO（方法参数）接受
     *
     * @param loginUserDTO 用户登录 dto
     * @return tokenDTO
     */
    public TokenDTO createToken(LoginUserDTO loginUserDTO) {
        // 1、为用户分配 UUID
        String userLoginId = UUID.randomUUID().toString();
        loginUserDTO.setUserKey(userLoginId);

        // 2、将用户信息存入 Redis 缓存
        long currentTimeStamp = System.currentTimeMillis();
        long expireTimeStamp = currentTimeStamp + EXPIRE_TIME * 60 * 1000; // expireTime 单位是分钟
        loginUserDTO.setLoginTime(currentTimeStamp);
        loginUserDTO.setExpireTime(EXPIRE_TIME);
        loginUserDTO.setExpireTimeStamp(expireTimeStamp);
        // 先设置 loginUserDTO 再设置 Redis 缓存的信息，是保证 Redis 缓存的过期时间一定比 LoginUserDTO 的时间靠后一些
        refreshLoginExpiration(loginUserDTO);

        // 3、制作 Token
        Map<String, Object> claims = loginUserDTO.convert2TokenClaims();
        String token = JwtUtil.createToken(claims);

        // 4、返回 TokenDTO
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setAccessToken(token);
        tokenDTO.setExpireTime(EXPIRE_TIME);
        tokenDTO.setExpireTimeStamp(expireTimeStamp);
        return tokenDTO;
    }

    /**
     * 根据令牌获取用户信息
     *
     * @param token 令牌
     * @return LoginUserDTO
     */
    public LoginUserDTO getLoginUser(String token) {
        LoginUserDTO loginUserDTO = null;
        try {
            if (StringUtils.isNotEmpty(token) && !isTokenExpired(token)) {
                String redisLoginUserKey = getRedisLoginUserKey(JwtUtil.getUserKey(token));
                loginUserDTO = redisService.getCacheObject(redisLoginUserKey, LoginUserDTO.class);
            }
        } catch (Exception e) {
            log.warn("未获取到用户信息，用户 token: {}", token);
            return null;
        }
        return loginUserDTO;
    }

    // 根据请求对象内容直接获取用户信息

    /**
     * 根据请求体获取用户信息
     *
     * @param request Http 请求体
     * @return LoginUserDTO
     */
    public LoginUserDTO getLoginUser(HttpServletRequest request) {
        String token = SecurityUtil.getToken(request);
        return getLoginUser(token);
    }

    /**
     * 直接获取用户信息（在当前连接的线程 ThreadLocal 中获取当前请求体）
     *
     * @return LoginUserDTO
     */
    public LoginUserDTO getLoginUser() {
        HttpServletRequest request = ServletUtil.getRequest();
        return getLoginUser(request);
    }

    /**
     * 根据用户 Token 删除当前用户的登录态
     *
     * @param token 令牌
     */
    public void delLoginUser(String token) {
        // 判断用户 Token 是否已过期
        if (StringUtils.isNotEmpty(token) && !isTokenExpired(token)) {
            // 删除 Redis 中的用户信息
            redisService.deleteObject(getRedisLoginUserKey(JwtUtil.getUserKey(token)));
        }
    }

    /**
     * 为超管提供删除用户的登录状态
     * @param userId 用户 id
     * @param userFrom 用户 来源
     */
    public void delLoginUser(String userId, String userFrom) {
        if (StringUtils.isEmpty(userId)) return;
        Collection<String> redisUserKeys = redisService.keys(LOGIN_TOKEN_KEY + "*");
        for (String redisUserKey : redisUserKeys) {
            LoginUserDTO loginUserDTO = redisService.getCacheObject(redisUserKey, LoginUserDTO.class);
            if (loginUserDTO != null && loginUserDTO.getUserId().equals(userId) && loginUserDTO.getUserFrom().equals(userFrom)) {
                redisService.deleteObject(redisUserKey);
                return;
            }
        }

    }


    /**
     * 根据用户 token 判断用户是否已经登录过期
     *
     * @param token 令牌
     * @return 登录过期返回 true，否则返回 false
     */
    public boolean isTokenExpired(String token) {
        return System.currentTimeMillis() > JwtUtil.getUserExpireTimeStamp(token);
    }

    /**
     * 根据 UUID 获取用户在 Redis 中存储值的键
     *
     * @param userKey UUID
     * @return Redis 中的 key
     */
    public String getRedisLoginUserKey(String userKey) {
        return LOGIN_TOKEN_KEY + userKey;
    }

    /**
     * 刷新用户登录信息，重新设置 Redis 中的过期时间
     *
     * @param loginUserDTO 用户登录 DTO
     */
    public void refreshLoginExpiration(LoginUserDTO loginUserDTO) {
        if (StringUtils.isNotEmpty(loginUserDTO.getUserKey())) {
            throw new ServiceException("用户未分配登录 ID");
        }
        String loginUserRedisKey = LOGIN_TOKEN_KEY + loginUserDTO.getUserKey();
        redisService.setCacheObject(loginUserRedisKey, loginUserDTO, EXPIRE_TIME, TimeUnit.MINUTES);
    }


}
