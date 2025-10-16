package com.bitejiuyeke.biteadminapi.user.feign;

import com.bitejiuyeke.biteadminapi.user.domain.dto.LoginByPhoneReqDTO;
import com.bitejiuyeke.biteadminapi.user.domain.vo.AppUserVO;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.vo.TokenVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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
     * 根据手机号注册用户
     *
     * @param phoneNumber 手机号
     * @return C端用户VO
     */
    @GetMapping("/register/phone/{phoneNumber}")
    R<AppUserVO> registerByPhone(@PathVariable("phoneNumber") String phoneNumber);

    /**
     * 根据手机号码进行登录（验证码传参为空则发送新的验证码）
     *
     * @return token
     */
    @GetMapping("/login/phone")
    R<TokenVO> loginByPhone(@RequestBody @Validated LoginByPhoneReqDTO reqDTO);

}
