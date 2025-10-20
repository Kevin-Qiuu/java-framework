package com.bitejiuyeke.biteadminservice.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bitejiuyeke.biteadminapi.config.domain.dto.*;
import com.bitejiuyeke.biteadminservice.config.domain.entity.SysDictionaryData;
import com.bitejiuyeke.biteadminservice.config.domain.entity.SysDictionaryType;
import com.bitejiuyeke.biteadminservice.config.mapper.SysDictionaryDataMapper;
import com.bitejiuyeke.biteadminservice.config.mapper.SysDictionaryTypeMapper;
import com.bitejiuyeke.biteadminservice.config.service.ISysDictionaryService;
import com.bitejiuyeke.bitecommoncore.utils.BeanCopyUtil;
import com.bitejiuyeke.bitecommondomain.domain.dto.BasePageDTO;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        LambdaQueryWrapper<SysDictionaryType> queryWrapper = new LambdaQueryWrapper<SysDictionaryType>().select();

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
                new LambdaQueryWrapper<SysDictionaryType>().select(SysDictionaryType::getTypeKey)
                        .eq(SysDictionaryType::getTypeKey, dicTypeWriteReqDTO.getTypeKey()));
        if (sysDictionaryType == null) {
            throw new ServiceException("所要修改的类型键不存在！");
        }

        // 存在判断该字典类型值是否已经被使用了
        if (sysDictionaryTypeMapper.selectOne(new LambdaQueryWrapper<SysDictionaryType>()
                .select(SysDictionaryType::getTypeKey)
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
    public Long addDictionaryData(DicDataWriteReqDTO dicDataWriteReqDTO) {
        // 判断字典数据类型是否存在
        if (!sysDictionaryTypeMapper.exists(new LambdaQueryWrapper<SysDictionaryType>()
                .select(SysDictionaryType::getTypeKey)
                .eq(SysDictionaryType::getTypeKey, dicDataWriteReqDTO.getTypeKey()))) {
            throw new ServiceException("新增字典数据所对应的字典类型不存在！");
        }

        // 存在的话判断字典数据键值是否存在
        if (sysDictionaryDataMapper.exists(new LambdaQueryWrapper<SysDictionaryData>()
                .select(SysDictionaryData::getDataKey)
                .eq(SysDictionaryData::getDataKey, dicDataWriteReqDTO.getDataKey())
                .or()
                .eq(SysDictionaryData::getValue, dicDataWriteReqDTO.getValue()))) {
            throw new ServiceException("字典数据已存在重复键或值！");
        }

        // 不存在的话新增一条
        SysDictionaryData sysDictionaryData = new SysDictionaryData();
        BeanCopyUtil.copyProperties(dicDataWriteReqDTO, sysDictionaryData);
        sysDictionaryDataMapper.insert(sysDictionaryData);

        return sysDictionaryData.getId();
    }

    @Override
    public BasePageDTO<DicDataDTO> getDictionaryDataList(DicDataReadReqDTO dicDataReadReqDTO) {
        LambdaQueryWrapper<SysDictionaryData> dataQuery = new LambdaQueryWrapper<SysDictionaryData>().select();

        if (StringUtil.isNotBlank(dicDataReadReqDTO.getTypeKey())) {
            dataQuery.eq(SysDictionaryData::getTypeKey, dicDataReadReqDTO.getTypeKey());
        }
        if (StringUtil.isNotBlank(dicDataReadReqDTO.getValue())) {
            dataQuery.like(SysDictionaryData::getValue, dicDataReadReqDTO.getValue());
        }

        dataQuery.orderByAsc(SysDictionaryData::getSort);
        dataQuery.orderByAsc(SysDictionaryData::getId);

        Page<SysDictionaryData> dataPage = sysDictionaryDataMapper.selectPage
                (new Page<>(dicDataReadReqDTO.getPageIndex(), dicDataReadReqDTO.getPageSize()), dataQuery);
        BasePageDTO<DicDataDTO> basePageDTO = new BasePageDTO<>();
        basePageDTO.setTotals((int) dataPage.getTotal());
        basePageDTO.setTotalPages((int) dataPage.getPages());
        basePageDTO.setList(BeanCopyUtil.copyListProperties(dataPage.getRecords(), DicDataDTO::new));

        return basePageDTO;
    }

    @Override
    public Long editDictionaryData(DicDataWriteReqDTO dicDataWriteReqDTO) {
        // 判断字典类型是否存在
        LambdaQueryWrapper<SysDictionaryType> typeQuery = new LambdaQueryWrapper<>();
        typeQuery.select(SysDictionaryType::getTypeKey).eq(SysDictionaryType::getTypeKey, dicDataWriteReqDTO.getTypeKey());
        if (!sysDictionaryTypeMapper.exists(typeQuery)) {
            throw new ServiceException("当前的字典类型键不存在！");
        }

        // 判断字典键值是否存在
        LambdaQueryWrapper<SysDictionaryData> dataQuery = new LambdaQueryWrapper<>();
        dataQuery.select(SysDictionaryData::getTypeKey)
                .eq(SysDictionaryData::getTypeKey, dicDataWriteReqDTO.getTypeKey())
                .eq(SysDictionaryData::getDataKey, dicDataWriteReqDTO.getDataKey());
        SysDictionaryData sysDictionaryData = sysDictionaryDataMapper.selectOne(dataQuery);
        if (!sysDictionaryDataMapper.exists(dataQuery)) {
            throw new ServiceException("当前的字典数据键不存在！");
        }

        // 修改
        BeanCopyUtil.copyProperties(dicDataWriteReqDTO, sysDictionaryData);
        sysDictionaryDataMapper.updateById(sysDictionaryData);

        // 返回
        return sysDictionaryData.getId();
    }


    @Override
    public List<DicDataDTO> selectDicDataByTypeKey(String typeKey) {
        LambdaQueryWrapper<SysDictionaryType> typeQuery = new LambdaQueryWrapper<>();
        typeQuery.select(SysDictionaryType::getTypeKey).eq(SysDictionaryType::getTypeKey, typeKey);
        if (!sysDictionaryTypeMapper.exists(typeQuery)) {
            throw new ServiceException("当前的字典类型键不存在！");
        }

        List<SysDictionaryData> selectList = sysDictionaryDataMapper.selectList(new LambdaQueryWrapper<SysDictionaryData>().select()
                .eq(SysDictionaryData::getTypeKey, typeKey));
        return BeanCopyUtil.copyListProperties(selectList, DicDataDTO::new);
    }

    @Override
    public Map<String, List<DicDataDTO>> selectDicDataByTypeKeys(DicListReqDTO dicListReqDTO) {
        Map<String, List<DicDataDTO>> dicDataDTOMap = new HashMap<>();
        List<SysDictionaryData> selectList = sysDictionaryDataMapper
                .selectList(new LambdaQueryWrapper<SysDictionaryData>()
                        .in(SysDictionaryData::getTypeKey, dicListReqDTO.getKeys()));
        for (SysDictionaryData sysDictionaryData : selectList) {
            if (!dicDataDTOMap.containsKey(sysDictionaryData.getTypeKey())) {
                dicDataDTOMap.put(sysDictionaryData.getTypeKey(), new ArrayList<>());
            }
            DicDataDTO dicDataDTO = new DicDataDTO();
            BeanCopyUtil.copyProperties(sysDictionaryData, dicDataDTO);
            dicDataDTOMap.get(sysDictionaryData.getTypeKey()).add(dicDataDTO);
        }
        return dicDataDTOMap;
    }

    // todo: test
    @Override
    public DicDataDTO selectDicDataByDataKey(String dataKey) {
        DicDataDTO dicDataDTO = new DicDataDTO();
        SysDictionaryData sysDictionaryData = sysDictionaryDataMapper
                .selectOne(new LambdaQueryWrapper<SysDictionaryData>()
                        .eq(SysDictionaryData::getDataKey, dataKey));
        if (sysDictionaryData == null)
            return null;
        BeanCopyUtil.copyProperties(sysDictionaryData, dicDataDTO);
        return dicDataDTO;
    }

    @Override
    public List<DicDataDTO> selectDicDataByDataKeys(DicListReqDTO dicListReqDTO) {
        List<SysDictionaryData> selectList = sysDictionaryDataMapper
                .selectList(new LambdaQueryWrapper<SysDictionaryData>()
                        .in(SysDictionaryData::getDataKey, dicListReqDTO.getKeys()));
        return BeanCopyUtil.copyListProperties(selectList, DicDataDTO::new);
    }
}
