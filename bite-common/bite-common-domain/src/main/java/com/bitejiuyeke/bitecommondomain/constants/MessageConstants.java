package com.bitejiuyeke.bitecommondomain.constants;

public class MessageConstants {

    /**
     * 短信发送成功请求状态码
     */
    public static String SMS_MSG_OK = "OK";

    /**
     * 短信发送次数统计键
     */
    public static String SMS_MSG_TIMES_REDIS_KEY = "sms:times:";

    /**
     * 验证码发送缓存键
     */
    public static String SMS_CAPTCHA_REDIS_KEY = "sms:captcha:";

    /**
     * 登录/注册模板 <br>
     * 短信内容：您的验证码为${code}。尊敬的客户，以上验证码${min}分钟内有效，请注意保密，切勿告知他人。
     */
    public static String LOGIN_OR_REGISTER_TEMPLATE_CODE = "100001";

    /**
     * 修改绑定手机号模板 <br>
     * 短信内容：尊敬的客户，您正在进行修改手机号操作，您的验证码为${code}。以上验证码${min}分钟内有效，请注意保密，切勿告知他人。
     */
    public static String MODIFY_BIND_PHONE_TEMPLATE_CODE = "100002";

    /**
     * 重置密码模板 <br>
     * 短信内容：尊敬的客户，您正在进行重置密码操作，您的验证码为${code}。以上验证码${min}分钟内有效，请注意保密，切勿告知他人。
     */
    public static String RESET_PASSWORD_TEMPLATE_CODE = "100003";

    /**
     * 绑定新手机号模板 <br>
     * 短信内容：尊敬的客户，您正在进行绑定手机号操作，您的验证码为${code}。以上验证码${min}分钟内有效，请注意保密，切勿告知他人。
     */
    public static String BING_NEW_PHONE_TEMPLATE_CODE = "100004";

    /**
     * 验证绑定手机号模板 <br>
     * 短信内容：尊敬的客户，您正在验证绑定手机号操作，您的验证码为${code}。以上验证码${min}分钟内有效，请注意保密，切勿告知他人。
     */
    public static String VERIFY_BING_PHONE_TEMPLATE_CODE = "100005";

}
