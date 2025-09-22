package com.bitejiuyeke.biteadminservice.map.domain.dto;

import lombok.Data;

@Data
public class RegionDTO {

    /**
     * 区域 code
     */
    private String code;

    /**
     * 区域名称
     */
    private String name;

    /**
     * 区域全称
     */
    private String fullName;

    /**
     * 父级区域 code
     */
    private Integer parentCode;

    /**
     * 区域名称拼音
     */
    private String pinyin;

    /**
     * 区域级别
     */
    private Integer level;

    /**
     * 区域经度
     */
    private Double longitude;

    /**
     * 区域纬度
     */
    private Double latitude;

}
