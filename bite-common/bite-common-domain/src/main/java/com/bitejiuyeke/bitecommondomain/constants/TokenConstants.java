package com.bitejiuyeke.bitecommondomain.constants;

public class TokenConstants {

    /**
     * JWT 的签名密钥
     */
    public final static String SECRET_KEY = "kevinqiu@swjtu@java@0123456789@cjh";

    /**
     * token 内容的前缀
     */
    public final static String PREFIX = "Bearer ";

    /**
     * Redis 中用户信息存放 key
     */
    public final static String LOGIN_TOKEN_KEY = "loginToken:";

}
