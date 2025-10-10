package com.bitejiuyeke.bitecommoncore.utils;

import org.apache.commons.lang3.StringUtils;

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

}
