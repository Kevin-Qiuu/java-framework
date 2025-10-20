package com.bitejiuyeke.biteadminapi.config.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ArgListReqDTO {

    @NotEmpty(message = "传入列表不能为空")
    private List<@NotBlank(message = "列表中的配置键不能为空白") String> configKeys;

}
