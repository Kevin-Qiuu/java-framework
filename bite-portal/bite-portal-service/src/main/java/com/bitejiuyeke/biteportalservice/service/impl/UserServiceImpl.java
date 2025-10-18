package com.bitejiuyeke.biteportalservice.service.impl;

import com.bitejiuyeke.biteadminapi.user.domain.dto.AppUserDTO;
import com.bitejiuyeke.biteadminapi.user.domain.dto.EditUserReqDTO;
import com.bitejiuyeke.biteadminapi.user.domain.vo.AppUserVO;
import com.bitejiuyeke.biteadminapi.user.feign.AppUserFeignClient;
import com.bitejiuyeke.bitecommoncore.utils.BeanCopyUtil;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import com.bitejiuyeke.bitecommondomain.domain.dto.TokenDTO;
import com.bitejiuyeke.bitecommondomain.domain.vo.TokenVO;
import com.bitejiuyeke.bitenotifyapi.captcha.domain.dto.LoginByPhoneReqDTO;
import com.bitejiuyeke.bitenotifyapi.captcha.feign.CaptchaFeignClient;
import com.bitejiuyeke.biteportalservice.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private AppUserFeignClient appUserFeignClient;


    /**
     * 通过手机号码查询用户信息
     *
     * @param phoneNumber 手机电话号码
     * @return 用户信息
     */
    @Override
    public AppUserDTO findByPhone(String phoneNumber) {
        R<AppUserVO> findResult = appUserFeignClient.findByPhone(phoneNumber);
        if (findResult == null) {
            throw new ServiceException(ResultCode.ERROR);
        }
        if (findResult.getCode() != ResultCode.SUCCESS.getCode()) {
            throw new ServiceException(findResult.getMsg(), findResult.getCode());
        }
        AppUserDTO appUserDTO = new AppUserDTO();
        BeanCopyUtil.copyProperties(findResult.getData(), appUserDTO);
        return appUserDTO;
    }

    /**
     * 通过电话验证码登录
     *
     * @param reqDTO 电话登录请求体
     * @return token
     */
    @Override
    public TokenDTO loginByPhone(LoginByPhoneReqDTO reqDTO) {
        R<TokenVO> tokenVOResult = appUserFeignClient.loginByPhone(reqDTO);
        if (tokenVOResult == null) {
            throw new ServiceException(ResultCode.ERROR);
        }
        if (tokenVOResult.getCode() != ResultCode.SUCCESS.getCode()) {
            throw new ServiceException(tokenVOResult.getMsg(), tokenVOResult.getCode());
        }
        return tokenVOResult.getData().conver2TokenDTO();
    }

    /**
     * 编辑用户信息
     *
     * @param reqDTO 编辑请求体
     */
    @Override
    public void editUserInfo(EditUserReqDTO reqDTO) {
        R<Void> result = appUserFeignClient.editUserInfo(reqDTO);
        if (result == null) {
            throw new ServiceException("修改用户信息失败！", ResultCode.ERROR.getCode());
        }
        if (result.getCode() != ResultCode.SUCCESS.getCode()) {
            throw new ServiceException(result.getMsg(), ResultCode.INVALID_PARA.getCode());
        }
    }
}
