package com.bitejiuyeke.bitefileservice.service.impl;

import com.alibaba.nacos.common.codec.Base64;
import com.aliyun.oss.*;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import com.bitejiuyeke.bitefileservice.config.OSSProperties;
import com.bitejiuyeke.bitefileservice.constants.OSSCustomConstants;
import com.bitejiuyeke.bitefileservice.domain.dto.FileDTO;
import com.bitejiuyeke.bitefileservice.domain.dto.OSSSignDTO;
import com.bitejiuyeke.bitefileservice.service.IFileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@ConditionalOnProperty(value = "storage.type", havingValue = "oss")
public class OSSFileServiceImpl implements IFileService {

    @Autowired
    private OSS ossClient;
    @Autowired
    private OSSProperties ossProperties;

    @Override
    public FileDTO upload(MultipartFile file) {

        if (file.isEmpty() || Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf(".") == -1) {
            throw new ServiceException(ResultCode.OSS_UPLOAD_FAILED);
        }

        String pathPrefix = ossProperties.getPathPrefix();
        String originalFilename = file.getOriginalFilename();
        String originalFileExtName = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = UUID.randomUUID() + originalFileExtName;
        String objectName = pathPrefix + newFileName;
        String bucketName = ossProperties.getBucketName();

        try {
            InputStream inputStream = file.getInputStream();
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
            // 创建PutObject请求。
            PutObjectResult putObjectResult = ossClient.putObject(putObjectRequest);
            if (putObjectResult == null || StringUtils.isBlank(putObjectResult.getRequestId())) {
                log.error("OSS 上传文件返回结果与预期不一致！");
                throw new ServiceException(ResultCode.OSS_UPLOAD_FAILED);
            }
        } catch (Exception oe) {
            log.error("OSS 文件上传失败！");
            throw new ServiceException(ResultCode.OSS_UPLOAD_FAILED);
        }

        URL signedUrl = getSignedUrl(bucketName, objectName);
        FileDTO fileDTO = new FileDTO();
        fileDTO.setName(newFileName);
        fileDTO.setKey(objectName);
        fileDTO.setUrl(signedUrl.toString());
        return fileDTO;
    }

    @Override
    public OSSSignDTO getOOSSign() {
        String accessKeyId = ossProperties.getAccessKeyId();
        String accessKeySecret = ossProperties.getAccessKeySecret();
        String regionName = ossProperties.getRegionName();
        Instant now = Instant.now();

        OSSSignDTO ossSignDTO = new OSSSignDTO();
        ossSignDTO.setHost(ossProperties.getBaseUrl());
        ossSignDTO.setPathPrefix(ossProperties.getPathPrefix());
        try {
            // 步骤1：创建policy。
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> policy = new HashMap<>();

            // 定义过期时间，遵循 aliyun 默认格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(OSSCustomConstants.SIGN_EXPIRE_TIME_FORMAT)
                    .withZone(java.time.ZoneOffset.UTC);
            String signExpiredTime = formatter.format(now.plusSeconds(ossProperties.getSignExpiredTime()));
            policy.put("expiration", signExpiredTime);

            List<Object> conditions = new ArrayList<>();

            Map<String, String> bucketCondition = new HashMap<>();
            bucketCondition.put("bucket", ossProperties.getBucketName());
            conditions.add(bucketCondition);

            Map<String, String> signatureVersionCondition = new HashMap<>();
            signatureVersionCondition.put("x-oss-signature-version", "OSS4-HMAC-SHA256");
            conditions.add(signatureVersionCondition);

            Map<String, String> credentialCondition = new HashMap<>();
            formatter = DateTimeFormatter.ofPattern(OSSCustomConstants.SIGN_DATE_FORMAT).withZone(java.time.ZoneOffset.UTC);
            String signRequestDate = formatter.format(now);
            String credential = accessKeyId + "/" + signRequestDate + "/" + regionName + "/oss/aliyun_v4_request";
            credentialCondition.put("x-oss-credential", credential); // 替换为实际的 access key id
            conditions.add(credentialCondition);
            ossSignDTO.setOssCredential(credential);

            Map<String, String> dateCondition = new HashMap<>();
            formatter = DateTimeFormatter.ofPattern(OSSCustomConstants.SIGN_REQUEST_TIME_FORMAT);
            String ossTOT = formatter.format(now.atZone(ZoneOffset.UTC));
            dateCondition.put("x-oss-date", ossTOT);
            conditions.add(dateCondition);
            ossSignDTO.setOssDate(ossTOT);

            conditions.add(Arrays.asList("content-length-range", ossProperties.getMinLen(), ossProperties.getMaxLen()));
            conditions.add(Arrays.asList("eq", "$success_action_status", "200"));
            policy.put("conditions", conditions);

            String jsonPolicy = mapper.writeValueAsString(policy);

            // 步骤2：构造待签名字符串（StringToSign）。
            String stringToSign = new String(Base64.encodeBase64(jsonPolicy.getBytes()));
            ossSignDTO.setPolicy(stringToSign);

            // 步骤3：计算SigningKey。
            byte[] dateKey = hmacsha256(("aliyun_v4" + accessKeySecret).getBytes(), signRequestDate);
            byte[] dateRegionKey = hmacsha256(dateKey, regionName);
            byte[] dateRegionServiceKey = hmacsha256(dateRegionKey, "oss");
            byte[] signingKey = hmacsha256(dateRegionServiceKey, "aliyun_v4_request");

            // 步骤4：计算Signature。
            byte[] result = hmacsha256(signingKey, stringToSign);
            String signature = BinaryUtil.toHex(result);
            ossSignDTO.setSignature(signature);

            return ossSignDTO;
        } catch (Exception e) {
            throw new ServiceException(ResultCode.PRE_SIGN_URL_FAILED);
        }

    }


    private URL getSignedUrl(String bucketName, String objectName) {
        // 设置预签名URL过期时间，单位为毫秒。本示例以设置过期时间为1小时为例。
        Date expiration = new Date(new Date().getTime() + ossProperties.getSignExpiredTime() * 1000L);
        // 生成以GET方法访问的预签名URL。本示例没有额外请求头，其他人可以直接通过浏览器访问相关内容。
        return ossClient.generatePresignedUrl(bucketName, objectName, expiration);
    }

    private byte[] hmacsha256(byte[] key, String data) {
        try {
            // 初始化HMAC密钥规格，指定算法为HMAC-SHA256并使用提供的密钥。
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "HmacSHA256");
            // 获取Mac实例，并通过getInstance方法指定使用HMAC-SHA256算法。
            Mac mac = Mac.getInstance("HmacSHA256");
            // 使用密钥初始化Mac对象。
            mac.init(secretKeySpec);
            // 执行HMAC计算，通过doFinal方法接收需要计算的数据并返回计算结果的数组。
            byte[] hmacBytes = mac.doFinal(data.getBytes());

            return hmacBytes;
        } catch (Exception e) {
            throw new ServiceException(ResultCode.ERROR);
        }
    }
}
