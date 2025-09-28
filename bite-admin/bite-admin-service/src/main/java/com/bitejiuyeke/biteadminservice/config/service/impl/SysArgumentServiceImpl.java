package com.bitejiuyeke.biteadminservice.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgReadReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgWriteReqDTO;
import com.bitejiuyeke.biteadminservice.config.domain.entity.SysArgument;
import com.bitejiuyeke.biteadminservice.config.mapper.SysArgumentMapper;
import com.bitejiuyeke.biteadminservice.config.service.ISysArgumentService;
import com.bitejiuyeke.bitecommoncore.utils.BeanCopyUtil;
import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import com.bitejiuyeke.bitecommondomain.domain.dto.BasePageDTO;
import com.bitejiuyeke.bitecommonsecurity.exception.ServiceException;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysArgumentServiceImpl implements ISysArgumentService {

    @Autowired
    private SysArgumentMapper sysArgumentMapper;

    // todo: test
    @Override
    public Long argumentAdd(ArgWriteReqDTO argWriteReqDTO) {

        SysArgument sysArgument = sysArgumentMapper.selectOne(new LambdaQueryWrapper<SysArgument>()
                .select(SysArgument::getId)
                .eq(SysArgument::getConfigKey, argWriteReqDTO.getConfigKey())
                .or()
                .eq(SysArgument::getValue, argWriteReqDTO.getValue()));
        if (sysArgument != null) {
            throw new ServiceException("参数已存在！", ResultCode.INVALID_PARA.getCode());
        }

        sysArgument = new SysArgument();
        BeanCopyUtil.copyProperties(argWriteReqDTO, sysArgument);
        sysArgumentMapper.insert(sysArgument);
        return sysArgument.getId();

    }

    // todo: test
    @Override
    public BasePageDTO<ArgDTO> argumentList(ArgReadReqDTO argReadReqDTO) {

        LambdaQueryWrapper<SysArgument> queryWrapper = new LambdaQueryWrapper<SysArgument>().select();
        if (StringUtil.isNotBlank(argReadReqDTO.getConfigKey())) {
            queryWrapper.eq(SysArgument::getConfigKey, argReadReqDTO.getConfigKey());
        }
        if (StringUtil.isNotBlank(argReadReqDTO.getName())) {
            queryWrapper.likeRight(SysArgument::getName, argReadReqDTO.getName());
        }

        Page<SysArgument> argumentPage = sysArgumentMapper
                .selectPage(new Page<>(argReadReqDTO.getPageIndex(), argReadReqDTO.getPageSize()), queryWrapper);
        BasePageDTO<ArgDTO> basePageDTO = new BasePageDTO<>();
        basePageDTO.setTotals((int) argumentPage.getTotal());
        basePageDTO.setTotalPages((int) argumentPage.getPages());
        basePageDTO.setList(BeanCopyUtil.copyListProperties(argumentPage.getRecords(), ArgDTO::new));
        return basePageDTO;
    }

    // todo: test
    @Override
    public Long argumentEdit(ArgWriteReqDTO argWriteReqDTO) {
        // 判断存在
        SysArgument sysArgument = sysArgumentMapper.selectOne(new LambdaQueryWrapper<SysArgument>()
                .select(SysArgument::getId).eq(SysArgument::getConfigKey, argWriteReqDTO.getConfigKey()));
        if (sysArgument == null) {
            throw new ServiceException("参数键不存在！", ResultCode.INVALID_PARA.getCode());
        }
        // 参数键的名字要唯一
        if (sysArgumentMapper.exists(new LambdaQueryWrapper<SysArgument>()
                .select(SysArgument::getId).ne(SysArgument::getConfigKey, argWriteReqDTO.getConfigKey())
                .eq(SysArgument::getName, argWriteReqDTO.getName()))) {
            throw new ServiceException("参数名字已存在！", ResultCode.INVALID_PARA.getCode());
        }
        // 存在则更新
        BeanCopyUtil.copyProperties(argWriteReqDTO, sysArgument);
        sysArgumentMapper.updateById(sysArgument);
        return sysArgument.getId();
    }


}
