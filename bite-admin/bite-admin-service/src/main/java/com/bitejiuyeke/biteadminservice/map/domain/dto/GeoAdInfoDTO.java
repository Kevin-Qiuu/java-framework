package com.bitejiuyeke.biteadminservice.map.domain.dto;

import lombok.Data;

@Data
public class GeoAdInfoDTO {

    /**
     * 国家代码
     */
    private String nation_code;

    /**
     * 行政区划代码
     */
    private String adcode;

    /**
     * 城市代码
     */
    private String city_code;

    /**
     * 城市电话区号
     */
    private String phone_area_code;

    /**
     * 行政区划名称
     */
    private String name;

    /**
     * 国家
     */
    private String nation;

    /**
     * 省 / 直辖市
     */
    private String province;

    /**
     * 市 / 低级区
     */
    private String city;


}
