package com.bitejiuyeke.biteadminapi.config.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DicTypeWriteReqDTO {

    /**
     * 字典类型键
     */
    @NotBlank(message = "字典类型键为空！")
    private String typeKey;

    /**
     * 字典类型值
     */
    @NotBlank(message = "字典类型值为空！")
    private String value;

    /**
     * 备注（非必填）
     */
    private String remark;

}
