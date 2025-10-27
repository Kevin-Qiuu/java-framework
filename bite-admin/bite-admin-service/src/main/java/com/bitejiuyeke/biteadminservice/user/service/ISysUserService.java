package com.bitejiuyeke.biteadminservice.user.service;

import com.bitejiuyeke.biteadminservice.user.domain.dto.LoginPasswordDTO;
import com.bitejiuyeke.biteadminservice.user.domain.dto.SysUserDTO;
import com.bitejiuyeke.biteadminservice.user.domain.dto.SysUserSearchReqDTO;
import com.bitejiuyeke.bitecommondomain.domain.dto.TokenDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ISysUserService {

    /**
     * 通过用户密码进行登录
     *
     * @param loginPasswordDTO 登录体
     * @return tokenDTO
     */
    TokenDTO loginByPassword(LoginPasswordDTO loginPasswordDTO);

    /**
     * 添加或者编辑 b 端用户信息
     *
     * @param sysUserDTO b 端用户信息
     * @return b 端用户 id
     */
    Long addOrEdit(SysUserDTO sysUserDTO);

    /**
     * 获取用户信息列表
     *
     * @param searchReqDTO 查询请求体
     * @return List<SysUserDTO>
     */
    List<SysUserDTO> getUserList(SysUserSearchReqDTO searchReqDTO);

    /**
     * 获取用户登录信息
     * @return SysUserDTO
     */
    SysUserDTO getLoginInfo();


    /**
     * 根据表格信息上传系统用户身份
     *
     * @param excel 系统用户身份Excel文件
     */
    void uploadAppUserInfoFile(MultipartFile excel);

     /**
     * 根据表格信息上传系统用户身份
     *
     * @param excelUrl 系统用户身份Excel文件 url
     */
    void uploadAppUserInfoUrl(String excelUrl);
}
