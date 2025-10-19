package com.bitejiuyeke.biteadminapi.user.feign;

import cn.hutool.log.Log;
import com.bitejiuyeke.biteadminapi.user.domain.dto.EditUserReqDTO;
import com.bitejiuyeke.biteadminapi.user.domain.vo.AppUserVO;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.vo.TokenVO;
import com.bitejiuyeke.bitenotifyapi.captcha.domain.dto.LoginByPhoneReqDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    /**
     * 获取用户的登录信息
     * @param userId 用户 id
     * @return appUser
     */
    @GetMapping("/findUser/{userId}")
    R<AppUserVO> findById(@PathVariable("userId") @NotNull(message = "用户 id 为空！") Long userId);

    /**
     * 获取用户登录信息（列表）
     * @param userIds 用户 id 列表
     * @return 返回列表
     */
    @PostMapping("/findUserList")
    R<List<AppUserVO>> findUserList(@RequestBody @NotEmpty(message = "用户 id 列表为空！") List<Long> userIds);


}
