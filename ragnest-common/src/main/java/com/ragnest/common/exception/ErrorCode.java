package com.ragnest.common.exception;

/**
 * 错误码接口。
 *
 * <p>业务错误码统一实现此接口，便于在 {@link BizException} 与全局异常处理器中统一取值。</p>
 */
public interface ErrorCode {

    /** 错误码 */
    int getCode();

    /** 错误信息 */
    String getMessage();
}
