package com.bitejiuyeke.biteadminservice.user.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SysUserStatus {

    Enabled(1, "enable"),

    UnEnabled(0, "unEnable");

    /**
     * 状态代码
     */
    private final Integer status_code;

    /**
     * 状态内容
     */
    private final String status_info;

    /**
     * 判断状态是否合法
     *
     * @param status 状态
     * @return true = 合法 ； false = 非法
     */
    public static boolean isStatusValid(String status) {
        for (SysUserStatus status_i : SysUserStatus.values()) {
            if (status.equals(status_i.status_info))
                return true;
        }
        return false;
    }



}
