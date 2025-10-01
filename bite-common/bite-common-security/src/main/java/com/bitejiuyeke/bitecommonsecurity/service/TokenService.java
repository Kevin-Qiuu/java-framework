package com.bitejiuyeke.bitecommonsecurity.service;

import com.bitejiuyeke.bitecommondomain.constants.CacheConstants;
import com.bitejiuyeke.bitecommondomain.constants.TokenConstants;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import com.bitejiuyeke.bitecommonredis.service.RedisService;
import com.bitejiuyeke.bitecommonsecurity.domain.dto.LoginUserDTO;
import com.bitejiuyeke.bitecommonsecurity.domain.dto.TokenDTO;
import com.bitejiuyeke.bitecommonsecurity.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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

@Component
public class TokenService {

    /**
     * Redis 中用户信息存放 key
     */
    public final String loginTokenKey = TokenConstants.LOGIN_TOKEN_KEY;

    /**
     * 用户缓存信息刷新时间
     */
    public final Long refreshTime = CacheConstants.REFRESH_TIME;

    /**
     * 用户缓存信息过期时间
     */
    public final Long expireTime = CacheConstants.EXPIRATION;

    /**
     * Redis 服务端
     */
    @Autowired
    private RedisService redisService;

    /**
     * 创建用户登录信息，同时为用户分配 UUID，需要在 LoginUSerDTO（方法参数）接受
     * @param loginUserDTO 用户登录 dto
     * @return tokenDTO
     */
    public TokenDTO createToken(LoginUserDTO loginUserDTO) {
        // 1、为用户分配 UUID
        String userLoginId = UUID.randomUUID().toString();
        loginUserDTO.setUserKey(userLoginId);

        // 2、将用户信息存入 Redis 缓存
        loginUserDTO.setLoginTime(System.currentTimeMillis());
        loginUserDTO.setExpireTimeStamp(expireTime);
        refreshLoginExpiration(loginUserDTO);

        // 3、制作 Token
        Map<String, Object> claims = loginUserDTO.convert2TokenClaims();
        String token = JwtUtil.createToken(claims);

        // 4、返回 TokenDTO
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setAccessToken(token);
        tokenDTO.setExpireTime(expireTime);
        return tokenDTO;
    }

    /**
     * 刷新用户登录信息，重新设置 Redis 中的过期时间
     * @param loginUserDTO 用户登录 DTO
     */
    public void refreshLoginExpiration(LoginUserDTO loginUserDTO) {
        if (!StringUtils.hasLength(loginUserDTO.getUserKey())){
            throw new ServiceException("用户未分配登录 ID");
        }
        String loginUserRedisKey = loginTokenKey + loginUserDTO.getUserKey();
        redisService.setCacheObject(loginUserRedisKey, loginUserDTO, expireTime, TimeUnit.MINUTES);
    }

}
