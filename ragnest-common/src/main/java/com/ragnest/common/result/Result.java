package com.ragnest.common.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回体。
 *
 * <p>所有 REST 接口统一返回此结构，字段约定：</p>
 * <ul>
 *   <li>{@code code}：业务状态码，0 表示成功，非 0 表示失败</li>
 *   <li>{@code message}：提示信息</li>
 *   <li>{@code data}：业务数据，可为 null</li>
 * </ul>
 *
 * @param <T> 业务数据类型
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 成功状态码 */
    public static final int SUCCESS_CODE = 0;

    /** 失败状态码 */
    public static final int ERROR_CODE = 500;

    /** 业务状态码 */
    private int code;

    /** 提示信息 */
    private String message;

    /** 业务数据 */
    private T data;

    public Result() {
    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /* ==================== 成功 ==================== */

    public static <T> Result<T> success() {
        return new Result<>(SUCCESS_CODE, "success", null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(SUCCESS_CODE, "success", data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(SUCCESS_CODE, message, data);
    }

    /* ==================== 失败 ==================== */

    public static <T> Result<T> error(String message) {
        return new Result<>(ERROR_CODE, message, null);
    }

    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    public static <T> Result<T> error(int code, String message, T data) {
        return new Result<>(code, message, data);
    }

    /* ==================== 判断 ==================== */

    public boolean isSuccess() {
        return this.code == SUCCESS_CODE;
    }
}
