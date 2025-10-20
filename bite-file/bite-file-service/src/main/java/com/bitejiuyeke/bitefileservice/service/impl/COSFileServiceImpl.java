package com.bitejiuyeke.bitefileservice.service.impl;

import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import com.bitejiuyeke.bitefileservice.config.COSProperties;
import com.bitejiuyeke.bitefileservice.domain.dto.COSSignDTO;
import com.bitejiuyeke.bitefileservice.domain.dto.FileDTO;
import com.bitejiuyeke.bitefileservice.service.IFileService;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.auth.COSSigner;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@ConditionalOnProperty(value = "storage.type", havingValue = "cos")
public class COSFileServiceImpl implements IFileService {

    @Autowired
    private COSClient cosClient;
    @Autowired
    private COSProperties cosProperties;

    @Override
    public FileDTO upload(MultipartFile file) {
         if (file == null || file.isEmpty()) {
            log.error("上传文件为空");
            throw new ServiceException(ResultCode.COS_UPLOAD_FAILED);
        }
        FileDTO fileDTO = new FileDTO();
        try {
            // 直接使用 MultipartFile 的 InputStream 进行上传
            InputStream inputStream = file.getInputStream();

            // cos 的存储 Id 是 prefix/UUID+文件后缀名
            String originalFilename = file.getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uploadFileKey = cosProperties.getPathPrefix() + UUID.randomUUID() + extName;

            // 设置文件元数据
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());

            // 创建 PutObjectRequest
            PutObjectRequest putObjectRequest = new PutObjectRequest(cosProperties.getBucketName(),
                    uploadFileKey, inputStream, objectMetadata);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            URL objectUrl = cosClient.getObjectUrl(cosProperties.getBucketName(), uploadFileKey);
            log.info("打印 URL：{}", objectUrl);
            if (putObjectResult == null || objectUrl == null
                    || !StringUtils.hasLength(putObjectResult.getRequestId())) {
                log.error("上传至 COS 服务 PutObjectResult 未正常返回: {}", putObjectResult);
                throw new ServiceException(ResultCode.COS_UPLOAD_FAILED);
            }
            fileDTO.setUrl(String.valueOf(objectUrl.toURI()));
            fileDTO.setKey(uploadFileKey);
            fileDTO.setName(originalFilename);
        } catch (Exception e){
            log.error("文件上传至 COS 服务失败");
            throw new ServiceException(ResultCode.COS_UPLOAD_FAILED);
        }

        return fileDTO;
    }

    @Override
    public COSSignDTO getCOSSign(String filename, Boolean useUUIDFilename) {
        if (StringUtils.hasLength(filename) || filename.lastIndexOf(".") == -1) {
            log.error("请求上传的文件名为空，或者不存在文件后缀名，filename:{}", filename);
            throw new ServiceException(ResultCode.COS_UPLOAD_FAILED);
        }
        useUUIDFilename = useUUIDFilename != null && useUUIDFilename;
        // 创建身份认证信息
        COSCredentials cred = new BasicCOSCredentials(cosProperties.getSecretId(), cosProperties.getSecretKey());
        COSSigner cosSigner = new COSSigner();

        // 构建资源路径
        String bucketName = cosProperties.getBucketName();
        String extName = filename.substring(filename.lastIndexOf("."));
        if (useUUIDFilename) {
            filename = UUID.randomUUID() + extName;
        }
        String resourcePath = "/" + cosProperties.getPathPrefix() + filename;

        // 构建host头部信息
        String host = bucketName + ".cos." + cosProperties.getRegionName() + ".myqcloud.com";
        Map<String, String> headers = new HashMap<>();
        headers.put("host", host);


        String authorizationStr = cosSigner.buildAuthorizationStr(HttpMethodName.PUT, resourcePath, headers, new HashMap<>(),
                cred, new Date(cosProperties.getSignExpiredTime() * 1000L + System.currentTimeMillis()));
        COSSignDTO cosSignDTO = new COSSignDTO();
        cosSignDTO.setHost(host);
        cosSignDTO.setFilename(filename);
        cosSignDTO.setUploadFileUrl("https://" + host + resourcePath);
        cosSignDTO.setKeyPrefix(cosSignDTO.getKeyPrefix());
        cosSignDTO.setAuthorizationStr(authorizationStr);
        return cosSignDTO;
    }
}
