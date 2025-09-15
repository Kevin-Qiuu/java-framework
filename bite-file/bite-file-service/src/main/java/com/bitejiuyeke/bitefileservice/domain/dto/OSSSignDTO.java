package com.bitejiuyeke.bitefileservice.domain.dto;

import lombok.Data;

@Data
public class OSSSignDTO {

    private String host;

    private String pathPrefix;

    private String policy;

    private String ossCredential;

    private String ossDate;

    private String signature;

}
