package com.bitejiuyeke.biteadminservice.map.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TencentMapBaseResponseDTO implements Serializable {

    /**
     * 响应码: 0 表示成功，其他表示异常
     */
    private int status;

    /**
     * 请求 id
     */
    String request_id;

    /**
     * 响应消息
     */
    String message;

}
