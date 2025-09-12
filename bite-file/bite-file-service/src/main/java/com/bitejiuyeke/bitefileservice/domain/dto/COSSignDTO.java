package com.bitejiuyeke.bitefileservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class COSSignDTO {
    /**
     * 授权码
     */
    private String authorizationStr;

}
