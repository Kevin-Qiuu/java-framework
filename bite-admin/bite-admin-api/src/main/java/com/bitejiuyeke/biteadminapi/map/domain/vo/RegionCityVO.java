package com.bitejiuyeke.biteadminapi.map.domain.vo;

import lombok.Data;

@Data
public class RegionCityVO {

    /**
     * 区域 code
     */
    private String code;

    /**
     * 区域名称
     */
    private String name;

    /**
     * 区域全名
     */
    private String fullName;

}
