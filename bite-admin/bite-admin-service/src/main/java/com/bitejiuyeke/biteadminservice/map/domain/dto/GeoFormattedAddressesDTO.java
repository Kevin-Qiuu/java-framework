package com.bitejiuyeke.biteadminservice.map.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GeoFormattedAddressesDTO implements Serializable {

    /**
     * 推荐使用的地址描述，描述精确性较高
     */
    private String recommend;

    /**
     * 粗略位置描述
     */
    private String rough;

    /**
     * 基于附近关键地点（POI）的精确地址
     */
    private String standard_address;

}
