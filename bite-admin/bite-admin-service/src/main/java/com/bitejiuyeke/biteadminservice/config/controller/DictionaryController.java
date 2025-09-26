package com.bitejiuyeke.biteadminservice.config.controller;

import com.bitejiuyeke.biteadminapi.config.domain.dto.DicDataAddReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.DicTypeDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.DicTypeReadReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.DicTypeWriteReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.vo.DicTypeVO;
import com.bitejiuyeke.biteadminapi.config.feign.DictionaryFeignClient;
import com.bitejiuyeke.biteadminservice.config.service.ISysDictionaryService;
import com.bitejiuyeke.bitecommoncore.utils.BeanCopyUtil;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.dto.BasePageDTO;
import com.bitejiuyeke.bitecommondomain.domain.dto.BasePageReqDTO;
import com.bitejiuyeke.bitecommondomain.domain.vo.BasePageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DictionaryController implements DictionaryFeignClient {

    @Autowired
    private ISysDictionaryService sysDictionaryService;

    @PostMapping("/dictionaryType/add")
    public R<Long> addDictionaryType(@RequestBody @Validated DicTypeWriteReqDTO dicTypeWriteReqDTO) {
        Long addDicTypeId = sysDictionaryService.addDictionaryType(dicTypeWriteReqDTO);
        return R.ok(addDicTypeId);
    }

    @GetMapping("/dictionaryType/list")
    public R<BasePageVO<DicTypeVO>> getDictionaryTypeList(@RequestBody DicTypeReadReqDTO dicTypeReadReqDTO){
        BasePageDTO<DicTypeDTO> basePageDTO = sysDictionaryService.getDictionaryTypeList(dicTypeReadReqDTO);
        BasePageVO<DicTypeVO> basePageVO = new BasePageVO<>();
        BeanCopyUtil.copyProperties(basePageDTO, basePageVO);
        List<DicTypeVO> dicTypeVOS = BeanCopyUtil.copyListProperties(basePageDTO.getList(), DicTypeVO::new);
        basePageVO.setList(dicTypeVOS);
        return R.ok(basePageVO);
    }

    @PostMapping("/dictionaryType/edit")
    public R<Long> editDictionaryType(@RequestBody @Validated DicTypeWriteReqDTO dicTypeWriteReqDTO) {
        Long editDicTypeId = sysDictionaryService.editDictionaryType(dicTypeWriteReqDTO);
        return R.ok(editDicTypeId);
    }

    @PostMapping("/dictionaryData/add")
    public R<Long> addDictionaryData(@RequestBody @Validated DicDataAddReqDTO dicDataAddReqDTO) {
        Long addDicDataId = sysDictionaryService.addDictionaryData(dicDataAddReqDTO);
        return R.ok(addDicDataId);
    }


}
