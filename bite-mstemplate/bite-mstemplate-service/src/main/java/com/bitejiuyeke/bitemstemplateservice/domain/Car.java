package com.bitejiuyeke.bitemstemplateservice.domain;

import lombok.Data;

import java.util.Map;

@Data
public class Car {
    private String brand;
    private Long price;
    private String ownerName;
    private Map<String, Long> elementInfo;
}
