package com.bitejiuyeke.bitefileservice.service;

import com.bitejiuyeke.bitefileservice.domain.dto.COSSignDTO;
import com.bitejiuyeke.bitefileservice.domain.dto.FileDTO;
import com.bitejiuyeke.bitefileservice.domain.dto.OSSSignDTO;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
    /**
     * 上传文件
     *
     * @param multipartFile 文件
     * @param filePrefix    文件前缀
     * @return 文件上传结果
     */
    FileDTO upload(MultipartFile multipartFile, String filePrefix);

    /**
     * COS 获取签名授权
     * COS 与 OOS 不同，COS 授权只可授权上传文件 url 的签名，无法对整个目录或者整个 bucket 进行授权
     * 所以上传的时候需要传递本次上传的文件名，用于获取上传文件的后缀名
     * useUUIDFilename 表示是否通过 UUID 更改上传文件的文件名
     *
     * @param filename        上传文件名
     * @param useUUIDFilename 上传文件名使用 UUID
     * @return COSSignDTO
     */
    default COSSignDTO getCOSSign(String filename, Boolean useUUIDFilename) {
        return null;
    }

    ;

    /**
     * 获取 OOS 签名信息
     *
     * @return oos 签名信息
     */
    default OSSSignDTO getOOSSign() {
        return null;
    }

    ;

}
