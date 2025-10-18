package com.bitejiuyeke.biteadminapi.user.feign;

import com.bitejiuyeke.biteadminapi.user.domain.dto.EditUserReqDTO;
import com.bitejiuyeke.biteadminapi.user.domain.vo.AppUserVO;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.vo.TokenVO;
import com.bitejiuyeke.bitenotifyapi.captcha.domain.dto.LoginByPhoneReqDTO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@ConditionalOnProperty(name = "feign.bite-admin.feignEnabled", havingValue = "true")
@FeignClient(contextId = "appUserFeignClient", name = "bite-admin", path = "/app_user")
public interface AppUserFeignClient {

    /**
     * 根据手机号查询用户信息
     *
     * @param phoneNumber 手机号
     * @return C端用户VO
     */
    @GetMapping("/phone_find/{phoneNumber}")
    R<AppUserVO> findByPhone(@PathVariable("phoneNumber") String phoneNumber);


    /**
     * 根据手机号码进行登录（如果用户不存在自动注册，注册成功会为用户自动分配一个 token）
     *
     * @return token
     */
    @PostMapping("/login/phone")
    R<TokenVO> loginByPhone(@RequestBody @Validated LoginByPhoneReqDTO reqDTO);


    /**
     * 编辑用户信息
     * @param reqDTO 编辑用户请求 DTO
     */
    @PostMapping("/edit")
    R<Void> editUserInfo(@RequestBody EditUserReqDTO reqDTO);

}
