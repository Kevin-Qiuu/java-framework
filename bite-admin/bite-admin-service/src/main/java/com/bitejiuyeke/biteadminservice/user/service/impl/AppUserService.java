package com.bitejiuyeke.biteadminservice.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bitejiuyeke.biteadminapi.user.domain.dto.AppUserDTO;
import com.bitejiuyeke.biteadminapi.user.domain.dto.LoginByPhoneReqDTO;
import com.bitejiuyeke.biteadminservice.user.domain.entity.AppUser;
import com.bitejiuyeke.biteadminservice.user.domain.entity.Encrypt;
import com.bitejiuyeke.biteadminservice.user.mapper.AppUserMapper;
import com.bitejiuyeke.biteadminservice.user.service.IAppUserService;
import com.bitejiuyeke.bitecommoncore.utils.BeanCopyUtil;
import com.bitejiuyeke.bitecommoncore.utils.StringUtil;
import com.bitejiuyeke.bitecommoncore.utils.VerifyUtil;
import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import com.bitejiuyeke.bitecommonmessage.service.CaptchaService;
import com.bitejiuyeke.bitecommonsecurity.domain.dto.LoginUserDTO;
import com.bitejiuyeke.bitecommonsecurity.domain.dto.TokenDTO;
import com.bitejiuyeke.bitecommonsecurity.service.TokenService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AppUserService implements IAppUserService {

    @Autowired
    private AppUserMapper appUserMapper;
    @Autowired
    private TokenService tokenService;

    @Value("${app-user.info.default-avatar}")
    private String defaultAvatar;
    @Autowired
    private CaptchaService captchaService;

    @Override
    public AppUserDTO findByPhone(String phoneNumber) {
        if (!VerifyUtil.checkMobile(phoneNumber)) {
            return null;
        }
        return convertToAppUserDTO(appUserMapper.selectOne(new LambdaQueryWrapper<AppUser>()
                .select().eq(AppUser::getPhoneNumber, new Encrypt(phoneNumber))));
    }

    @Override
    public AppUserDTO registerByPhone(String phoneNumber) {
        if (!VerifyUtil.checkMobile(phoneNumber)) {
            throw new ServiceException(ResultCode.ERROR_PHONE_FORMAT);
        }

        if (appUserMapper.exists(new LambdaQueryWrapper<AppUser>().eq(AppUser::getPhoneNumber, new Encrypt(phoneNumber)))) {
            throw new ServiceException("此电话号码已被占用！", ResultCode.INVALID_PARA.getCode());
        }

        AppUser appUser = new AppUser();
        appUser.setNickName("Bite-User-" + StringUtil.generateRandomStr(10));
        appUser.setAvatar(defaultAvatar);
        appUser.setPhoneNumber(new Encrypt(phoneNumber));
        appUserMapper.insert(appUser);
        AppUserDTO appUserDTO = convertToAppUserDTO(appUser);

        appUserDTO.setTokenDTO(createTokenForAppUser(appUser, "sys"));

        return appUserDTO;

    }

    @Override
    public TokenDTO loginByPhone(LoginByPhoneReqDTO reqDTO) {

        if (!VerifyUtil.checkMobile(reqDTO.getPhoneNumber())) {
            throw new ServiceException(ResultCode.ERROR_PHONE_FORMAT);
        }

        AppUser appUser = appUserMapper.selectOne(new LambdaQueryWrapper<AppUser>().select().eq(AppUser::getPhoneNumber, new Encrypt(reqDTO.getPhoneNumber())));
        if (appUser == null) {
            throw new ServiceException("当前手机号尚未注册！", ResultCode.INVALID_PARA.getCode());
        }

        if (StringUtils.isEmpty(reqDTO.getCaptchaCode())) {
            captchaService.sendCaptchaCode(reqDTO.getPhoneNumber());
            return null;
        }

        String captchaCode = captchaService.getCaptchaCode(reqDTO.getPhoneNumber());
        if (StringUtils.isEmpty(captchaCode)) {
            throw new ServiceException("验证码已过期！", ResultCode.INVALID_CODE.getCode());
        }

        if (!reqDTO.getCaptchaCode().equals(captchaCode)) {
            throw new ServiceException(ResultCode.INVALID_CODE);
        }

        return createTokenForAppUser(appUser, reqDTO.getUserFrom());

    }

    public TokenDTO createTokenForAppUser(AppUser appUser, String userFrom) {
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        loginUserDTO.setUserId(String.valueOf(appUser.getId()));
        loginUserDTO.setUsername(appUser.getNickName());
        loginUserDTO.setUserFrom(userFrom);
        return tokenService.createToken(loginUserDTO);
    }

    /**
     * 转换 C 端用户信息类型
     *
     * @param appUser C 端用户信息 mapper
     * @return C 端用户信息 DTO
     */
    public AppUserDTO convertToAppUserDTO(AppUser appUser) {
        if (appUser == null) return null;
        AppUserDTO appUserDTO = new AppUserDTO();
        BeanCopyUtil.copyProperties(appUser, appUserDTO);
        appUserDTO.setPhoneNumber(appUser.getPhoneNumber().getValue());
        return appUserDTO;
    }


}
