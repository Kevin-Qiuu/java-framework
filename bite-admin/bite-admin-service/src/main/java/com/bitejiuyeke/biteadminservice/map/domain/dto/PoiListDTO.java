package com.bitejiuyeke.biteadminservice.map.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class PoiListDTO extends TencentMapBaseResponseDTO{

    /**
     * 本次搜素的 poi 结果数
     */
    private Integer count;

    /**
     * 搜索到的 poi 列表
     */
    private List<PoiDTO> data;

}
