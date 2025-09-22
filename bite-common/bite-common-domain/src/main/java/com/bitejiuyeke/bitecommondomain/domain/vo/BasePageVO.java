package com.bitejiuyeke.bitecommondomain.domain.vo;

import com.bitejiuyeke.bitecommondomain.domain.dto.BasePageDTO;
import lombok.Data;

import java.util.List;

@Data
public class BasePageVO<T> {

    /**
     * 查询元素总数
     */
    private Integer totals;

    /**
     * 查询页数总数
     */
    private Integer totalPages;

    /**
     * 本次查询返回数据列表（当前页）
     */
    private List<T> list;

}
