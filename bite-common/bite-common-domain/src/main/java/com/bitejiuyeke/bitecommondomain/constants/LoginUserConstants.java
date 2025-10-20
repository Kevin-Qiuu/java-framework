package com.bitejiuyeke.bitecommondomain.constants;

/**
 * 定义在 Redis 中存放的数据键
 */
public class LoginUserConstants {

    /**
     * 用户当前登录标识（UUID）
     */
    public static final String USER_KEY = "user_key";

    /**
     * 用户 id（数据库）
     */
    public static final String USER_ID = "user_id";

    /**
     * 用户来源
     */
    public static final String USER_FROM = "user_from";

    /**
     * 用户名
     */
    public static final String USERNAME = "user_name";

    /**
     * 用户登录时间戳（单位：毫秒）
     */
    public static final String LOGIN_TIME = "login_time";

    /**
     * 用户登录过期时间戳（单位：毫秒）
     */
    public static final String EXPIRE_TIME = "expire_time";

    /**
     * 用户登录会话 redis 键前缀
     */
    public static final String USER_SESSIONS = "userSessions:";

}
