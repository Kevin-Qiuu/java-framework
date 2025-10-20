package com.bitejiuyeke.bitenotifyservice.captcha.service;


import com.bitejiuyeke.bitenotifyapi.captcha.domain.dto.LoginByPhoneReqDTO;

public interface ICaptchaService {

    /**
     * 发送手机验证码
     *
     * @param phoneNumber 手机号
     * @return 验证码
     */
    String sendCaptchaCode(String phoneNumber);


    /**
     * 校验手机验证码是否正确
     *
     * @param reqDTO 验证码校验请求体
     * @return true = 校验成功 ； false = 校验失败
     */
    Boolean verifyCaptchaCode(LoginByPhoneReqDTO reqDTO);

}
