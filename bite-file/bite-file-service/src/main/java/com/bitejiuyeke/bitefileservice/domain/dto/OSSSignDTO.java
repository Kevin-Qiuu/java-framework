package com.bitejiuyeke.bitefileservice.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OSSSignDTO implements Serializable {

    private String host;

    private String pathPrefix;

    private String policy;

    private String ossCredential;

    private String ossDate;

    private String signature;

}
