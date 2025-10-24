package com.bitejiuyeke.biteadminservice.user.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bitejiuyeke.biteadminapi.config.domain.dto.DicDataDTO;
import com.bitejiuyeke.biteadminservice.config.service.ISysDictionaryService;
import com.bitejiuyeke.biteadminservice.user.constants.MqTaskTypeConstant;
import com.bitejiuyeke.biteadminservice.user.constants.UserTypeConstants;
import com.bitejiuyeke.biteadminservice.user.domain.dto.LoginPasswordDTO;
import com.bitejiuyeke.biteadminservice.user.domain.dto.SysUserDTO;
import com.bitejiuyeke.biteadminservice.user.domain.entity.AppUser;
import com.bitejiuyeke.biteadminservice.user.domain.entity.Encrypt;
import com.bitejiuyeke.biteadminservice.user.domain.entity.SysUser;
import com.bitejiuyeke.biteadminservice.user.mapper.SysUserMapper;
import com.bitejiuyeke.biteadminservice.user.mq.domain.FileTaskDTO;
import com.bitejiuyeke.biteadminservice.user.service.ISysUserService;
import com.bitejiuyeke.bitecommoncore.utils.*;
import com.bitejiuyeke.bitecommondomain.constants.FilePrefixConstants;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import com.bitejiuyeke.bitecommondomain.domain.dto.LoginUserDTO;
import com.bitejiuyeke.bitecommondomain.domain.dto.TokenDTO;
import com.bitejiuyeke.bitecommonrabbitmq.component.TaskProducer;
import com.bitejiuyeke.bitecommonrabbitmq.handler.TaskHandler;
import com.bitejiuyeke.bitecommonsecurity.service.TokenService;
import com.bitejiuyeke.bitefileapi.domain.dto.FileUploadDTO;
import com.bitejiuyeke.bitefileapi.domain.vo.FileVO;
import com.bitejiuyeke.bitefileapi.feign.FileFeignClient;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysUserServiceImpl implements ISysUserService {

    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private ISysDictionaryService sysDictionaryService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private FileFeignClient fileFeignClient;

    @Autowired
    private TaskProducer taskProducer;


    @Override
    public TokenDTO loginByPassword(LoginPasswordDTO loginPasswordDTO) {

        // 1. 判断电话格式是否正确
        if (!VerifyUtil.checkMobile(loginPasswordDTO.getPhone())) {
            throw new ServiceException(ResultCode.ERROR_PHONE_FORMAT);
        }

        // 2. 判断电话是否存在
        SysUser sysUser = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getPhoneNumber, new Encrypt(loginPasswordDTO.getPhone())));
        if (sysUser == null) {
            throw new ServiceException("当前手机号不存在！", ResultCode.INVALID_PARA.getCode());
        }

        // 3. 判断密码是否正确
        // 先解密
        String password = AESUtil.decryptHex(loginPasswordDTO.getPassword());
        if (StringUtils.isEmpty(password)) {
            throw new ServiceException("密码解析为空！", ResultCode.INVALID_PARA.getCode());
        }
        // 再加密（这一步其实是计算数据摘要）
        String passwordEncrypt = DigestUtil.sha256Hex(password);
        if (!passwordEncrypt.equals(sysUser.getPassword())) {
            throw new ServiceException("当前密码不正确！", ResultCode.INVALID_PARA.getCode());
        }

        // 4. 创建用户登录信息并返回 token
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        loginUserDTO.setUserId(String.valueOf(sysUser.getId()));
        loginUserDTO.setUsername(sysUser.getNickName());
        loginUserDTO.setUserFrom(loginPasswordDTO.getLoginFrom());
        return tokenService.createToken(loginUserDTO);
    }


    @Override
    public Long addOrEdit(SysUserDTO sysUserDTO) {
        SysUser sysUser = new SysUser();
        if (sysUserDTO.getUserId() == null) {

            if (StringUtils.isEmpty(sysUserDTO.getNickName())) {
                sysUser.setNickName("Bite-sys-user-" + StringUtil.generateRandomStr(10));
            }

            // 进行添加用户行为，校验用户信息
            checkPhoneNumberIsValid(sysUserDTO.getPhoneNumber());

            // 校验用户的身份信息是否合法
            checkIdentityIsValid(sysUserDTO.getIdentity());

            // 校验用户的密码是否合法
            checkPasswordIsValid(sysUserDTO.getPassword());

            BeanCopyUtil.copyProperties(sysUserDTO, sysUser);
            sysUser.getPhoneNumber().setValue(sysUserDTO.getPhoneNumber());
            sysUser.setPassword(DigestUtil.sha256Hex(sysUserDTO.getPassword()));
        } else {
            // 查看用户是否存在
            sysUser = sysUserMapper.selectById(sysUserDTO.getUserId());
            if (sysUser == null) {
                throw new ServiceException("当前编辑的用户不存在！", ResultCode.INVALID_PARA.getCode());
            }

            // 修改昵称
            if (StringUtils.isNotEmpty(sysUserDTO.getNickName())) {
                sysUser.setNickName(sysUserDTO.getNickName());
            }

            // 判断电话号码是否合法
            if (StringUtils.isNotEmpty(sysUserDTO.getPhoneNumber())
                    && !sysUser.getPhoneNumber().getValue().equals(sysUserDTO.getPhoneNumber())) {
                checkPhoneNumberIsValid(sysUserDTO.getPhoneNumber());
                sysUser.setPhoneNumber(new Encrypt(sysUserDTO.getPhoneNumber()));
            }
            // 判断密码是否合法
            if (StringUtils.isNotEmpty(sysUserDTO.getPassword())) {
                checkPasswordIsValid(sysUserDTO.getPassword());
                sysUser.setPassword(DigestUtil.sha256Hex(sysUserDTO.getPassword()));
            }
            // 判断身份信息是否合法
            if (StringUtils.isNotEmpty(sysUserDTO.getIdentity())) {
                checkIdentityIsValid(sysUserDTO.getIdentity());
                sysUser.setIdentity(sysUserDTO.getIdentity());
            }
        }

        sysUser.setStatus(sysUserDTO.getStatus());
        sysUser.setRemark(sysUserDTO.getRemark());
        sysUserMapper.insertOrUpdate(sysUser);
        return sysUser.getId();
    }


    @Override
    public List<SysUserDTO> getUserList(Long userId, String phoneNumber, String status) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<SysUser>().select();
        if (userId != null) queryWrapper.eq(SysUser::getId, userId);
        if (VerifyUtil.checkMobile(phoneNumber)) queryWrapper.eq(SysUser::getPhoneNumber, phoneNumber);
        if (StringUtils.isNotEmpty(status)) queryWrapper.eq(SysUser::getStatus, status);
        return sysUserMapper.selectList(queryWrapper).stream().map(this::sysUserToSysUserDTO).toList();

    }


    @Override
    public SysUserDTO getLoginInfo() {
        LoginUserDTO loginUser = tokenService.getLoginUser();
        if (loginUser == null || StringUtils.isEmpty(loginUser.getUserId())) {
            throw new ServiceException(ResultCode.TOKEN_INVALID);
        }
        return sysUserToSysUserDTO(sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .select().eq(SysUser::getId, loginUser.getUserId())));
    }

    @Override
    public void uploadAppUserInfoFile(MultipartFile excel) {
        // 校验用户身份
        if (!checkSysUserPrivacy(UserTypeConstants.ADMIN_USER)) {
            throw new ServiceException("当前用户无权限！", ResultCode.PRIVACY_NOT_ENOUGH.getCode());
        }
        if (!ExcelUtils.columNameValidate(excel, AppUser.class)) {
            throw new ServiceException("所上传的 excel 文件格式不符合 app 用户的落库要求！", ResultCode.INVALID_PARA.getCode());
        }
        try {
            R<FileVO> uploadRet = fileFeignClient.upload(excel, FilePrefixConstants.APP_USER_EXCEL);
            if (uploadRet == null || uploadRet.getData() == null || uploadRet.getCode() != ResultCode.SUCCESS.getCode()) {
                throw new ServiceException("上传 excel 文件失败！", ResultCode.ERROR.getCode());
            }

            sendUploadAppTask(uploadRet.getData().getUrl());
        } catch (Exception e) {
            log.error("", e);
        }


    }

    @Override
    public void uploadAppUserInfoUrl(String excelUrl) {
        // 校验用户身份
        if (!checkSysUserPrivacy(UserTypeConstants.ADMIN_USER)) {
            throw new ServiceException("当前用户无权限！", ResultCode.PRIVACY_NOT_ENOUGH.getCode());
        }
        sendUploadAppTask(excelUrl);
    }

    /**
     * 校验手机号码是否合法
     *
     * @param phoneNumber 手机号码
     */
    private void checkPhoneNumberIsValid(String phoneNumber) {
        if (StringUtils.isEmpty(phoneNumber) || !VerifyUtil.checkMobile(phoneNumber)) {
            throw new ServiceException("手机号码格式错误！", ResultCode.INVALID_PARA.getCode());
        }

        if (sysUserMapper.exists(new LambdaQueryWrapper<SysUser>().select().eq(SysUser::getPhoneNumber, new Encrypt(phoneNumber)))) {
            throw new ServiceException("手机号码已存在！", ResultCode.INVALID_PARA.getCode());
        }

    }

    /**
     * 校验密码是否合法
     *
     * @param password 密码
     */
    private void checkPasswordIsValid(String password) {
        if (StringUtils.isEmpty(password) || !VerifyUtil.checkPassword(password)) {
            throw new ServiceException("密码格式错误！", ResultCode.INVALID_PARA.getCode());
        }
    }

    /**
     * 校验身份是否合法
     *
     * @param identity 身份
     */
    private void checkIdentityIsValid(String identity) {
        DicDataDTO dicDataDTO = sysDictionaryService.selectDicDataByDataKey(identity);
        if (dicDataDTO == null) {
            throw new ServiceException("指定用户身份信息不存在！", ResultCode.INVALID_PARA.getCode());
        }
    }

    /**
     * 发送上传用户任务
     *
     * @param excelUrl 用户表格 url
     */
    private void sendUploadAppTask(String excelUrl) {
        try {
            taskProducer.sendTaskToMq(MqTaskTypeConstant.UPLOAD_APP_USER, excelUrl);
        } catch (Exception e) {
            throw new ServiceException("上传用户任务提交失败！", ResultCode.ERROR.getCode());
        }
    }

    /**
     * 判断当前用户是不是给定角色（依托 token）
     *
     * @param role 角色
     * @return 是 = true ; 不是 = false
     */
    private boolean checkSysUserPrivacy(String role) {
        LoginUserDTO loginUser = tokenService.getLoginUser();
        if (loginUser == null) {return false;}
        String userId = tokenService.getLoginUser().getUserId();
        SysUser sysUser = sysUserMapper.selectById(userId);
        return sysUser != null &&
                sysDictionaryService.selectDicDataByDataKey(sysUser.getIdentity()).getTypeKey().equals(role);
    }

    /**
     * 转换类型
     */
    private SysUserDTO sysUserToSysUserDTO(SysUser sysUser) {
        if (sysUser == null) return null;
        SysUserDTO sysUserDTO = new SysUserDTO();
        BeanCopyUtil.copyProperties(sysUser, sysUserDTO);
        sysUserDTO.setPhoneNumber(sysUser.getPhoneNumber().getValue());
        return sysUserDTO;
    }


}
