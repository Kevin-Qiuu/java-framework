package com.bitejiuyeke.biteadminservice.config.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "sys_argument")
public class SysArgument {

    /**
     * 参数 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 参数名
     */
    private String name;

    /**
     * 参数键
     */
    private String configKey;

    /**
     * 参数值
     */
    private String value;

    /**
     * 参数备注
     */
    private String remark;

}
