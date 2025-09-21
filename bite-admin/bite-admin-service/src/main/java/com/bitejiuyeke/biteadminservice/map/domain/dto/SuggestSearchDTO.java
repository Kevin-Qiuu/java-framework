package com.bitejiuyeke.biteadminservice.map.domain.dto;

import lombok.Data;

@Data
public class SuggestSearchDTO {

    /**
     * 区域名称（限制城市）
     */
    private String region;

    /**
     * 搜索关键词
     */
    private String keyword;

    /**
     * 0：不限制当前城市，会召回其他城市的 poi
     * 1：[默认] 仅限制在当前城市，
     */
    private Integer region_fix = 1;

}
