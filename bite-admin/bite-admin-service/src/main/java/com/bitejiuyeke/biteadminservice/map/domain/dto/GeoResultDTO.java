package com.bitejiuyeke.biteadminservice.map.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class GeoResultDTO extends TencentMapBaseResponseDTO{

    private GeoCoderDTO result;

}
