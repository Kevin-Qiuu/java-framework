package com.bitejiuyeke.biteadminservice.user.controller;

import com.bitejiuyeke.biteadminapi.user.domain.dto.LoginByPhoneReqDTO;
import com.bitejiuyeke.biteadminapi.user.domain.vo.AppUserVO;
import com.bitejiuyeke.biteadminapi.user.feign.AppUserFeignClient;
import com.bitejiuyeke.biteadminservice.user.service.IAppUserService;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.vo.TokenVO;
import com.bitejiuyeke.bitecommonsecurity.domain.dto.TokenDTO;
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
     * 根据手机号注册用户
     *
     * @param phoneNumber 手机号
     * @return C端用户VO
     */
    @Override
    @GetMapping("/register/phone/{phoneNumber}")
    public R<AppUserVO> registerByPhone(@PathVariable("phoneNumber") String phoneNumber) {
        return R.ok(appUserService.registerByPhone(phoneNumber).convertToVO());
    }

    /**
     * 根据手机号码进行登录（如果验证码参数为空则会发送验证码）
     * @return token
     */
    @Override
    @GetMapping("/login/phone")
    public R<TokenVO> loginByPhone(@RequestBody @Validated LoginByPhoneReqDTO reqDTO) {
        TokenDTO tokenDTO = appUserService.loginByPhone(reqDTO);
        if (tokenDTO == null) {
            return R.ok();
        }
        return R.ok(tokenDTO.convert2TokenVO());
    }


}
