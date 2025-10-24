package com.bitejiuyeke.bitefileservice.controller;

import com.bitejiuyeke.bitecommoncore.utils.BeanCopyUtil;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitefileapi.domain.dto.FileUploadDTO;
import com.bitejiuyeke.bitefileapi.domain.vo.FileVO;
import com.bitejiuyeke.bitefileapi.domain.vo.OSSSignVO;
import com.bitejiuyeke.bitefileapi.feign.FileFeignClient;
import com.bitejiuyeke.bitefileservice.domain.dto.FileDTO;
import com.bitejiuyeke.bitefileservice.domain.dto.OSSSignDTO;
import com.bitejiuyeke.bitefileservice.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@ConditionalOnProperty(value = "storage.type", havingValue = "oss")
public class OSSController implements FileFeignClient {

    @Autowired
    private IFileService fileService;

    @Override
    public R<FileVO> upload(@RequestPart("multipartFile") MultipartFile multipartFile,
                            @RequestParam("filePrefix") String filePrefix) {
        FileDTO fileDTO = fileService.upload(multipartFile, filePrefix);
        FileVO fileVO = new FileVO();
        BeanCopyUtil.copyProperties(fileDTO, fileVO);
        return R.ok(fileVO);
    }

    @Override
    public R<OSSSignVO> getOSSSign() {
        OSSSignDTO ossSignDTO = fileService.getOOSSign();
        OSSSignVO ossSignVO = new OSSSignVO();
        BeanCopyUtil.copyProperties(ossSignDTO, ossSignVO);
        return R.ok(ossSignVO);
    }

}
