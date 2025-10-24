package com.bitejiuyeke.biteadminapi.map.domain.vo;

import lombok.Data;

@Data
public class SearchPoiVO {

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
    private double longitude;

    /**
     * 地点纬度
     */
    private double latitude;

}
