package com.bitejiuyeke.bitefileapi.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class FileUploadDTO implements Serializable {

    /**
     * 上传文件
     */
    @NotNull(message = "上传文件不可为空！")
    private MultipartFile multipartFile;

    /**
     * 上传文件前缀
     */
    @NotBlank(message = "上传文件前缀不可为空！")
    private String filePrefix;

}
