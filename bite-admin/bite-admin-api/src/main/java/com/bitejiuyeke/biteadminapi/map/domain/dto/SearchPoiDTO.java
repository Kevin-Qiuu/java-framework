package com.bitejiuyeke.biteadminapi.map.domain.dto;

import lombok.Data;

@Data
public class SearchPoiDTO {

    /**
     * 地点名称
     */
    private String title;

    /**
     * 地点地址
     */
    private String address;

    /**
     * 地点经度
     */
    private Double longitude;

    /**
     * 地点纬度
     */
    private Double latitude;

}
