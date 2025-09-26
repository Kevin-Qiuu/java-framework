package com.bitejiuyeke.biteadminservice.config.service;

import com.bitejiuyeke.biteadminapi.config.domain.dto.DicDataAddReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.DicTypeDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.DicTypeReadReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.DicTypeWriteReqDTO;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.dto.BasePageDTO;

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
     * @param dicDataAddReqDTO 字典数据新增请求体
     * @return 新增字典数据 id
     */
    Long addDictionaryData(DicDataAddReqDTO dicDataAddReqDTO);

}
