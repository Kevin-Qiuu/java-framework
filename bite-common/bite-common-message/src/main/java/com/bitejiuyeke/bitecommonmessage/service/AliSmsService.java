package com.bitejiuyeke.bitecommonmessage.service;

import com.aliyun.dypnsapi20170525.Client;
import com.aliyun.dypnsapi20170525.models.SendSmsVerifyCodeRequest;
import com.aliyun.dypnsapi20170525.models.SendSmsVerifyCodeResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teautil.models.RuntimeOptions;
import com.bitejiuyeke.bitecommoncore.utils.JsonUtil;
import com.bitejiuyeke.bitecommondomain.constants.MessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class AliSmsService {

    /**
     * 通信体
     */
    @Autowired
    @Qualifier("smsAliyunClient")
    private Client client;

    /**
     * 短信签名
     */
    @Value("${sms.aliyun.signName}")
    private String signName;

    /**
     * 发送验证码消息
     *
     * @param phoneNumber 电话号码
     * @param captchaCode 验证码
     * @param expireTime 有效时间（单位：分钟）
     * @return true = 发送成功；false = 发送失败
     */
    public boolean sendCaptchaCodeMessage(String phoneNumber, String captchaCode, Integer expireTime) {
        Map<String, String> templateParam = new HashMap<>();
        templateParam.put("code", captchaCode);
        templateParam.put("min", String.valueOf(expireTime));
        return sendTemplateMessage(phoneNumber, MessageConstants.LOGIN_OR_REGISTER_TEMPLATE_CODE, templateParam);
    }

    /**
     * 发送模板消息
     *
     * @param phoneNumber 电话号码
     * @param templateCode 模板 code
     * @param templateParam 模板内容
     * @return true = 发送成功；false = 发送失败
     */
    public boolean sendTemplateMessage(String phoneNumber, String templateCode, Map<String, String> templateParam) {
        // 创建验证码请求体
        SendSmsVerifyCodeRequest sendSmsVerifyCodeRequest = new SendSmsVerifyCodeRequest();
        sendSmsVerifyCodeRequest
                .setSignName(signName)
                .setTemplateCode(templateCode)
                .setTemplateParam(JsonUtil.obj2String(templateParam))
                .setPhoneNumber(phoneNumber)
                .setInterval(0L);
        RuntimeOptions runtime = new RuntimeOptions();

        try {
            // 复制代码运行请自行打印 API 的返回值
            SendSmsVerifyCodeResponse sendResponse = client.sendSmsVerifyCodeWithOptions(sendSmsVerifyCodeRequest, runtime);
            if (sendResponse.getBody().getCode().equals(MessageConstants.SMS_MSG_OK)) {
                return true;
            } else {
                log.error("短信: {} 阿里云返回状态体失败， 失败原因: {}", phoneNumber, sendResponse.getBody().getMessage());
                return false;
            }
        } catch (Exception e) {
            TeaException error = new TeaException(e.getMessage(), e);
            // 错误 message
            log.error("短信: {} 本地主机端发送失败， 失败原因 {}", phoneNumber, e.getMessage());
            return false;
        }

    }


}
