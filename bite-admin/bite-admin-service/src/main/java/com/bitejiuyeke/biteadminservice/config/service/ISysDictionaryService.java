package com.bitejiuyeke.biteadminservice.config.service;

import com.bitejiuyeke.biteadminapi.config.domain.dto.DicTypeWriteReqDTO;
import com.bitejiuyeke.bitecommondomain.domain.R;

public interface ISysDictionaryService {

    /**
     * 新增字典类型
     * @param dicTypeWriteReqDTO 字典类型请求体
     * @return 新增字典类型 id
     */
    Long dictionaryTypeAdd(DicTypeWriteReqDTO dicTypeWriteReqDTO);
    
}
