package com.bitejiuyeke.biteadminapi.map.domain.vo;

import lombok.Data;

@Data
public class RegionVO {

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
     * 父级区域列表
     */
    private String parentCode;

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
