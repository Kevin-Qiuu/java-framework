package com.bitejiuyeke.bitecommondomain.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class BaseDO {

    /**
     * 表 id ，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

}
