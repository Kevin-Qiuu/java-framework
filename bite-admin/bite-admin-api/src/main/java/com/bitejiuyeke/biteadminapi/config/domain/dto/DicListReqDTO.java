package com.bitejiuyeke.biteadminapi.config.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class DicListReqDTO {

    @NotEmpty(message = "键值列表为空！")
    private List<@NotBlank(message = "键值列表元素有空值！") String> keys;

}
