package com.bitejiuyeke.biteadminservice.map.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bitejiuyeke.bitecommoncore.domain.entity.BaseDO;
import lombok.Data;

@Data
@TableName("sys_region")
public class SysRegion extends BaseDO {


    /**
     * 区域名称
     */
    private String name;

    /**
     * 区域全称
     */
    private String fullName;

    /**
     * 父级区域列表
     */
    private Long parentId;

    /**
     * 区域名称拼音
     */
    private String pinyin;

    /**
     * 区域级别
     */
    private Integer level;

    /**
     * 区域经度
     */
    private Double longitude;

    /**
     * 区域纬度
     */
    private Double latitude;


}
