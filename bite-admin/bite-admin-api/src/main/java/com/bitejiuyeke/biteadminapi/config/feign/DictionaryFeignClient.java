package com.bitejiuyeke.biteadminapi.config.feign;

import com.bitejiuyeke.biteadminapi.config.domain.dto.DicDataAddReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.DicDataReadReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.DicTypeReadReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.DicTypeWriteReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.vo.DicDataVO;
import com.bitejiuyeke.biteadminapi.config.domain.vo.DicTypeVO;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.vo.BasePageVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface DictionaryFeignClient {

    /**
     * 新增字典类型
     * @param dicTypeWriteReqDTO 写字典类型请求体
     * @return 新增字典 id
     */
    @PostMapping("/dictionaryType/add")
    R<Long> addDictionaryType(@RequestBody @Validated DicTypeWriteReqDTO dicTypeWriteReqDTO);

    /**
     * 获取字典类型列表
     * @param dicTypeReadReqDTO 读字典类型请求体
     * @return 翻页列表
     */
    @GetMapping("/dictionaryType/list")
    R<BasePageVO<DicTypeVO>> getDictionaryTypeList(@RequestBody DicTypeReadReqDTO dicTypeReadReqDTO);

    /**
     * 编辑字典类型数据
     * @param dicTypeWriteReqDTO 写字典类型请求体
     * @return 编辑字典类型 Id
     */
    @PostMapping("/dictionaryType/edit")
    R<Long> editDictionaryType(@RequestBody @Validated DicTypeWriteReqDTO dicTypeWriteReqDTO);

    /**
     * 新增字典数据
     * @param dicDataAddReqDTO 新增字典数据请求体
     * @return 新增字典数据 Id
     */
    @PostMapping("/dictionaryData/add")
    R<Long> addDictionaryData(@RequestBody @Validated DicDataAddReqDTO dicDataAddReqDTO);


    @GetMapping("/dicitonaryData/list")
    R<BasePageVO<DicDataVO>> getDictionaryDataList(@RequestBody @Validated DicDataReadReqDTO dicDataReadReqDTO);

}
