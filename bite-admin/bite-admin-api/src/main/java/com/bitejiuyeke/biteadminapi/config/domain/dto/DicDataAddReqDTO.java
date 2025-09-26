package com.bitejiuyeke.biteadminapi.config.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DicDataAddReqDTO {

    /**
     * 字典数据的类型键
     */
    @NotBlank(message = "字典类型键为空！")
    private String typeKey;

    /**
     * 字典数据的键
     */
    @NotBlank(message = "字典数据的键为空！")
    private String dataKey;

    /**
     * 字典数据的值
     */
    @NotBlank(message = "字典数据的值为空！")
    private String value;

    /**
     * 字典数据的备注
     */
    private String remark;

    /**
     * 字典数据的排序序号
     */
    private Integer sort;

    /**
     * 字典数据的状态：【默认】 1 表示启用，0 表示禁用
     */
    private Integer status = 1;

}
