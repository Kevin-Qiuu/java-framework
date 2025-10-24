package com.bitejiuyeke.bitefileservice.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class FileDTO implements Serializable {
    /**
     * 文件 url : https://<BucketName-APPID>.cos.<Region>.myqcloud.com/folder/img.png
     */
    private String url;
    /**
     * 文件路径 /folder/img.png
     */
    private String key;
    /**
     * 文件名称 img.png
     */
    private String name;
}
