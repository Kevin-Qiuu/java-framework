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
import jakarta.validation.constraints.NotBlank;
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
    @PostMapping("/dictionaryType/add")
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
    @PostMapping("/dictionaryType/list")
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
    @PostMapping("/dictionaryType/edit")
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
    @PostMapping("/dictionaryData/add")
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
    @PostMapping("/dictionaryData/list")
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
     * @param dicDataWriteReqDTO 写字典数据请求体
     * @return 编辑的字典数据 id
     */
    @PostMapping("/dictionaryData/edit")
    public R<Long> editDictionaryData(DicDataWriteReqDTO dicDataWriteReqDTO) {
        Long editDicDataId = sysDictionaryService.editDictionaryData(dicDataWriteReqDTO);
        return R.ok(editDicDataId);
    }

    /**
     * 根据一个字典类型键查询所有的字典数据
     *
     * @param typeKey 字典类型键（非空）
     * @return 字典数据的 list
     */
    @Override
    @GetMapping("/dictionaryData/typeKey/{typeKey}")
    public R<List<DicDataVO>> selectDicDataByTypeKey(@PathVariable("typeKey") String typeKey) {
        List<DicDataDTO> dicDataDTOS = sysDictionaryService.selectDicDataByTypeKey(typeKey);
        return R.ok(BeanCopyUtil.copyListProperties(dicDataDTOS, DicDataVO::new));
    }

    /**
     * 根据多个字典类型键查询所有的字典数据
     *
     * @param typeKeys 字典类型键集合（非空）
     * @return 字典类型键：字典数据
     */
    @Override
    @PostMapping("/dictionaryData/typeKeys")
    public R<Map<String, List<DicDataVO>>> selectDicDataByTypeKeys(@RequestBody List<String> typeKeys) {
        Map<String, List<DicDataDTO>> dicDataDTOMap = sysDictionaryService.selectDicDataByTypeKeys(typeKeys);
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
     * @return 字典数据
     */
    @Override
    @GetMapping("/dictionaryData/dataKey/{dataKey}")
    public R<DicDataVO> selectDicDataByDataKey(@PathVariable("dataKey") String dataKey) {
        DicDataDTO dicDataDTO = sysDictionaryService.selectDicDataByDataKey(dataKey);
        if (dicDataDTO == null)
            return R.ok(null);
        DicDataVO dicDataVO = new DicDataVO();
        BeanCopyUtil.copyProperties(dicDataDTO, dicDataVO);
        return R.ok(dicDataVO);
    }

    /**
     * 根据多个字典数据键查询对应的字典数据
     *
     * @param dataKeys 字典数据键集合
     * @return 字典数据的 List
     */
    @Override
    @PostMapping("/dictionaryData/dataKeys")
    public R<List<DicDataVO>> selectDicDataByDataKeys(@RequestBody List<String> dataKeys) {
        List<DicDataDTO> dicDataDTOS = sysDictionaryService.selectDicDataByDataKeys(dataKeys);
        return R.ok(BeanCopyUtil.copyListProperties(dicDataDTOS, DicDataVO::new));
    }
}
