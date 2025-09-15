package com.bitejiuyeke.bitefileservice.controller;

import com.bitejiuyeke.bitecommoncore.utils.BeanCopyUtil;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitefileservice.domain.dto.COSSignDTO;
import com.bitejiuyeke.bitefileservice.domain.dto.FileDTO;
import com.bitejiuyeke.bitefileservice.domain.vo.COSSignVO;
import com.bitejiuyeke.bitefileservice.domain.vo.FileVO;
import com.bitejiuyeke.bitefileservice.service.IFileService;
import com.bitejiuyeke.bitefileservice.service.impl.COSFileServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@ConditionalOnProperty(value = "storage.type", havingValue = "cos")
@RequestMapping("/cos")
@Slf4j
public class COSController {
    @Autowired
    private IFileService fileService;

    @PostMapping("/upload")
    public R<FileVO> upload(MultipartFile multipartFile) throws IOException {
        FileDTO upload = fileService.upload(multipartFile);
        FileVO fileVO = new FileVO();
        BeanCopyUtil.copyProperties(upload, fileVO);
        return R.ok(fileVO);
    }

     /**
     * COS 获取签名授权，需给定上传的文件 key (filename)
     * COS 与 OOS 不同，COS 授权只可授权上传文件 url 的签名，无法对整个目录或者整个 bucket 进行授权
     * 所以上传的时候需要传递本次上传的文件名，用于获取上传文件的后缀名
     * useUUIDFilename 表示是否通过 UUID 更改上传文件的文件名
     *
     * @param filename 上传文件名
     * @param useUUIDFilename 上传文件名使用 UUID，不传递或者传递 false 则不使用
     * @return R<COSSignVO>
     */
    @GetMapping("/getSign")
    public R<COSSignVO> getCOSSign(String filename, Boolean useUUIDFilename) {
        COSSignDTO cosSignDTO = fileService.getCOSSign(filename, useUUIDFilename);
        COSSignVO cosSignVO = new COSSignVO();
        BeanCopyUtil.copyProperties(cosSignDTO, cosSignVO);
        return R.ok(cosSignVO);
    }
}
