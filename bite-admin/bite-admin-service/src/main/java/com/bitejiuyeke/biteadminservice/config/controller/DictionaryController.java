package com.bitejiuyeke.biteadminservice.config.controller;

import com.bitejiuyeke.biteadminapi.config.domain.dto.DicTypeWriteReqDTO;
import com.bitejiuyeke.biteadminservice.config.service.ISysDictionaryService;
import com.bitejiuyeke.bitecommondomain.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DictionaryController {

    @Autowired
    private ISysDictionaryService sysDictionaryService;

    @PostMapping("/dictionaryType/add")
    public R<Long> dictionaryTypeAdd(@RequestBody @Validated DicTypeWriteReqDTO dicTypeWriteReqDTO) {
        Long addDicTypeId = sysDictionaryService.dictionaryTypeAdd(dicTypeWriteReqDTO);
        return R.ok(addDicTypeId);
    }

}
