package com.bitejiuyeke.biteadminservice.user.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Encrypt {

    /**
     * 加解密内容
     */
    private String value;

}
