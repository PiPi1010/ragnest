package com.ragnest.common.exception;

import lombok.Getter;

/**
 * 业务异常。
 *
 * <p>业务代码抛出此异常，由全局异常处理器统一转换为 {@code Result} 返回。</p>
 */
@Getter
public class BizException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** 错误码 */
    private final int code;

    public BizException(String message) {
        super(message);
        this.code = CommonErrorCode.INTERNAL_ERROR.getCode();
    }

    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BizException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }
}
