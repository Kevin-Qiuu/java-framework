package com.bitejiuyeke.biteadminservice.user.service;

import com.bitejiuyeke.biteadminapi.user.domain.dto.AppUserDTO;
import com.bitejiuyeke.biteadminapi.user.domain.dto.EditUserReqDTO;
import com.bitejiuyeke.biteadminservice.user.domain.dto.AppUserListReqDTO;
import com.bitejiuyeke.bitecommondomain.domain.dto.BasePageDTO;
import com.bitejiuyeke.bitecommondomain.domain.dto.TokenDTO;
import com.bitejiuyeke.bitenotifyapi.captcha.domain.dto.LoginByPhoneReqDTO;

import java.util.List;

public interface IAppUserService {

    /**
     * 根据手机号查询用户信息
     *
     * @param phoneNumber 手机号
     * @return C端用户DTO
     */
    AppUserDTO findByPhone(String phoneNumber);

    /**
     * 根据手机验证码进行登录（如果用户不存在自动注册，注册成功会为用户自动分配一个 token）
     *
     * @return token
     */
    TokenDTO loginByPhone(LoginByPhoneReqDTO reqDTO);

    /**
     * 编辑用户信息
     * @param reqDTO 编辑用户请求 DTO
     */
    void editUserInfo(EditUserReqDTO reqDTO);

    /**
     * 获取用户的信息
     *
     * @param userId 用户 id
     * @return appUser
     */
    AppUserDTO findById(Long userId);

    /**
     * 获取用户信息（列表）
     *
     * @param userIds 用户 id 列表
     * @return 返回列表
     */
    List<AppUserDTO> findUserList(List<Long> userIds);

    /**
     * 获取用户信息（列表&分页）
     *
     * @param appUserListReqDTO 用户信息请求体
     * @return 用户信息列表（分页）
     */
    BasePageDTO<AppUserDTO> getUserList(AppUserListReqDTO appUserListReqDTO);
}
