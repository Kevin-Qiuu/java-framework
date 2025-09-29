package com.bitejiuyeke.biteadminapi.config.feign;

import com.bitejiuyeke.biteadminapi.config.domain.dto.DicDataReadReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.vo.DicDataVO;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.vo.BasePageVO;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@ConditionalOnProperty(name = "feign.bite-admin.feignEnabled", havingValue = "true")
@FeignClient(contextId = "dictionaryFeignClient", name = "bite-admin")
public interface DictionaryFeignClient {

    /**
     * 根据一个字典类型键查询所有的字典数据
     *
     * @param typeKey 字典类型键（非空）
     * @return 字典数据的 list
     */
    @GetMapping("/dictionaryData/typeKey/{typeKey}")
    R<List<DicDataVO>> selectDicDataByTypeKey(@PathVariable("typeKey")
                                              @NotBlank(message = "字典类型键为空！") String typeKey);

    /**
     * 根据多个字典类型键查询所有的字典数据
     *
     * @param typeKeys 字典类型键集合（非空）
     * @return 字典类型键：字典数据
     */
    @PostMapping("/dictionaryData/typeKeys")
    R<Map<String, List<DicDataVO>>> selectDicDataByTypeKeys(@RequestBody
                                                            @NotBlank(message = "字典类型键列表为空或者有空值！") List<String> typeKeys);

    /**
     * 查询字典类型值
     *
     * @param dicDataReadReqDTO 读字典类型值请求体
     * @return 翻页列表
     */
    @PostMapping("/dictionaryData/list")
    R<BasePageVO<DicDataVO>> getDictionaryDataList(@RequestBody DicDataReadReqDTO dicDataReadReqDTO);


    /**
     * 根据一个字数据键查询对应字典数据
     *
     * @param dataKey 字典数据键
     * @return 字典数据
     */
    @GetMapping("/dictionaryData/dataKey/{dataKey}")
    R<DicDataVO> selectDicDataByDataKey(@PathVariable("dataKey")
                                        @NotBlank(message = "字典数据键为空！") String dataKey);

    /**
     * 根据多个字典数据键查询对应的字典数据
     *
     * @param dataKeys 字典数据键集合
     * @return 字典数据的 List
     */
    @PostMapping("/dictionaryData/dataKeys")
    R<List<DicDataVO>> selectDicDataByDataKeys(@RequestBody
                                               @NotBlank(message = "字典类型键列表为空或者有空值!") List<String> dataKeys);

}
