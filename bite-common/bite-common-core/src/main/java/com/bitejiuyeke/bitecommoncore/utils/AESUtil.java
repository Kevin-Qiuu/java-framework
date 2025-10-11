package com.bitejiuyeke.bitecommoncore.utils;

import cn.hutool.crypto.SecureUtil;

import java.nio.charset.StandardCharsets;

public class AESUtil {

    /**
     * 对称加密密钥
     */
    private final static byte[] SECRET_KEY = "123456789@SWJTU@".getBytes(StandardCharsets.UTF_8);

    /**
     * 加密信息
     *
     * @param data 待加密信息
     * @return 加密后信息
     */
    public static String encryptHex(String data) {
        return SecureUtil.aes(SECRET_KEY).encryptHex(data);
    }

    /**
     * 解密信息
     *
     * @param encryptData 待解密信息
     * @return 解密后的信息
     */
    public static String decryptHex(String encryptData) {
        return SecureUtil.aes(SECRET_KEY).decryptStr(encryptData);
    }

}
