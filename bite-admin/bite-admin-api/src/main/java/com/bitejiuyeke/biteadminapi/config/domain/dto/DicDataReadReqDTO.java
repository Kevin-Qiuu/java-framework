package com.bitejiuyeke.biteadminapi.config.domain.dto;

import com.bitejiuyeke.bitecommondomain.domain.dto.BasePageReqDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DicDataReadReqDTO extends BasePageReqDTO {

    /**
     * 字典类型键
     */
    @NotBlank(message = "字典类型键为空！")
    private String typeKey;

    /**
     * 字典数据名称
     */
    private String value;

}
