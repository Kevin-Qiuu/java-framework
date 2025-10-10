package com.bitejiuyeke.biteadminservice.user.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bitejiuyeke.biteadminservice.user.domain.dto.LoginPasswordDTO;
import com.bitejiuyeke.biteadminservice.user.domain.entity.SysUser;
import com.bitejiuyeke.biteadminservice.user.mapper.SysUserMapper;
import com.bitejiuyeke.biteadminservice.user.service.ISysUserService;
import com.bitejiuyeke.bitecommoncore.utils.AESUtil;
import com.bitejiuyeke.bitecommoncore.utils.VerifyUtil;
import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import com.bitejiuyeke.bitecommonsecurity.domain.dto.LoginUserDTO;
import com.bitejiuyeke.bitecommonsecurity.domain.dto.TokenDTO;
import com.bitejiuyeke.bitecommonsecurity.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements ISysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private TokenService tokenService;

    @Override
    public TokenDTO loginByPassword(LoginPasswordDTO loginPasswordDTO) {

        // 1. 判断电话格式是否正确
        if (!VerifyUtil.checkMobile(loginPasswordDTO.getPhone())) {
            throw new ServiceException(ResultCode.ERROR_PHONE_FORMAT);
        }

        // 2. 判断电话是否存在
        SysUser sysUser = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getPhoneNumber, loginPasswordDTO.getPhone()));
        if (sysUser == null) {
            throw new ServiceException("当前手机号不存在！", ResultCode.INVALID_PARA.getCode());
        }

        // 3. 判断密码是否正确
        // 先解密
        String password = AESUtil.decryptHex(loginPasswordDTO.getPassword());
        if (StringUtils.isEmpty(password)) {
            throw new ServiceException("密码解析为空！", ResultCode.INVALID_PARA.getCode());
        }
        // 再加密（这一步其实是计算数据摘要）
        String passwordEncrypt = DigestUtil.sha256Hex(password);
        if (!passwordEncrypt.equals(sysUser.getPassword())) {
            throw new ServiceException("当前密码不正确！", ResultCode.INVALID_PARA.getCode());
        }

        // 4. 创建用户登录信息并返回 token
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        loginUserDTO.setUserId(String.valueOf(sysUser.getId()));
        loginUserDTO.setUsername(sysUser.getNickName());
        loginUserDTO.setUserFrom(loginPasswordDTO.getLoginFrom());
        return tokenService.createToken(loginUserDTO);
    }
}
