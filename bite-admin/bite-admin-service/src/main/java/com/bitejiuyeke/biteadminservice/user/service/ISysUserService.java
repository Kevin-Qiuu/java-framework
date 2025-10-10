package com.bitejiuyeke.biteadminservice.user.service;

import com.bitejiuyeke.biteadminservice.user.domain.dto.LoginPasswordDTO;
import com.bitejiuyeke.bitecommonsecurity.domain.dto.TokenDTO;

public interface ISysUserService {

    /**
     * 通过用户密码进行登录
     *
     * @param loginPasswordDTO 登录体
     * @return tokenDTO
     */
    TokenDTO loginByPassword(LoginPasswordDTO loginPasswordDTO);

}
