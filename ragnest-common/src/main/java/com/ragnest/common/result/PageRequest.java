package com.ragnest.common.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页请求参数。
 *
 * <p>用于 Controller 接收分页查询参数。</p>
 */
@Data
public class PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 默认页码 */
    public static final long DEFAULT_PAGE = 1;

    /** 默认每页大小 */
    public static final long DEFAULT_SIZE = 10;

    /** 最大每页大小 */
    public static final long MAX_SIZE = 100;

    /** 当前页（从 1 开始） */
    private long page = DEFAULT_PAGE;

    /** 每页大小 */
    private long size = DEFAULT_SIZE;

    /** 归一化：确保 page >= 1、size 在合理区间 */
    public PageRequest normalize() {
        if (this.page < 1) {
            this.page = DEFAULT_PAGE;
        }
        if (this.size < 1) {
            this.size = DEFAULT_SIZE;
        }
        if (this.size > MAX_SIZE) {
            this.size = MAX_SIZE;
        }
        return this;
    }

    public long getOffset() {
        return (page - 1) * size;
    }
}
