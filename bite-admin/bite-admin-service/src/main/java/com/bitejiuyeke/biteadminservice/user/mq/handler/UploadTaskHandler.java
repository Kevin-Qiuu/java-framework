package com.bitejiuyeke.biteadminservice.user.mq.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bitejiuyeke.biteadminservice.user.constants.MqTaskTypeConstant;
import com.bitejiuyeke.biteadminservice.user.domain.entity.AppUser;
import com.bitejiuyeke.biteadminservice.user.domain.entity.AppUserNoOpenId;
import com.bitejiuyeke.biteadminservice.user.domain.entity.Encrypt;
import com.bitejiuyeke.biteadminservice.user.mapper.AppUserMapper;
import com.bitejiuyeke.bitecommoncore.utils.ExcelUtils;
import com.bitejiuyeke.bitecommoncore.utils.StringUtil;
import com.bitejiuyeke.bitecommoncore.utils.VerifyUtil;
import com.bitejiuyeke.bitecommonrabbitmq.domain.annotation.MqTaskType;
import com.bitejiuyeke.bitecommonrabbitmq.handler.TaskHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.BatchResult;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Component
@Slf4j
@MqTaskType(MqTaskTypeConstant.UPLOAD_APP_USER)
public class UploadTaskHandler implements TaskHandler<Boolean> {

    @Autowired
    private AppUserMapper appUserMapper;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 默认头像 url 地址
     */
    @Value("${app-user.info.default-avatar}")
    private String defaultAvatar;

    @Override
    public Boolean handleTask(String excelUrl) {

        if (StringUtils.isEmpty(excelUrl)) {
            return false;
        }

        // 下载文件（默认下载至临时文件中）
        String tempDir = System.getProperty("java.io.tmpdir");
        String savePath = Paths.get(tempDir, StringUtil.generateRandomStr(15) + ".xlsx").toString();
        boolean downloadSuccess = downloadFile(excelUrl, savePath);
        if (!downloadSuccess) {
            return false;
        }
        File file = new File(savePath);
        MultipartFile multipartFile = null;
        try (FileInputStream input = new FileInputStream(file)) {
            multipartFile = new MockMultipartFile(
                    file.getName(), // 文件名
                    file.getName(), // 原始文件名
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // 文件类型
                    input // 文件内容
            );
            // 判断文件
            List<AppUserNoOpenId> appUsers = ExcelUtils.toList(multipartFile, AppUserNoOpenId.class);
            if (appUsers == null || appUsers.isEmpty()) {
                return false;
            }
            // 执行 sql
            int num = uploadAppUserList(appUsers);
            log.info("已上传{}名用户", num);
        } catch (Exception ignored) {
            log.error("用户上传失败！");
            return false;
        } finally {
            // 确保最后删除文件
            if (multipartFile != null) {
                File tempFile = new File(savePath);
                try {
                    Files.deleteIfExists(tempFile.toPath());
                    log.info("临时文件已删除: {}", tempFile.toPath());
                } catch (IOException e) {
                    log.error("临时文件删除失败: {}", tempFile.toPath());
                }
            }
        }

        return true;
    }

    public boolean downloadFile(String fileUrl, String savePath) {
        // 已经带签名的完整 URL，不要让它再编码，因为 RestTemplate 的编码器会把整个 QueryString 再做一次百分号转义（+→%2B、%2B→%252B…）。
        // OSS 收到请求后，用被转义后的 QueryString 重新算签名，发现和 URL 里带的那个 原始签名 对不上 → 403。
        // 所以直接将方法参数中的 url 视为最终的 url 参数
        ResponseEntity<Resource> response = null;
        try {
            URL url = new URL(fileUrl);
            RequestEntity<?> request = RequestEntity.get(url.toURI()).build();
            response = restTemplate.exchange(request, Resource.class);
        } catch (Exception e) {
            log.error("", e);
        }
        if (response == null || response.getBody() == null) {
            return false;
        }
        try (InputStream inputStream = response.getBody().getInputStream()) {
            // 确保保存路径的目录存在
            File saveFile = new File(savePath);
            saveFile.getParentFile().mkdirs();
            Files.copy(inputStream, Paths.get(savePath), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private int uploadAppUserList(List<AppUserNoOpenId> appUserNoEncrypts) {
        if (appUserNoEncrypts == null || appUserNoEncrypts.isEmpty()) {
            return 0;
        }

        // 过滤不符合电话规则的记录
        List<AppUserNoOpenId> appUsers = appUserNoEncrypts.stream().filter(appUserNoOpenId -> VerifyUtil.checkMobile(appUserNoOpenId.getPhoneNumber())).toList();

        // 查询已存在的电话记录
        LambdaQueryWrapper<AppUser> queryWrapper = new LambdaQueryWrapper<AppUser>();
        List<String> existPhoneNumber = appUserMapper.selectList(queryWrapper.select(AppUser::getPhoneNumber)
                        .in(AppUser::getPhoneNumber, appUsers.stream().map(appUserNoOpenId -> new Encrypt(appUserNoOpenId.getPhoneNumber())).toList()))
                        .stream().map(appUser -> appUser.getPhoneNumber().getValue()).toList();

        List<AppUser> appUsersNeedToInsert = appUsers.stream().filter(appUser -> !existPhoneNumber.contains(appUser.getPhoneNumber()))
                .map(appUser -> {
                    AppUser appUser1 = new AppUser();
                    String nickName = appUser.getNickName();
                    String avatar = appUser.getAvatar();
                    appUser1.setNickName(StringUtils.isEmpty(nickName) ? "bite-" + StringUtil.generateRandomStr(15) : nickName);
                    appUser1.setAvatar(StringUtils.isEmpty(avatar) ? defaultAvatar : avatar);
                    appUser1.setPhoneNumber(new Encrypt(appUser.getPhoneNumber()));
                    return appUser1;
                }).toList();

        if (appUsersNeedToInsert.isEmpty()) {
            return 0;
        }

        List<BatchResult> insertedResults = appUserMapper.insert(appUsersNeedToInsert);
        int totalInsertCount = 0;
        for (BatchResult batchResult : insertedResults) {
            int[] updateCounts = batchResult.getUpdateCounts();
            for (int updateCount : updateCounts) {
                if (updateCount > 0) {
                    totalInsertCount += updateCount;
                }
            }
        }
        return totalInsertCount;
    }

}
