package com.bitejiuyeke.bitefileservice.domain.vo;

import lombok.Data;

@Data
public class OSSSignVO {

    private String host;

    private String pathPrefix;

    private String policy;

    private String ossCredential;

    private String ossDate;

    private String signature;

}
