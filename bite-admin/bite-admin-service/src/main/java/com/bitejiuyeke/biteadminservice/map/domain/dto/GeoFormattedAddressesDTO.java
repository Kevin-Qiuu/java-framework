package com.bitejiuyeke.biteadminservice.map.domain.dto;

import lombok.Data;

@Data
public class GeoFormattedAddressesDTO {

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
