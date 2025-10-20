package com.bitejiuyeke.biteadminservice.config.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bitejiuyeke.bitecommondomain.domain.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName(value = "sys_dictionary_data")
public class SysDictionaryData {

    /**
     * 字典数据的 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 字典数据的类型键
     */
    private String typeKey;

    /**
     * 字典数据的键
     */
    private String dataKey;

    /**
     * 字典数据的值
     */
    private String value;

    /**
     * 字典数据的备注
     */
    private String remark;

    /**
     * 字典数据的排序序号
     */
    private Integer sort;

    /**
     * 字典数据的状态：【默认】 1 表示启用，0 表示禁用
     */
    private Integer status;

}
