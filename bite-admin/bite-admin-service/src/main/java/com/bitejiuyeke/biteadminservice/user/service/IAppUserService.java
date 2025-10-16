package com.bitejiuyeke.biteadminservice.user.service;

import com.bitejiuyeke.biteadminapi.user.domain.dto.AppUserDTO;
import com.bitejiuyeke.biteadminapi.user.domain.dto.LoginByPhoneReqDTO;
import com.bitejiuyeke.bitecommonsecurity.domain.dto.TokenDTO;

public interface IAppUserService {

    /**
     * 根据手机号查询用户信息
     *
     * @param phoneNumber 手机号
     * @return C端用户DTO
     */
    AppUserDTO findByPhone(String phoneNumber);

    /**
     * 根据手机号注册用户
     * @param phoneNumber 手机号
     * @return C端用户DTO
     */
    AppUserDTO registerByPhone(String phoneNumber);

    /**
     * 根据手机号码进行登录（验证码）
     * @return token
     */
    TokenDTO loginByPhone(LoginByPhoneReqDTO reqDTO);

}
