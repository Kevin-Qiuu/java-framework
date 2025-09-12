package com.bitejiuyeke.bitefileservice;

import com.bitejiuyeke.bitecommoncore.utils.BeanCopyUtil;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitefileservice.domain.dto.COSSignDTO;
import com.bitejiuyeke.bitefileservice.domain.dto.FileDTO;
import com.bitejiuyeke.bitefileservice.domain.vo.COSSignVO;
import com.bitejiuyeke.bitefileservice.domain.vo.FileVO;
import com.bitejiuyeke.bitefileservice.service.IFileService;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test/cos")
public class TestCOSController {

    @Autowired
    private IFileService fileService;

    @PostMapping("/upload")
    public R<FileVO> upload(MultipartFile multipartFile) throws IOException {
        FileDTO upload = fileService.upload(multipartFile);
        FileVO fileVO = new FileVO();
        BeanCopyUtil.copyProperties(upload, fileVO);
        return R.ok(fileVO);
    }

    @GetMapping("/getCOSSign")
    public R<COSSignVO> getCOSSign() {
        COSSignDTO cosSignDTO = fileService.getCOSSign();
        COSSignVO cosSignVO = new COSSignVO();
        BeanCopyUtil.copyProperties(cosSignDTO, cosSignVO);
        return R.ok(cosSignVO);
    }


}
