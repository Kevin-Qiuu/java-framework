package com.bitejiuyeke.biteadminservice.user.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Encrypt implements Serializable {

    /**
     * 加解密内容
     */
    private String value;

}
