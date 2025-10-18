package com.bitejiuyeke.biteportalservice.service;

import com.bitejiuyeke.biteadminapi.user.domain.dto.AppUserDTO;
import com.bitejiuyeke.biteadminapi.user.domain.dto.EditUserReqDTO;
import com.bitejiuyeke.bitecommondomain.domain.dto.TokenDTO;
import com.bitejiuyeke.bitenotifyapi.captcha.domain.dto.LoginByPhoneReqDTO;

public interface IUserService {

    /**
     * 编辑用户信息
     *
     * @param reqDTO 编辑请求体
     */
    void editUserInfo(EditUserReqDTO reqDTO);

    /**
     * 通过电话验证码登录
     *
     * @param reqDTO 电话登录请求体
     * @return token
     */
    TokenDTO loginByPhone(LoginByPhoneReqDTO reqDTO);

    /**
     * 通过手机号码查询用户信息
     *
     * @param phoneNumber 手机电话号码
     * @return 用户信息
     */
    AppUserDTO findByPhone(String phoneNumber);
}
