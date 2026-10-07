package com.ragnest.common.result;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * 分页返回体。
 *
 * @param <T> 列表元素类型
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 当前页（从 1 开始） */
    private long page;

    /** 每页大小 */
    private long size;

    /** 总记录数 */
    private long total;

    /** 总页数 */
    private long totalPages;

    /** 当前页数据 */
    private List<T> records;

    public PageResult() {
        this.records = Collections.emptyList();
    }

    public PageResult(long page, long size, long total, List<T> records) {
        this.page = page;
        this.size = size;
        this.total = total;
        this.totalPages = size > 0 ? (total + size - 1) / size : 0;
        this.records = records != null ? records : Collections.emptyList();
    }

    /** 类型转换：将 List<T> 映射为 List<R> */
    public <R> PageResult<R> map(Function<T, R> mapper) {
        List<R> mapped = this.records.stream().map(mapper).toList();
        return new PageResult<>(this.page, this.size, this.total, mapped);
    }
}
