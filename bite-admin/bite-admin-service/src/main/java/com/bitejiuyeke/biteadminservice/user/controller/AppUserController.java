package com.bitejiuyeke.biteadminservice.user.controller;

import com.bitejiuyeke.biteadminapi.user.domain.dto.EditUserReqDTO;
import com.bitejiuyeke.biteadminapi.user.domain.vo.AppUserVO;
import com.bitejiuyeke.biteadminapi.user.feign.AppUserFeignClient;
import com.bitejiuyeke.biteadminservice.user.service.IAppUserService;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.vo.TokenVO;
import com.bitejiuyeke.bitecommondomain.domain.dto.TokenDTO;
import com.bitejiuyeke.bitenotifyapi.captcha.domain.dto.LoginByPhoneReqDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app_user")
public class AppUserController implements AppUserFeignClient {

    @Autowired
    private IAppUserService appUserService;

    /**
     * 根据手机号查询用户信息
     *
     * @param phoneNumber 手机号
     * @return C端用户VO
     */
    @Override
    @GetMapping("/phone_find/{phoneNumber}")
    public R<AppUserVO> findByPhone(@PathVariable("phoneNumber") String phoneNumber) {
        return R.ok(appUserService.findByPhone(phoneNumber).convertToVO());
    }

    /**
     * 根据手机号码进行登录（如果用户不存在自动注册，注册成功会为用户自动分配一个 token）
     *
     * @return token
     */
    @Override
    @PostMapping("/login/phone")
    public R<TokenVO> loginByPhone(@RequestBody @Validated LoginByPhoneReqDTO reqDTO) {
        TokenDTO tokenDTO = appUserService.loginByPhone(reqDTO);
        if (tokenDTO == null) {
            return R.ok();
        }
        return R.ok(tokenDTO.convert2TokenVO());
    }

    /**
     * 编辑用户信息
     *
     * @param reqDTO 编辑用户请求 DTO
     */
    @Override
    @PostMapping("/edit")
    public R<Void> editUserInfo(@RequestBody EditUserReqDTO reqDTO) {
        appUserService.editUserInfo(reqDTO);
        return R.ok();
    }


}
