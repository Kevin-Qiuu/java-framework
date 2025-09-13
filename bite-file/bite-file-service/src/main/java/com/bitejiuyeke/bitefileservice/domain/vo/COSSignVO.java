package com.bitejiuyeke.bitefileservice.domain.vo;

import lombok.Data;

@Data
public class COSSignVO {

     /**
     * objectKey 的前缀
     */
    String keyPrefix;

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
     * 授权码
     */
    private String authorizationStr;
}
