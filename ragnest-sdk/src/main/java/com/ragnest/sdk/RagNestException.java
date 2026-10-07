package com.ragnest.sdk;

/**
 * RagNest SDK 异常。
 *
 * <p>封装 SDK 调用过程中的所有异常。</p>
 */
public class RagNestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RagNestException(String message) {
        super(message);
    }

    public RagNestException(String message, Throwable cause) {
        super(message, cause);
    }
}
