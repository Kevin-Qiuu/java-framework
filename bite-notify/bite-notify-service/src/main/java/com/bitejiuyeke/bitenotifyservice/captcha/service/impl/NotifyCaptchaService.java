package com.bitejiuyeke.bitenotifyservice.captcha.service.impl;

import com.bitejiuyeke.bitecommoncore.utils.VerifyUtil;
import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import com.bitejiuyeke.bitecommonmessage.service.CaptchaService;
import com.bitejiuyeke.bitenotifyapi.captcha.domain.dto.LoginByPhoneReqDTO;
import com.bitejiuyeke.bitenotifyservice.captcha.service.ICaptchaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifyCaptchaService implements ICaptchaService {

    @Autowired
    private CaptchaService captchaService;

    /**
     * 发送手机验证码
     *
     * @param phoneNumber 手机号
     * @return 验证码
     */
    @Override
    public String sendCaptchaCode(String phoneNumber) {
        if (!VerifyUtil.checkMobile(phoneNumber)) {
            throw new ServiceException(ResultCode.ERROR_PHONE_FORMAT);
        }

        return captchaService.sendCaptchaCode(phoneNumber);
    }

    /**
     * 校验手机验证码是否正确
     *
     * @param reqDTO 验证码校验请求体
     * @return true = 校验成功 ； false = 校验失败
     */
    @Override
    public Boolean verifyCaptchaCode(LoginByPhoneReqDTO reqDTO) {
        if (!VerifyUtil.checkMobile(reqDTO.getPhoneNumber())) {
            throw new ServiceException(ResultCode.ERROR_PHONE_FORMAT);
        }

        // 校验验证码是否正确
        String captchaCode = captchaService.getCaptchaCode(reqDTO.getPhoneNumber());
        if (StringUtils.isEmpty(captchaCode)) {
            throw new ServiceException("验证码已过期，请重新获取！", ResultCode.INVALID_CODE.getCode());
        }

        if (!reqDTO.getCaptchaCode().equals(captchaCode)) {
            throw new ServiceException(ResultCode.ERROR_CODE);
        }
        return true;
    }
}
