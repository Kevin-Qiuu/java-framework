package com.bitejiuyeke.biteadminservice.user.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bitejiuyeke.biteadminapi.config.domain.vo.DicDataVO;
import com.bitejiuyeke.biteadminapi.config.feign.DictionaryFeignClient;
import com.bitejiuyeke.biteadminservice.config.service.ISysDictionaryService;
import com.bitejiuyeke.biteadminservice.user.domain.dto.LoginPasswordDTO;
import com.bitejiuyeke.biteadminservice.user.domain.dto.SysUserDTO;
import com.bitejiuyeke.biteadminservice.user.domain.entity.Encrypt;
import com.bitejiuyeke.biteadminservice.user.domain.entity.SysUser;
import com.bitejiuyeke.biteadminservice.user.mapper.SysUserMapper;
import com.bitejiuyeke.biteadminservice.user.service.ISysUserService;
import com.bitejiuyeke.bitecommoncore.utils.AESUtil;
import com.bitejiuyeke.bitecommoncore.utils.BeanCopyUtil;
import com.bitejiuyeke.bitecommoncore.utils.VerifyUtil;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import com.bitejiuyeke.bitecommondomain.domain.dto.LoginUserDTO;
import com.bitejiuyeke.bitecommondomain.domain.dto.TokenDTO;
import com.bitejiuyeke.bitecommonsecurity.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserServiceImpl implements ISysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private ISysDictionaryService sysDictionaryService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private DictionaryFeignClient dictionaryFeignClient;

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
            // 进行添加用户行为，校验用户信息

            if (StringUtils.isEmpty(sysUserDTO.getPhoneNumber()) || !VerifyUtil.checkMobile(sysUserDTO.getPhoneNumber())) {
                throw new ServiceException("手机号码格式错误！", ResultCode.INVALID_PARA.getCode());
            }

            if (sysUserMapper.exists(new LambdaQueryWrapper<SysUser>().select().eq(SysUser::getPhoneNumber, new Encrypt(sysUserDTO.getPhoneNumber())))) {
                throw new ServiceException("手机号码已存在！", ResultCode.INVALID_PARA.getCode());
            }

            if (StringUtils.isEmpty(sysUserDTO.getPassword()) || !VerifyUtil.checkPassword(sysUserDTO.getPassword())) {
                throw new ServiceException("密码格式错误！", ResultCode.INVALID_PARA.getCode());
            }

            // 校验用户的 identity 信息是否合法
            R<DicDataVO> dicDataVOR = dictionaryFeignClient.selectDicDataByDataKey(sysUserDTO.getIdentity());
            if (dicDataVOR == null || dicDataVOR.getCode() != ResultCode.SUCCESS.getCode()) {
                throw new ServiceException(ResultCode.ERROR);
            }
            if (dicDataVOR.getData() == null) {
                throw new ServiceException("用户身份信息不存在！", ResultCode.INVALID_PARA.getCode());
            }

            BeanCopyUtil.copyProperties(sysUserDTO, sysUser);
            sysUser.getPhoneNumber().setValue(sysUserDTO.getPhoneNumber());
            sysUser.setPassword(DigestUtil.sha256Hex(sysUserDTO.getPassword()));
        } else {
            // 查看用户是否存在
            if (!sysUserMapper.exists(new LambdaQueryWrapper<SysUser>().eq(SysUser::getId, sysUserDTO.getUserId()))) {
                throw new ServiceException("当前编辑的用户不存在！", ResultCode.INVALID_PARA.getCode());
            }
            sysUser.setId(sysUserDTO.getUserId());
        }

        if (StringUtils.isEmpty(sysUserDTO.getIdentity())
                || sysDictionaryService.selectDicDataByDataKey(sysUserDTO.getIdentity()) == null) {
            throw new ServiceException("身份信息不存在！", ResultCode.INVALID_PARA.getCode());
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
