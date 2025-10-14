package com.bitejiuyeke.bitecommonmessage.service;

import com.bitejiuyeke.bitecommondomain.constants.MessageConstants;
import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import com.bitejiuyeke.bitecommonredis.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CaptchaService {

    /**
     * 短信发送服务
     */
    @Autowired
    private AliSmsService smsService;

    /**
     * redis 服务
     */
    @Autowired
    private RedisService redisService;

    /**
     * 单个手机号短信发送单日限额
     */
    @Value("${sms.daily-limit:3}")
    private Integer msgDailyLimit;

    /**
     * 验证码的有效时间
     */
    @Value("${sms.captcha-effective-time:10}")
    private Integer captchaEffectiveTime;

    /**
     * 验证码发送时间间隔
     */
    @Value("${sms.captcha-send-interval:2}")
    private Integer captchaSendInterval;

    private final Long MILL_SECOND = 60 * 1000L;

    /**
     * 发送电话验证码（实现了防盗刷逻辑）
     *
     * @param phoneNumber 电话号码
     * @return 验证码
     */
    public String sendCaptchaCode(String phoneNumber) {

        // 1、校验发送次数是否超过一天限额
        Integer msgDailyTime = redisService.getCacheObject(MessageConstants.SMS_CAPTCHA_REDIS_KEY + phoneNumber, int.class);
        msgDailyTime = msgDailyTime == null ? 0 : msgDailyTime;
        if (msgDailyTime >= msgDailyLimit) {
            throw new ServiceException(ResultCode.SEND_MSG_OVERLIMIT);
        }

        // 2、校验发送次数是否超过一分钟限额
        // 直接获取当前电话的验证码缓存，然后判断是否超过单次发送间隔
        String captchaCode = redisService.getCacheObject(MessageConstants.SMS_CAPTCHA_REDIS_KEY + phoneNumber, String.class);
        if (StringUtils.isNotEmpty(captchaCode)) {
            long expire = redisService.getExpire(MessageConstants.SMS_CAPTCHA_REDIS_KEY + phoneNumber);
            if ((captchaEffectiveTime * MILL_SECOND - expire) < captchaSendInterval * MILL_SECOND) {
                // todo 完善几秒后重试的这个功能
                throw new ServiceException("验证码发送过于频繁，请在 。。 秒后重试！", ResultCode.SEND_MSG_FAILED.getCode());
            }
        }

        // 3、产生验证码并发送

        // 4、缓存到 Redis 中

        // 5、缓存发送校验信息

        return null;
    }

}
