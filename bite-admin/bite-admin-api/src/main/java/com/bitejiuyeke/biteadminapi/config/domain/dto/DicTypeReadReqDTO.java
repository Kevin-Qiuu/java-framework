package com.bitejiuyeke.biteadminapi.config.domain.dto;

import com.bitejiuyeke.bitecommondomain.domain.dto.BasePageReqDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DicTypeReadReqDTO extends BasePageReqDTO {

    /**
     * 字典类型键
     * 唯一查询
     */
    private String typeKey;

    /**
     * 字典类型值
     * 右模糊查询
     */
    private String value;

}
