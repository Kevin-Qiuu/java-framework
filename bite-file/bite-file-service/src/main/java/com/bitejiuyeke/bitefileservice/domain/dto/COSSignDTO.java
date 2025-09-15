package com.bitejiuyeke.bitefileservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class COSSignDTO {

    /**
     * objectKey 的前缀
     */
    private String keyPrefix;

    /**
     * 允许上传的文件名
     */
    private String filename;

    /**
     * Host
     * 请求的主机，形式为<BucketName-APPID>.cos.<Region>.myqcloud.com
     */
    private String host;

    /**
     * 允许上传的文件 url
     * 前端可以请求的 url（已签名授权）
     */
    private String uploadFileUrl;

    /**
     * 授权码
     */
    private String authorizationStr;

}
