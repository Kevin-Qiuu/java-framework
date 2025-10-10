package com.bitejiuyeke.biteadminservice.user.controller;

import com.bitejiuyeke.biteadminservice.user.domain.dto.LoginPasswordDTO;
import com.bitejiuyeke.biteadminservice.user.service.ISysUserService;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.vo.TokenVO;
import com.bitejiuyeke.bitecommonsecurity.domain.dto.TokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SysUserController {

    @Autowired
    private ISysUserService sysUserService;

    // todo 待测试
    @PostMapping("/login/password")
    public R<TokenVO> loginByPassword(@Validated @RequestBody LoginPasswordDTO loginPasswordDTO) {
        TokenDTO tokenDTO = sysUserService.loginByPassword(loginPasswordDTO);
        return R.ok(tokenDTO.convert2TokenVO());
    }

}
