package com.bitejiuyeke.biteadminservice.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bitejiuyeke.biteadminapi.user.domain.dto.AppUserDTO;
import com.bitejiuyeke.biteadminapi.user.domain.dto.EditUserReqDTO;
import com.bitejiuyeke.biteadminservice.user.config.RabbitMqUserConfig;
import com.bitejiuyeke.biteadminservice.user.domain.dto.AppUserListReqDTO;
import com.bitejiuyeke.biteadminservice.user.domain.entity.AppUser;
import com.bitejiuyeke.biteadminservice.user.domain.entity.Encrypt;
import com.bitejiuyeke.biteadminservice.user.mapper.AppUserMapper;
import com.bitejiuyeke.biteadminservice.user.service.IAppUserService;
import com.bitejiuyeke.bitecommoncore.utils.BeanCopyUtil;
import com.bitejiuyeke.bitecommoncore.utils.StringUtil;
import com.bitejiuyeke.bitecommoncore.utils.VerifyUtil;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import com.bitejiuyeke.bitecommondomain.domain.dto.BasePageDTO;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import com.bitejiuyeke.bitecommondomain.domain.dto.LoginUserDTO;
import com.bitejiuyeke.bitecommondomain.domain.dto.TokenDTO;
import com.bitejiuyeke.bitecommonsecurity.service.TokenService;
import com.bitejiuyeke.bitenotifyapi.captcha.domain.dto.LoginByPhoneReqDTO;
import com.bitejiuyeke.bitenotifyapi.captcha.feign.CaptchaFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class AppUserServiceImpl implements IAppUserService {

    /**
     * mapper 服务
     */
    @Autowired
    private AppUserMapper appUserMapper;

    /**
     * token 服务
     */
    @Autowired
    private TokenService tokenService;

    /**
     * 验证码服务
     */
    @Autowired
    private CaptchaFeignClient captchaFeignClient;


    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 默认头像 url 地址
     */
    @Value("${app-user.info.default-avatar}")
    private String defaultAvatar;


    /**
     * 根据手机号查询用户信息
     *
     * @param phoneNumber 手机号
     * @return C端用户DTO
     */
    @Override
    public AppUserDTO findByPhone(String phoneNumber) {
        if (!VerifyUtil.checkMobile(phoneNumber)) {
            return null;
        }
        return convertToAppUserDTO(appUserMapper.selectOne(new LambdaQueryWrapper<AppUser>()
                .select().eq(AppUser::getPhoneNumber, new Encrypt(phoneNumber))));
    }

    /**
     * 根据手机验证码进行登录（如果用户不存在自动注册，注册成功会为用户自动分配一个 token）
     *
     * @return token
     */
    @Override
    public TokenDTO loginByPhone(LoginByPhoneReqDTO reqDTO) {

        R<Boolean> booleanR = null;
        // 校验验证码是否正确
        try {
            booleanR = captchaFeignClient.verifyCaptchaCode(reqDTO);
        } catch (Exception e) {
            log.error("调用 captchaFeign 失败：", e);
        }
        if (booleanR == null) {
            throw new ServiceException(ResultCode.INVALID_CODE);
        }
        if (booleanR.getCode() != ResultCode.SUCCESS.getCode()) {
            throw new ServiceException(booleanR.getMsg(), booleanR.getCode());
        }

        // 创建 token
        AppUserDTO appUserDTO;
        AppUser appUser = appUserMapper.selectOne(new LambdaQueryWrapper<AppUser>().select().eq(AppUser::getPhoneNumber, new Encrypt(reqDTO.getPhoneNumber())));
        if (appUser == null) {
            // 用户不存在则自动注册
            appUserDTO = registerByPhone(reqDTO.getPhoneNumber());
        } else {
            appUserDTO = convertToAppUserDTO(appUser);
        }

        return createTokenForAppUserDTO(appUserDTO, reqDTO.getUserFrom());

    }

    /**
     * 编辑用户信息
     *
     * @param reqDTO 编辑用户请求 DTO
     */
    @Override
    public void editUserInfo(EditUserReqDTO reqDTO) {

        AppUser appUser = appUserMapper.selectOne(new LambdaQueryWrapper<AppUser>()
                .select().eq(AppUser::getId, reqDTO.getUserId()));
        if (appUser == null) {
            throw new ServiceException("所要编辑的用户不存在！", ResultCode.INVALID_PARA.getCode());
        }

        if (StringUtils.isNotEmpty(reqDTO.getNickName())) {
            appUser.setNickName(reqDTO.getNickName());
        }
        if (StringUtils.isNotEmpty(reqDTO.getAvatar())) {
            appUser.setAvatar(reqDTO.getAvatar());
        }

        appUserMapper.updateById(appUser);

        // 发送广播信息，帮助其他服务感知该用户信息已修改，进而进行后续处理
        AppUserDTO appUserDTO = new AppUserDTO();
        BeanCopyUtil.copyProperties(appUser, appUserDTO);
        appUserDTO.setUserId(appUser.getId());
        try {
            rabbitTemplate.convertAndSend(RabbitMqUserConfig.EXCHANGE_NAME, "", appUserDTO);
        } catch (Exception e) {
            log.error("广播用户信息修改消息发送失败！", e);
        }

    }

    private AppUserDTO registerByPhone(String phoneNumber) {
        AppUser appUser = new AppUser();
        appUser.setNickName("Bite-User-" + StringUtil.generateRandomStr(10));
        appUser.setAvatar(defaultAvatar);
        appUser.setPhoneNumber(new Encrypt(phoneNumber));
        appUserMapper.insert(appUser);
        return convertToAppUserDTO(appUser);
    }

    private TokenDTO createTokenForAppUserDTO(AppUserDTO appUserDTO, String userFrom) {
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        loginUserDTO.setUserId(String.valueOf(appUserDTO.getUserId()));
        loginUserDTO.setUsername(appUserDTO.getNickName());
        loginUserDTO.setUserFrom(userFrom);
        return tokenService.createToken(loginUserDTO);
    }

    /**
     * 转换 C 端用户信息类型
     *
     * @param appUser C 端用户信息 mapper
     * @return C 端用户信息 DTO
     */
    private AppUserDTO convertToAppUserDTO(AppUser appUser) {
        if (appUser == null) return null;
        AppUserDTO appUserDTO = new AppUserDTO();
        BeanCopyUtil.copyProperties(appUser, appUserDTO);
        appUserDTO.setUserId(appUser.getId());
        appUserDTO.setPhoneNumber(appUser.getPhoneNumber().getValue());
        return appUserDTO;
    }

    /**
     * 获取用户信息
     *
     * @param userId 用户 id
     * @return 用户信息
     */
    @Override
    public AppUserDTO findById(Long userId) {
        if (userId == null) {
            throw new ServiceException("用户 id 为空！", ResultCode.INVALID_PARA.getCode());
        }
        AppUser appUser = appUserMapper.selectOne(new LambdaQueryWrapper<AppUser>().select().eq(AppUser::getId, userId));
        return convertToAppUserDTO(appUser);
    }

    /**
     * 获取用户信息列表
     *
     * @param userIds 用户 id 列表
     * @return 用户信息
     */
    @Override
    public List<AppUserDTO> findUserList(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            throw new ServiceException("用户 id 列表为空！", ResultCode.INVALID_PARA.getCode());
        }
        List<AppUser> appUsers = appUserMapper.selectList(new LambdaQueryWrapper<AppUser>().select().in(AppUser::getId, userIds));
        return appUsers.stream().filter(Objects::nonNull).map(this::convertToAppUserDTO).toList();
    }

    /**
     * 获取用户信息（列表&分页）
     *
     * @param appUserListReqDTO 用户信息请求体
     * @return 用户信息列表（分页）
     */
    @Override
    public BasePageDTO<AppUserDTO> getUserList(AppUserListReqDTO appUserListReqDTO) {
        BasePageDTO<AppUserDTO> pageDTO = new BasePageDTO<>();
        LambdaQueryWrapper<AppUser> appUserQuery = new LambdaQueryWrapper<>();

        if (appUserListReqDTO.getUserId() != null) {
            appUserQuery.eq(AppUser::getId, appUserListReqDTO.getUserId());
        }
        if (StringUtils.isNotEmpty(appUserListReqDTO.getNickName())) {
            appUserQuery.likeRight(AppUser::getNickName, appUserListReqDTO.getNickName());
        }
        if (StringUtils.isNotEmpty(appUserListReqDTO.getPhoneNumber()) && VerifyUtil.checkMobile(appUserListReqDTO.getPhoneNumber())) {
            appUserQuery.eq(AppUser::getPhoneNumber, new Encrypt(appUserListReqDTO.getPhoneNumber()));
        }
        if (StringUtils.isNotEmpty(appUserListReqDTO.getOpenId())) {
            appUserQuery.eq(AppUser::getOpenId, appUserListReqDTO.getOpenId());
        }

        // 获取查询总数
        Long selectCount = appUserMapper.selectCount(appUserQuery);
        pageDTO.setTotals(selectCount.intValue());
        pageDTO.setTotalPages(BasePageDTO.calculateTotalPages(selectCount, appUserListReqDTO.getPageSize()));
        if (selectCount == 0) {
            pageDTO.setList(null);
            return pageDTO;
        }

        // 获取分页 list
        Page<AppUser> appUserPage = new Page<>(appUserListReqDTO.getOffset(), appUserListReqDTO.getPageSize());
        List<AppUser> records = appUserMapper.selectPage(appUserPage, appUserQuery).getRecords();

        // 判断是否超页
        if (records.isEmpty()){
            pageDTO.setList(null);
            return pageDTO;
        }

        List<AppUserDTO> appUserDTOS = records.stream().map(appUser -> {
            AppUserDTO appUserDTO = new AppUserDTO();
            BeanCopyUtil.copyProperties(appUser, appUserDTO);
            appUserDTO.setUserId(appUser.getId());
            appUserDTO.setPhoneNumber(appUser.getPhoneNumber().getValue());
            return appUserDTO;
        }).toList();

        pageDTO.setList(appUserDTOS);
        return pageDTO;
    }
}
