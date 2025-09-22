package com.bitejiuyeke.biteadminservice.map.domain.dto;

import com.bitejiuyeke.bitecommondomain.domain.dto.BasePageReqDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SuggestSearchDTO extends BasePageReqDTO {

    /**
     * 区域 code（限制城市）
     */
    private String code;

    /**
     * 搜索关键词
     */
    private String keyword;

    /**
     * 0：不限制当前城市，会召回其他城市的 poi
     * 1：[默认] 仅限制在当前城市，
     */
    private Integer region_fix = 1;

}
