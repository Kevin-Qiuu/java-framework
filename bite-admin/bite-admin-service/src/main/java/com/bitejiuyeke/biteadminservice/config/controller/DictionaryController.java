package com.bitejiuyeke.biteadminservice.config.controller;

import com.bitejiuyeke.biteadminapi.config.domain.dto.*;
import com.bitejiuyeke.biteadminapi.config.domain.vo.DicDataVO;
import com.bitejiuyeke.biteadminapi.config.domain.vo.DicTypeVO;
import com.bitejiuyeke.biteadminapi.config.feign.DictionaryFeignClient;
import com.bitejiuyeke.biteadminservice.config.service.ISysDictionaryService;
import com.bitejiuyeke.bitecommoncore.utils.BeanCopyUtil;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.dto.BasePageDTO;
import com.bitejiuyeke.bitecommondomain.domain.vo.BasePageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DictionaryController implements DictionaryFeignClient {

    @Autowired
    private ISysDictionaryService sysDictionaryService;

    /**
     * 新增字典类型
     *
     * @param dicTypeWriteReqDTO 写字典类型请求体
     * @return 新增字典 id
     */
    @PostMapping("/dictionary_type/add")
    public R<Long> addDictionaryType(@RequestBody @Validated DicTypeWriteReqDTO dicTypeWriteReqDTO) {
        Long addDicTypeId = sysDictionaryService.addDictionaryType(dicTypeWriteReqDTO);
        return R.ok(addDicTypeId);
    }

    /**
     * 获取字典类型列表
     *
     * @param dicTypeReadReqDTO 读字典类型请求体
     * @return 翻页列表
     */
    @PostMapping("/dictionary_type/list")
    public R<BasePageVO<DicTypeVO>> getDictionaryTypeList(@RequestBody DicTypeReadReqDTO dicTypeReadReqDTO) {
        BasePageDTO<DicTypeDTO> basePageDTO = sysDictionaryService.getDictionaryTypeList(dicTypeReadReqDTO);
        BasePageVO<DicTypeVO> basePageVO = new BasePageVO<>();
        BeanCopyUtil.copyProperties(basePageDTO, basePageVO);
        List<DicTypeVO> dicTypeVOS = BeanCopyUtil.copyListProperties(basePageDTO.getList(), DicTypeVO::new);
        basePageVO.setList(dicTypeVOS);
        return R.ok(basePageVO);
    }

    /**
     * 编辑字典类型数据
     *
     * @param dicTypeWriteReqDTO 写字典类型请求体
     * @return 编辑字典类型 Id
     */
    @PostMapping("/dictionary_type/edit")
    public R<Long> editDictionaryType(@RequestBody @Validated DicTypeWriteReqDTO dicTypeWriteReqDTO) {
        Long editDicTypeId = sysDictionaryService.editDictionaryType(dicTypeWriteReqDTO);
        return R.ok(editDicTypeId);
    }

    /**
     * 新增字典数据
     *
     * @param dicDataWriteReqDTO 写字典数据请求体
     * @return 新增字典数据 Id
     */
    @PostMapping("/dictionary_data/add")
    public R<Long> addDictionaryData(@RequestBody @Validated DicDataWriteReqDTO dicDataWriteReqDTO) {
        Long addDicDataId = sysDictionaryService.addDictionaryData(dicDataWriteReqDTO);
        return R.ok(addDicDataId);
    }

    /**
     * 查询字典类型值
     *
     * @param dicDataReadReqDTO 读字典类型值请求体
     * @return 翻页列表
     */
    @PostMapping("/dictionary_data/list")
    public R<BasePageVO<DicDataVO>> getDictionaryDataList(@RequestBody DicDataReadReqDTO dicDataReadReqDTO) {
        BasePageDTO<DicDataDTO> basePageDTO = sysDictionaryService.getDictionaryDataList(dicDataReadReqDTO);
        BasePageVO<DicDataVO> basePageVO = new BasePageVO<>();
        BeanCopyUtil.copyProperties(basePageDTO, basePageVO);
        basePageVO.setList(BeanCopyUtil.copyListProperties(basePageDTO.getList(), DicDataVO::new));
        return R.ok(basePageVO);
    }

    /**
     * 编辑字典数据
     *
     * @param dicDataEditReqDTO 编辑字典数据请求体
     * @return 编辑的字典数据 id
     */
    @PostMapping("/dictionary_data/edit")
    public R<Long> editDictionaryData(@RequestBody @Validated DicDataEditReqDTO dicDataEditReqDTO) {
        Long editDicDataId = sysDictionaryService.editDictionaryData(dicDataEditReqDTO);
        return R.ok(editDicDataId);
    }

    /**
     * 根据一个字典类型键查询所有的字典数据
     *
     * @param typeKey 字典类型键（非空）
     * @return 字典数据的 list
     */
    @Override
    @GetMapping("/dictionary_data/type_key")
    public R<List<DicDataVO>> selectDicDataByTypeKey(@RequestParam("typeKey") String typeKey) {
        List<DicDataDTO> dicDataDTOS = sysDictionaryService.selectDicDataByTypeKey(typeKey);
        return R.ok(BeanCopyUtil.copyListProperties(dicDataDTOS, DicDataVO::new));
    }

    /**
     * 根据多个字典类型键查询所有的字典数据
     *
     * @param dicListReqDTO 字典类型键集合 dto
     * @return 字典类型键：字典数据
     */
    @Override
    @PostMapping("/dictionary_data/type_keys")
    public R<Map<String, List<DicDataVO>>> selectDicDataByTypeKeys(@RequestBody DicListReqDTO dicListReqDTO) {
        Map<String, List<DicDataDTO>> dicDataDTOMap = sysDictionaryService.selectDicDataByTypeKeys(dicListReqDTO);
        Map<String, List<DicDataVO>> dicDataVOMap = new HashMap<>();
        for (Map.Entry<String, List<DicDataDTO>> entry : dicDataDTOMap.entrySet()) {
            dicDataVOMap.put(entry.getKey(),
                    BeanCopyUtil.copyListProperties(entry.getValue(), DicDataVO::new));
        }
        return R.ok(dicDataVOMap);
    }

    /**
     * 根据一个字数据键查询对应的字典数据
     *
     * @param dataKey 字典数据键
     * @return 字典数据list
     */
    @Override
    @GetMapping("/dictionary_data/data_key")
    public R<List<DicDataVO>> selectDicDataByDataKey(@RequestParam("dataKey") String dataKey) {
        List<DicDataDTO> dicDataDTOS = sysDictionaryService.selectDicDataByDataKey(dataKey);
        return R.ok(BeanCopyUtil.copyListProperties(dicDataDTOS, DicDataVO::new));
    }

    /**
     * 根据多个字典数据键查询对应的字典数据
     *
     * @param dicListReqDTO 字典数据键集合 dto
     * @return 字典数据的 List
     */
    @Override
    @PostMapping("/dictionary_data/data_keys")
    public R<List<DicDataVO>> selectDicDataByDataKeys(@RequestBody DicListReqDTO dicListReqDTO) {
        List<DicDataDTO> dicDataDTOS = sysDictionaryService.selectDicDataByDataKeys(dicListReqDTO);
        return R.ok(BeanCopyUtil.copyListProperties(dicDataDTOS, DicDataVO::new));
    }
}
