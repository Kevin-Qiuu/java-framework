package com.bitejiuyeke.bitecommondomain.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BasePageReqDTO implements Serializable {

    /**
     * 查询页下标
     */
    private Integer pageNo = 1;

    /**
     * 单页元素总数
     */
    private Integer pageSize = 10;

    /**
     * 获取偏移量
     * @return 当前页的第一项在 list 中的下标
     */
    public Integer getOffset() {
        return (pageNo - 1) * pageSize;
    }

}
