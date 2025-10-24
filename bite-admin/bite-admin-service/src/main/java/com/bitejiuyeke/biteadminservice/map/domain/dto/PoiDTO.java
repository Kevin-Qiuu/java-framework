package com.bitejiuyeke.biteadminservice.map.domain.dto;

import com.bitejiuyeke.biteadminapi.map.domain.dto.LocationDTO;
import lombok.Data;

@Data
public class PoiDTO {

    /**
     * POI id
     */
    private String id;

    /**
     * 行政区划代码
     */
    private String adcode;

    /**
     * POI 地点名称
     */
    private String title;

    /**
     * POI 具体地址
     */
    private String address;

    /**
     * POI类型，值说明：0:普通POI / 1:公交车站 / 2:地铁站 / 3:公交线路 / 4:行政区划
     */
    private String type;

    /**
     * POI 经纬度
     */
    private LocationDTO location;


}
