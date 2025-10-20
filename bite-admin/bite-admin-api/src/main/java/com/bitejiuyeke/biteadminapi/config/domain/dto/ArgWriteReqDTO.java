package com.bitejiuyeke.biteadminapi.config.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ArgWriteReqDTO {

    /**
     * 参数名称
     */
    @NotBlank(message = "参数名称为空！")
    private String name;

    /**
     * 参数键名
     */
    @NotBlank(message = "参数键为空！")
    private String configKey;

    /**
     * 参数值
     */
    @NotBlank(message = "参数值为空！")
    private String value;

    /**
     * 参数备注
     */
    private String remark;

}
