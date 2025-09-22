package com.bitejiuyeke.biteadminapi.map.domain.dto;

import com.bitejiuyeke.bitecommondomain.domain.dto.BasePageReqDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SearchPoiReqDTO  extends BasePageReqDTO {

    /**
     * 查询关键词
     */
    @NotBlank(message = "查询关键词为空！")
    private String keyword;

    /**
     * 查询地的区域 code
     */
    @NotEmpty(message = "查询地区 code 为空！")
    private String code;

}
