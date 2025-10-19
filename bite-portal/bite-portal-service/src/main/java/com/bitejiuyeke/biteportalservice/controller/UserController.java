package com.bitejiuyeke.biteportalservice.controller;

import com.bitejiuyeke.biteadminapi.user.domain.dto.EditUserReqDTO;
import com.bitejiuyeke.biteadminapi.user.domain.vo.AppUserVO;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.vo.TokenVO;
import com.bitejiuyeke.bitenotifyapi.captcha.domain.dto.LoginByPhoneReqDTO;
import com.bitejiuyeke.biteportalservice.domain.dto.LoginUserInfoDTO;
import com.bitejiuyeke.biteportalservice.domain.vo.LoginUserInfoVO;
import com.bitejiuyeke.biteportalservice.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private IUserService userService;

   /**
     * 根据手机号查询用户信息
     *
     * @param phoneNumber 手机号
     * @return C端用户VO
     */
    @GetMapping("/phone_find/{phoneNumber}")
    public R<AppUserVO> findByPhone(@PathVariable("phoneNumber") String phoneNumber) {
        return R.ok(userService.findByPhone(phoneNumber).convertToVO());
    }

    /**
     * 通过电话验证码登录（如果用户不存在则自动注册并创建 token）
     *
     * @param reqDTO 登录请求体
     * @return token
     */
    @PostMapping("/login/phone")
    public R<TokenVO> loginByPhone(@RequestBody @Validated LoginByPhoneReqDTO reqDTO) {
        return R.ok(userService.loginByPhone(reqDTO).convert2TokenVO());
    }

    /**
     * 编辑用户信息
     *
     * @param reqDTO 编辑请求体
     */
    @PostMapping("/user/edit")
    public R<Void> editUserInfo(@RequestBody @Validated EditUserReqDTO reqDTO) {
        userService.editUserInfo(reqDTO);
        return R.ok();
    }

    /**
     * 获取登录用户的相关信息
     * @return LoginUserInfoVO
     */
    @GetMapping("/user/find")
    public R<LoginUserInfoVO> getLoginUserInfo() {
        return R.ok(userService.getLoginUserInfo().convertToVO());
    }


}
