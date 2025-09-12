package com.bitejiuyeke.bitefileservice.service;

import com.bitejiuyeke.bitefileservice.domain.dto.COSSignDTO;
import com.bitejiuyeke.bitefileservice.domain.dto.FileDTO;
import com.bitejiuyeke.bitefileservice.domain.dto.OOSSignDTO;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
    /**
     * 上传文件
     * @param file 文件
     * @return 文件资源信息
     */
    FileDTO upload(MultipartFile file);

    /**
     * 获取 COS 签名信息
     * @return cos 签名信息
     */
    default COSSignDTO getCOSSign() {return null;};

    /**
     * 获取 OOS 签名信息
     * @return oos 签名信息
     */
    default OOSSignDTO getOOSSign() {return null;};

}
