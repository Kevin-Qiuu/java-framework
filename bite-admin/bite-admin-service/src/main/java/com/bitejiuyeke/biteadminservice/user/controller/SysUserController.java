package com.bitejiuyeke.biteadminservice.user.controller;

import com.bitejiuyeke.biteadminservice.user.domain.dto.LoginPasswordDTO;
import com.bitejiuyeke.biteadminservice.user.domain.dto.SysUserDTO;
import com.bitejiuyeke.biteadminservice.user.domain.dto.SysUserSearchReqDTO;
import com.bitejiuyeke.biteadminservice.user.domain.vo.SysUserVO;
import com.bitejiuyeke.biteadminservice.user.service.ISysUserService;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.vo.TokenVO;
import com.bitejiuyeke.bitecommondomain.domain.dto.TokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/sys_user")
public class SysUserController {

    @Autowired
    private ISysUserService sysUserService;


    @PostMapping("/login/password")
    public R<TokenVO> loginByPassword(@Validated @RequestBody LoginPasswordDTO loginPasswordDTO) {
        TokenDTO tokenDTO = sysUserService.loginByPassword(loginPasswordDTO);
        return R.ok(tokenDTO.convert2TokenVO());
    }

    /**
     * 添加或编辑用户
     *
     * @param sysUserDTO 用户信息
     * @return 用户 id
     */
    @PostMapping("/add_edit")
    public R<Long> addOrEdit(@Validated @RequestBody SysUserDTO sysUserDTO) {
        return R.ok(sysUserService.addOrEdit(sysUserDTO));
    }

    /**
     * 获取用户信息
     *
     * @param searchReqDTO 查询请求体
     * @return List<SysUserVO>
     */
    @PostMapping("/list")
    public R<List<SysUserVO>> getUserList(@RequestBody SysUserSearchReqDTO searchReqDTO) {
        List<SysUserVO> sysUserVOS = sysUserService.getUserList(searchReqDTO).stream()
                .map(SysUserDTO::convertToVO).toList();
        return R.ok(sysUserVOS);
    }

    /**
     * 获取用户登录信息
     *
     * @return SysUserVO
     */
    @GetMapping("/login_info/get")
    public R<SysUserVO> getLoginInfo() {
        return R.ok(sysUserService.getLoginInfo().convertToVO());
    }

    @PostMapping("/upload/app_user/file")
    public R<Void> uploadAppUserInfo(@RequestBody MultipartFile sysUserExcel) {
        sysUserService.uploadAppUserInfoFile(sysUserExcel);
        return R.ok();
    }

    @PostMapping("/upload/app_user/url")
    public R<Void> uploadAppUserInfo(@RequestParam String sysUserExcelUrl) {
        sysUserService.uploadAppUserInfoUrl(sysUserExcelUrl);
        return R.ok();
    }

}
