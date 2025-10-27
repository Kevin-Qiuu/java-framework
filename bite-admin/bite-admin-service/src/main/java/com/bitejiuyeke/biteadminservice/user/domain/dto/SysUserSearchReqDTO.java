package com.bitejiuyeke.biteadminservice.user.domain.dto;
import lombok.Data;

@Data
public class SysUserSearchReqDTO {

    /**
     * 用户 id
     */
    Long userId;

    /**
     * 用户电话号码
     */
    String phoneNumber;

    /**
     * 用户状态
     */
    String status;
}
