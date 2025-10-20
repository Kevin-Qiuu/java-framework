package com.bitejiuyeke.biteadminservice.user.domain.dto;

import com.bitejiuyeke.biteadminservice.user.domain.vo.SysUserVO;
import com.bitejiuyeke.bitecommoncore.utils.BeanCopyUtil;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class SysUserDTO implements Serializable {

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 用户昵称
     */
    @NotBlank(message = "昵称不可为空！")
    private String nickName;

    /**
     * 用户电话号码
     */
    private String phoneNumber;

    /**
     * 用户登录密码
     */
    private String password;

    /**
     * 用户身份
     */
    private String identity;

    /**
     * 用户状态
     */
    @NotBlank(message = "用户状态不可为空！")
    private String status;

    /**
     * 用户备注
     */
    private String remark;

    /**
     * 转换参数类型
     * @return SysUserVO
     */
    public SysUserVO convertToVO() {
        SysUserVO sysUserVO = new SysUserVO();
        BeanCopyUtil.copyProperties(this, sysUserVO);
        return sysUserVO;
    }

}
