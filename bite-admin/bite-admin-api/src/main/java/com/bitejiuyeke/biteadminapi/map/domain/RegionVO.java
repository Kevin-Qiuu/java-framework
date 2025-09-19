package com.bitejiuyeke.biteadminapi.map.domain;

import lombok.Data;

@Data
public class RegionVO {

    /**
     * 区域 Id
     */
    private Long id;

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
    private Long parentId;

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
