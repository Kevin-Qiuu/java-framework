package com.bitejiuyeke.biteportalservice.service.impl;

import com.bitejiuyeke.biteadminapi.user.domain.dto.AppUserDTO;
import com.bitejiuyeke.biteadminapi.user.domain.dto.EditUserReqDTO;
import com.bitejiuyeke.biteadminapi.user.domain.vo.AppUserVO;
import com.bitejiuyeke.biteadminapi.user.feign.AppUserFeignClient;
import com.bitejiuyeke.bitecommoncore.utils.BeanCopyUtil;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import com.bitejiuyeke.bitecommondomain.domain.dto.LoginUserDTO;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import com.bitejiuyeke.bitecommondomain.domain.dto.TokenDTO;
import com.bitejiuyeke.bitecommondomain.domain.vo.TokenVO;
import com.bitejiuyeke.bitecommonsecurity.service.TokenService;
import com.bitejiuyeke.bitenotifyapi.captcha.domain.dto.LoginByPhoneReqDTO;
import com.bitejiuyeke.bitenotifyapi.captcha.feign.CaptchaFeignClient;
import com.bitejiuyeke.biteportalservice.domain.dto.LoginUserInfoDTO;
import com.bitejiuyeke.biteportalservice.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    /**
     * appUser 服务 （feign）
     */
    @Autowired
    private AppUserFeignClient appUserFeignClient;

    /**
     * token 组件
     */
    @Autowired
    private TokenService tokenService;

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
        R<TokenVO> tokenVOResult = null;
        try {
            tokenVOResult = appUserFeignClient.loginByPhone(reqDTO);
        } catch (Exception e) {
            log.error("调用 appUserFeignClient 失败：", e);
        }
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

    /**
     * 获取登录用户信息
     *
     * @return LoginUserInfoDTO
     */
    @Override
    public LoginUserInfoDTO getLoginUserInfo() {
        LoginUserDTO loginUserDTO = tokenService.getLoginUser();
        if (loginUserDTO == null) {
            throw new ServiceException(ResultCode.LOGIN_STATUS_OVERTIME);
        }
        R<AppUserVO> findRet = appUserFeignClient.findById(Long.valueOf(loginUserDTO.getUserId()));
        if (findRet == null) {
            throw new ServiceException("用户信息查询失败！", ResultCode.ERROR.getCode());
        }
        LoginUserInfoDTO loginUserInfoDTO = new LoginUserInfoDTO();
        BeanCopyUtil.copyProperties(loginUserDTO, loginUserInfoDTO);
        BeanCopyUtil.copyProperties(findRet.getData(), loginUserInfoDTO);
        return loginUserInfoDTO;
    }

    /**
     * 用户登出
     */
    @Override
    public void logout() {
        tokenService.defLoginUser();
    }
}
