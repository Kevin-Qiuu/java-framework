package com.bitejiuyeke.biteadminservice.map.domain.dto;

import lombok.Data;

@Data
public class GeoResultDTO extends TencentMapBaseResponseDTO{

    private GeoCoderDTO result;

}
