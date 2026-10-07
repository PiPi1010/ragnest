package com.ragnest.common.exception;

/**
 * 通用错误码枚举。
 *
 * <p>错误码分段约定（预留扩展空间）：</p>
 * <ul>
 *   <li>1xxx：通用错误</li>
 *   <li>2xxx：知识库相关</li>
 *   <li>3xxx：文档相关</li>
 *   <li>4xxx：AI / 对话相关</li>
 *   <li>5xxx：租户 / 权限相关</li>
 * </ul>
 */
public enum CommonErrorCode implements ErrorCode {

    /** 系统内部错误 */
    INTERNAL_ERROR(1000, "系统内部错误"),

    /** 参数校验失败 */
    PARAM_INVALID(1001, "参数校验失败"),

    /** 资源不存在 */
    NOT_FOUND(1002, "资源不存在"),

    /** 请求过于频繁 */
    TOO_MANY_REQUESTS(1003, "请求过于频繁"),

    /** 未认证 */
    UNAUTHORIZED(5001, "未认证或登录已过期"),

    /** 无权限 */
    FORBIDDEN(5002, "无权限访问");

    private final int code;
    private final String message;

    CommonErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
