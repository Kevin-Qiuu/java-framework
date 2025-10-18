package com.bitejiuyeke.biteadminapi.user.domain.dto;

import com.bitejiuyeke.biteadminapi.user.domain.vo.AppUserVO;
import com.bitejiuyeke.bitecommoncore.utils.BeanCopyUtil;
import lombok.Data;

@Data
public class AppUserDTO {

    /**
     * C端用户ID
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 微信ID
     */
    private String openId;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 转换用户信息类型
     * @return C 端用户信息 VO
     */
    public AppUserVO convertToVO() {
        AppUserVO appUserVO = new AppUserVO();
        BeanCopyUtil.copyProperties(this, appUserVO);
        return appUserVO;
    }

}
