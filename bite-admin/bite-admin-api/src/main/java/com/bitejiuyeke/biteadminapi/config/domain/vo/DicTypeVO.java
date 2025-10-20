package com.bitejiuyeke.biteadminapi.config.domain.vo;

import lombok.Data;

@Data
public class DicTypeVO {

    /**
     * id
     */
    private Long id;

    /**
     * 类型键
     */
    private String typeKey;

    /**
     * 类型值
     */
    private String value;

    /**
     * 类型备注
     */
    private String remark;

    /**
     * 类型状态
     */
    private Integer status;

}
