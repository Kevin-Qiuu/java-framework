package com.bitejiuyeke.biteadminservice.map.domain.dto;

import lombok.Data;

@Data
public class GeoCoderDTO{

    /**
     * 行政区划信息
     */
    private GeoAdInfoDTO ad_info;

    /**
     * 结合知名地点形成的描述性地址，更具人性化特点
     */
    private GeoFormattedAddressesDTO formatted_addresses;

}
