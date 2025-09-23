package com.bitejiuyeke.biteadminservice.config.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bitejiuyeke.biteadminapi.config.domain.dto.DicTypeWriteReqDTO;
import com.bitejiuyeke.biteadminservice.config.domain.entity.SysDictionaryType;
import com.bitejiuyeke.biteadminservice.config.mapper.SysDictionaryTypeMapper;
import com.bitejiuyeke.biteadminservice.config.service.ISysDictionaryService;
import com.bitejiuyeke.bitecommoncore.utils.BeanCopyUtil;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommonsecurity.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysDictionaryTypeServiceImpl implements ISysDictionaryService {

    @Autowired
    private SysDictionaryTypeMapper sysDictionaryTypeMapper;

    @Override
    public Long dictionaryTypeAdd(DicTypeWriteReqDTO dicTypeWriteReqDTO) {
        LambdaQueryWrapper<SysDictionaryType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(SysDictionaryType::getId)
                .eq(SysDictionaryType::getTypeKey, dicTypeWriteReqDTO.getTypeKey()).or()
                .eq(SysDictionaryType::getValue, dicTypeWriteReqDTO.getValue());
        SysDictionaryType sysDictionaryType = sysDictionaryTypeMapper.selectOne(lambdaQueryWrapper);
        if(sysDictionaryType != null) {
            throw new ServiceException("字典类型键或者值已存在！");
        }
        sysDictionaryType = new SysDictionaryType();
        BeanCopyUtil.copyProperties(dicTypeWriteReqDTO, sysDictionaryType);
        sysDictionaryType.setStatus(1);
        // insert 方法会自动为 id 变量赋值
        sysDictionaryTypeMapper.insert(sysDictionaryType);
        return sysDictionaryType.getId();
    }
}
