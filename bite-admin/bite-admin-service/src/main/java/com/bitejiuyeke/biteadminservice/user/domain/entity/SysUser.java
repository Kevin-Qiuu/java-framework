package com.bitejiuyeke.biteadminservice.user.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bitejiuyeke.bitecommondomain.domain.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("sys_user")
@EqualsAndHashCode(callSuper = true)
public class SysUser extends BaseDO {

    /**
     * 用户名
     */
    private String nickName;

    /**
     * 电话号码
     */
    private Encrypt phoneNumber;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户身份
     */
    private String identity;

    /**
     * 用户备注
     */
    private String remark;

    /**
     * 用户状态
     */
    private String status;

}
