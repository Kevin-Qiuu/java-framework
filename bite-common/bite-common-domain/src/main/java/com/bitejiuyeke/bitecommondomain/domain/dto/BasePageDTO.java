package com.bitejiuyeke.bitecommondomain.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class BasePageDTO<T> {

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

    /**
     * 计算总页数
     *
     * @param totals 总数量
     * @param pageSize 页大小
     * @return 页数
     */
    public static int calculateTotalPages(long totals, int pageSize) {
        if (pageSize <= 0) {
            throw new IllegalArgumentException("Page size must be greater than 0.");
        }
        return (int) Math.ceil((double) totals / pageSize);
    }

}
