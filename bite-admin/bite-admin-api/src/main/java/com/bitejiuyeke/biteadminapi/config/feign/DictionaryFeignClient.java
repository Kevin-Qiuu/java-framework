package com.bitejiuyeke.biteadminapi.config.feign;

import com.bitejiuyeke.biteadminapi.config.domain.dto.DicDataWriteReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.DicDataReadReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.DicTypeReadReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.DicTypeWriteReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.vo.DicDataVO;
import com.bitejiuyeke.biteadminapi.config.domain.vo.DicTypeVO;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.vo.BasePageVO;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

public interface DictionaryFeignClient {

    /**
     * 根据一个字典类型键查询所有的字典数据
     * @param typeKey 字典类型键（非空）
     * @return 字典数据的 list
     */
    @GetMapping("/dictionaryData/typeKey")
    R<List<DicDataVO>> selectDicDataByTypeKey(@NotBlank(message = "字典类型键为空！") String typeKey);

    /**
     * 根据多个字典类型键查询所有的字典数据
     * @param typeKeys 字典类型键集合（非空）
     * @return 字典数据的 list
     */
    @GetMapping("/dictionaryData/typeKeys")
    R<Map<String, List<DicDataVO>>> selectDicDataByTypeKeys(@NotBlank(message = "字典类型键列表为空或者有空值！") List<String> typeKeys);

    /**
     * 根据一个字数据键查询所有的字典数据
     * @param dataKey 字典数据键
     * @return 字典数据
     */
    @GetMapping("/dictionaryData/dataKey")
    R<DicDataVO> selectDicDataByDataKey(@NotBlank(message = "字典数据键为空！") String dataKey);

}
