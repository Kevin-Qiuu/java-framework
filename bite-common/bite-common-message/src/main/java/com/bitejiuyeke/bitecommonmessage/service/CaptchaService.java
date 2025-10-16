package com.bitejiuyeke.bitecommonmessage.service;

import com.bitejiuyeke.bitecommoncore.utils.VerifyUtil;
import com.bitejiuyeke.bitecommondomain.constants.MessageConstants;
import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import com.bitejiuyeke.bitecommonredis.service.RedisService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RefreshScope
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
    @Value("${sms.captcha-expiration-time:10}")
    private Integer captchaExpirationTime;

    /**
     * 验证码发送时间间隔
     */
    @Value("${sms.captcha-send-interval:2}")
    private Integer captchaSendInterval;

    /**
     * 是否发送随机验证码
     */
    @Value("${sms.send-random-captcha:false}")
    private boolean sendRandomCaptcha;
    @Autowired
    private AliSmsService aliSmsService;


    /**
     * 发送电话验证码（实现了防盗刷逻辑）
     *
     * @param phoneNumber 电话号码
     * @return 验证码
     */
    public String sendCaptchaCode(String phoneNumber) {

        // 1、校验发送次数是否超过一天限额
        List<Long> msgDailyRecordList = redisService.getCacheList(getSmsMsgTimeRedisKey(phoneNumber), new TypeReference<>() {
        });
        msgDailyRecordList = msgDailyRecordList == null ? new ArrayList<>() : msgDailyRecordList;
        if (msgDailyRecordList.size() >= msgDailyLimit) {
            throw new ServiceException(ResultCode.SEND_MSG_OVERLIMIT);
        }

        // 2、校验发送次数是否超过一分钟限额
        // 直接获取当前电话的验证码缓存，然后判断是否超过单次发送间隔
        String captchaCodeRedis = redisService.getCacheObject(getSmsCaptchaRedisKey(phoneNumber), String.class);
        if (StringUtils.isNotEmpty(captchaCodeRedis)) {
            // 获取 redis 缓存的 TTL
            long expire = redisService.getExpire(getSmsCaptchaRedisKey(phoneNumber));
            // 计算发送间隔时间
            long sendIntervalTime = captchaExpirationTime * 60 - expire;
            if (sendIntervalTime < captchaSendInterval * 60) {
                throw new ServiceException(String.format("验证码发送过于频繁，请在%d秒后重试！",
                        captchaSendInterval * 60 - sendIntervalTime),
                        ResultCode.SEND_MSG_FAILED.getCode());
            }
        }

        // 3、产生验证码
        String captchaCode = sendRandomCaptcha ? VerifyUtil.generateCaptchaCode(6) : "123456";

        // 4、缓存验证码到 Redis 中
        redisService.setCacheObject(getSmsCaptchaRedisKey(phoneNumber), captchaCode, captchaExpirationTime * 60);

        // 5、缓存发送校验信息
        aliSmsService.sendCaptchaCodeMessage(phoneNumber, captchaCode, captchaExpirationTime);

        // 6、 设置发送次数限制的缓存（使用 List 数据结构，存放的是发送验证码时间戳，便于后续溯源）
        if (msgDailyRecordList.isEmpty()) {
            msgDailyRecordList.add(System.currentTimeMillis());
            redisService.setCacheList(getSmsMsgTimeRedisKey(phoneNumber), msgDailyRecordList);
            redisService.expire(getSmsMsgTimeRedisKey(phoneNumber), 24 * 60 * 60);
        } else {
            redisService.rightPushForList(getSmsMsgTimeRedisKey(phoneNumber), System.currentTimeMillis());
        }

        return captchaCode;
    }

    /**
     * 获取已经发送的电话验证码
     * @param phoneNumber 电话号码
     * @return 验证码
     */
    public String getCaptchaCode(String phoneNumber) {
        return redisService.getCacheObject(getSmsCaptchaRedisKey(phoneNumber), String.class);
    }



    /**
     * 获取记录短信一天之内发送限额的 Redis key
     *
     * @param phoneNumber 电话号码
     * @return redis key
     */
    public String getSmsMsgTimeRedisKey(String phoneNumber) {
        return MessageConstants.SMS_MSG_TIMES_REDIS_KEY + phoneNumber;
    }

    /**
     * 获取记录的已发送验证码的 Redis key
     *
     * @param phoneNumber 电话号码
     * @return redis key
     */
    public String getSmsCaptchaRedisKey(String phoneNumber) {
        return MessageConstants.SMS_CAPTCHA_REDIS_KEY + phoneNumber;
    }

}
