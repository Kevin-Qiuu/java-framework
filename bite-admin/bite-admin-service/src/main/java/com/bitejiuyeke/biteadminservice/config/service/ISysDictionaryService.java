package com.bitejiuyeke.biteadminservice.config.service;

import com.bitejiuyeke.biteadminapi.config.domain.dto.*;
import com.bitejiuyeke.bitecommondomain.domain.dto.BasePageDTO;

import java.util.List;
import java.util.Map;

public interface ISysDictionaryService {

    /**
     * 新增字典类型
     * @param dicTypeWriteReqDTO 写字典类型请求体
     * @return 新增字典类型 id
     */
    Long addDictionaryType(DicTypeWriteReqDTO dicTypeWriteReqDTO);

    /**
     * 查询字典相关类型
     * @param dicTypeReadReqDTO 读字典类型请求体
     * @return 翻页 list
     */
    BasePageDTO<DicTypeDTO> getDictionaryTypeList(DicTypeReadReqDTO dicTypeReadReqDTO);

    /**
     * 编辑字典已存在的相关类型
     * @param dicTypeWriteReqDTO 写字典类型请求体
     * @return 修改字典类型 id
     */
    Long editDictionaryType(DicTypeWriteReqDTO dicTypeWriteReqDTO);

    /**
     * 新增字典数据
     * @param dicDataWriteReqDTO 字典数据新增请求体
     * @return 新增字典数据 id
     */
    Long addDictionaryData(DicDataWriteReqDTO dicDataWriteReqDTO);

    /**
     * 根据字典类型键查询字典类型值
     * @param dicDataReadReqDTO 字典类型值查询请求体
     * @return 翻页列表
     */
    BasePageDTO<DicDataDTO> getDictionaryDataList(DicDataReadReqDTO dicDataReadReqDTO);

    /**
     * 编辑字典数据
     * @param dicDataWriteReqDTO 写字典数据请求体
     * @return 编辑的字典数据 id
     */
    Long editDictionaryData(DicDataWriteReqDTO dicDataWriteReqDTO);

    /**
     * 根据一个字典类型键查询所有的字典数据
     * @param typeKey 字典类型键（非空）
     * @return 字典数据的 list
     */
    List<DicDataDTO> selectDicDataByTypeKey(String typeKey);

    /**
     * 根据多个字典类型键查询所有的字典数据
     * @param typeKeys 字典类型键集合（非空）
     * @return 字典数据的 list
     */
    Map<String, List<DicDataDTO>> selectDicDataByTypeKeys(List<String> typeKeys);

    /**
     * 根据一个字数据键查询对应的字典数据
     * @param dataKey 字典数据键
     * @return 字典数据
     */
    DicDataDTO selectDicDataByDataKey(String dataKey);

    /**
     * 根据多个字典数据键查询对应的字典数据
     * @param dataKeys 字典数据键集合
     * @return 字典数据的 List
     */
    List<DicDataDTO> selectDicDataByDataKeys(List<String> dataKeys);

}
