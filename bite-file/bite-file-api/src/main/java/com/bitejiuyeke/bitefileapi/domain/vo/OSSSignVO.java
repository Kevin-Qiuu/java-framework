package com.bitejiuyeke.bitefileapi.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class OSSSignVO implements Serializable {

    private String host;

    private String pathPrefix;

    private String policy;

    private String ossCredential;

    private String ossDate;

    private String signature;

}
