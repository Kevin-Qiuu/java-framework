package com.bitejiuyeke.biteadminservice.user.service;

import com.bitejiuyeke.biteadminservice.user.domain.dto.LoginPasswordDTO;
import com.bitejiuyeke.biteadminservice.user.domain.dto.SysUserDTO;
import com.bitejiuyeke.bitecommonsecurity.domain.dto.TokenDTO;

import java.util.List;

public interface ISysUserService {

    /**
     * 通过用户密码进行登录
     *
     * @param loginPasswordDTO 登录体
     * @return tokenDTO
     */
    TokenDTO loginByPassword(LoginPasswordDTO loginPasswordDTO);

    /**
     * 添加或者编辑 b 端用户信息
     *
     * @param sysUserDTO b 端用户信息
     * @return b 端用户 id
     */
    Long addOrEdit(SysUserDTO sysUserDTO);

    /**
     * 获取用户信息列表
     *
     * @param userId 用户 id
     * @param phoneNumber 用户电话号码
     * @param status 用户状态
     * @return List<SysUserDTO>
     */
    List<SysUserDTO> getUserList(Long userId, String phoneNumber, String status);

    /**
     * 获取用户登录信息
     * @return SysUserDTO
     */
    SysUserDTO getLoginInfo();
}
