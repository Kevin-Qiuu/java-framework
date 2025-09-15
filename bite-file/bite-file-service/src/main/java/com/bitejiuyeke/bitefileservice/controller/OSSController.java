package com.bitejiuyeke.bitefileservice.controller;

import com.bitejiuyeke.bitecommoncore.utils.BeanCopyUtil;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitefileservice.domain.dto.FileDTO;
import com.bitejiuyeke.bitefileservice.domain.dto.OSSSignDTO;
import com.bitejiuyeke.bitefileservice.domain.vo.FileVO;
import com.bitejiuyeke.bitefileservice.domain.vo.OSSSignVO;
import com.bitejiuyeke.bitefileservice.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@ConditionalOnProperty(value = "storage.type", havingValue = "oss")
@RequestMapping("/oss")
public class OSSController {

    @Autowired
    private IFileService fileService;

    @PostMapping("/upload")
    public R<FileVO> upload(MultipartFile multipartFile) {
        FileDTO fileDTO = fileService.upload(multipartFile);
        FileVO fileVO = new FileVO();
        BeanCopyUtil.copyProperties(fileDTO, fileVO);
        return R.ok(fileVO);
    }

    @GetMapping("/getSign")
    public R<OSSSignVO> getSign() {
        OSSSignDTO ossSignDTO = fileService.getOOSSign();
        OSSSignVO ossSignVO = new OSSSignVO();
        BeanCopyUtil.copyProperties(ossSignDTO, ossSignVO);
        return R.ok(ossSignVO);
    }

}
