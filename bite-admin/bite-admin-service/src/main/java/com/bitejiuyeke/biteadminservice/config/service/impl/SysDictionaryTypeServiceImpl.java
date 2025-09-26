package com.bitejiuyeke.biteadminservice.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bitejiuyeke.biteadminapi.config.domain.dto.DicDataAddReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.DicTypeDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.DicTypeReadReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.DicTypeWriteReqDTO;
import com.bitejiuyeke.biteadminservice.config.domain.entity.SysDictionaryData;
import com.bitejiuyeke.biteadminservice.config.domain.entity.SysDictionaryType;
import com.bitejiuyeke.biteadminservice.config.mapper.SysDictionaryDataMapper;
import com.bitejiuyeke.biteadminservice.config.mapper.SysDictionaryTypeMapper;
import com.bitejiuyeke.biteadminservice.config.service.ISysDictionaryService;
import com.bitejiuyeke.bitecommoncore.utils.BeanCopyUtil;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.dto.BasePageDTO;
import com.bitejiuyeke.bitecommonsecurity.exception.ServiceException;
import jodd.util.StringUtil;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysDictionaryTypeServiceImpl implements ISysDictionaryService {

    @Autowired
    private SysDictionaryTypeMapper sysDictionaryTypeMapper;
    @Autowired
    private SysDictionaryDataMapper sysDictionaryDataMapper;

    @Override
    public Long addDictionaryType(DicTypeWriteReqDTO dicTypeWriteReqDTO) {
        LambdaQueryWrapper<SysDictionaryType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(SysDictionaryType::getId)
                .eq(SysDictionaryType::getTypeKey, dicTypeWriteReqDTO.getTypeKey()).or()
                .eq(SysDictionaryType::getValue, dicTypeWriteReqDTO.getValue());
        SysDictionaryType sysDictionaryType = sysDictionaryTypeMapper.selectOne(lambdaQueryWrapper);
        if (sysDictionaryType != null) {
            throw new ServiceException("字典类型键或者值已存在！");
        }
        sysDictionaryType = new SysDictionaryType();
        BeanCopyUtil.copyProperties(dicTypeWriteReqDTO, sysDictionaryType);
        sysDictionaryType.setStatus(1);
        // insert 方法会自动为 id 变量赋值
        sysDictionaryTypeMapper.insert(sysDictionaryType);
        return sysDictionaryType.getId();
    }

    @Override
    public BasePageDTO<DicTypeDTO> getDictionaryTypeList(DicTypeReadReqDTO dicTypeReadReqDTO) {
        // 创建分页对象
        Page<SysDictionaryType> page = new Page<>(dicTypeReadReqDTO.getPageIndex(), dicTypeReadReqDTO.getPageSize());

        // 构建查询条件
        LambdaQueryWrapper<SysDictionaryType> queryWrapper = new LambdaQueryWrapper<>();

        // 添加查询条件
        if (StringUtil.isNotBlank(dicTypeReadReqDTO.getTypeKey())) {
            queryWrapper.eq(SysDictionaryType::getTypeKey, dicTypeReadReqDTO.getTypeKey());
        }
        if (StringUtil.isNotBlank(dicTypeReadReqDTO.getValue())) {
            queryWrapper.likeRight(SysDictionaryType::getValue, dicTypeReadReqDTO.getValue());
        }

        // 执行分页查询
        Page<SysDictionaryType> sysPage = sysDictionaryTypeMapper.selectPage(page, queryWrapper);

        // 构造返回结果
        BasePageDTO<DicTypeDTO> basePageDTO = new BasePageDTO<>();
        basePageDTO.setTotals((int) sysPage.getTotal());
        basePageDTO.setTotalPages((int) sysPage.getPages());
        basePageDTO.setList(BeanCopyUtil.copyListProperties(sysPage.getRecords(), DicTypeDTO::new));

        return basePageDTO;
    }

    @Override
    public Long editDictionaryType(DicTypeWriteReqDTO dicTypeWriteReqDTO) {

        // 判断这个类型是否存在
        SysDictionaryType sysDictionaryType = sysDictionaryTypeMapper.selectOne(
                new LambdaQueryWrapper<SysDictionaryType>()
                        .eq(SysDictionaryType::getTypeKey, dicTypeWriteReqDTO.getTypeKey()));
        if (sysDictionaryType == null) {
            throw new ServiceException("所要修改的类型键不存在！");
        }

        // 存在判断该字典类型值是否已经被使用了
        if (sysDictionaryTypeMapper.selectOne(new LambdaQueryWrapper<SysDictionaryType>()
                .ne(SysDictionaryType::getTypeKey, dicTypeWriteReqDTO.getTypeKey())
                .eq(SysDictionaryType::getValue, dicTypeWriteReqDTO.getValue())) != null) {
            throw new ServiceException("所要使用的类型值已经存在！");
        }

        // 修改字典键值
        sysDictionaryType.setValue(dicTypeWriteReqDTO.getValue());
        sysDictionaryTypeMapper.updateById(sysDictionaryType);

        // 返回更新的类型键 id
        return sysDictionaryType.getId();
    }

    @Override
    public Long addDictionaryData(DicDataAddReqDTO dicDataAddReqDTO) {
        // 判断字典数据类型是否存在
        if (!sysDictionaryTypeMapper.exists(new LambdaQueryWrapper<SysDictionaryType>()
                .eq(SysDictionaryType::getTypeKey, dicDataAddReqDTO.getTypeKey()))) {
            throw new ServiceException("新增字典数据所对应的字典类型不存在！");
        }

        // 存在的话判断字典数据键值是否存在
        if (sysDictionaryDataMapper.exists(new LambdaQueryWrapper<SysDictionaryData>()
                .eq(SysDictionaryData::getDataKey, dicDataAddReqDTO.getDataKey())
                .or()
                .eq(SysDictionaryData::getValue, dicDataAddReqDTO.getValue()))) {
            throw new ServiceException("字典数据已存在重复键或值！");
        }

        // 不存在的话新增一条
        SysDictionaryData sysDictionaryData = new SysDictionaryData();
        BeanCopyUtil.copyProperties(dicDataAddReqDTO, sysDictionaryData);
        sysDictionaryDataMapper.insert(sysDictionaryData);

        return sysDictionaryData.getId();
    }
}
