package com.bitejiuyeke.bitecommoncore.utils;

import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;

import java.util.Random;
import java.util.regex.Pattern;

public class VerifyUtil {

    /**
     * 电话匹配正则化表达式 <br>
     * ^ 表示匹配字符串的开始。<br>
     * 1 表示手机号码以数字 1 开头。<br>
     * [3|4|5|6|7|8|9] 表示接下来的数字是3到9之间的任意一个数字。这是中国大陆手机号码的第二位数字，通常用来区分不同的运营商。<br>
     * [0-9]{9} 表示后面跟着9个0到9之间的任意数字，这代表手机号码的剩余部分。<br>
     * $ 表示匹配字符串的结束。<br>
     */
    private final static String PHONE_NUMBER_REGEX = "^1[3|4|5|6|7|8|9][0-9]{9}$";

    private final static String SOURCE = "0123456789";

    /**
     * 密码最小长度
     */
    private final static int PASSWORD_MIN_LENGTH = 8;

    /**
     * 密码最大长度
     */
    private final static int PASSWORD_MAX_LENGTH = 16;

    /**
     * 密码正则化校验表达式（支持字母、数字和常见特殊字符）
     */
    private final static String PASSWORD_REGEX = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+$";

    /**
     * 校验电话格式是否正确
     *
     * @param phone 电话号码
     * @return true = 电话格式正确 ； false = 电话格式错误
     */
    public static boolean checkMobile(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return false;
        }
        return Pattern.matches(PHONE_NUMBER_REGEX, phone);
    }

    public static boolean checkPassword(String password) {
        if (StringUtils.isEmpty(password)
                || password.length() < PASSWORD_MIN_LENGTH || password.length() > PASSWORD_MAX_LENGTH) {
            return false;
        }
        return Pattern.matches(PASSWORD_REGEX, password);
    }

    /**
     * 生成验证码
     *
     * @param size 验证码长度
     * @return 生成的验证码
     */
    public static String generateCaptchaCode(int size) {
        if (size <= 0) {
            throw new ServiceException("验证码长度参数不是正数！", ResultCode.INVALID_PARA.getCode());
        }
        Random random = new Random(System.currentTimeMillis());
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < size; ++i) {
            stringBuilder.append(SOURCE.charAt(random.nextInt(9)));
        }
        return stringBuilder.toString();
    }

}
