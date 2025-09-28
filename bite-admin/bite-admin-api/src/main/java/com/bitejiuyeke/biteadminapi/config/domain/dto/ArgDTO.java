package com.bitejiuyeke.biteadminapi.config.domain.dto;

import lombok.Data;

@Data
public class ArgDTO {

    /**
     * 参数 id
     */
    private Long id;

    /**
     * 参数名
     */
    private String name;

    /**
     * 参数键
     */
    private String configKey;

    /**
     * 参数值
     */
    private String value;

    /**
     * 参数备注
     */
    private String remark;

}
