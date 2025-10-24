package com.bitejiuyeke.biteadminapi.map.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LocationDTO {

    /**
     * 经度
     */
    @NotNull(message = "经度为空！")
    private Double lat;

    /**
     * 纬度
     */
    @NotNull(message = "纬度为空！")
    private Double lng;

}
