package com.bitejiuyeke.biteadminservice.config.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "sys_dictionary_type")
public class SysDictionaryType {

    /**
     * 字典键 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 字典类型键
     */
    private String typeKey;

    /**
     * 字典类型值
     */
    private String value;

    /**
     * 备注：当字典类型值无法解释清楚可采用备注
     */
    private String remark;

    /**
     * 字典类型状态：1 正常 0 停用
     */
    private Integer status;

}
