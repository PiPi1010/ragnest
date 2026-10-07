package com.ragnest.common.exception;

import com.ragnest.common.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 全局异常处理器。
 *
 * <p>统一将各类异常转换为 {@link Result} 返回，避免异常细节直接暴露给调用方。</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 业务异常。
     */
    @ExceptionHandler(BizException.class)
    public Result<Void> handleBizException(BizException ex) {
        log.warn("业务异常: code={}, message={}", ex.getCode(), ex.getMessage());
        return Result.error(ex.getCode(), ex.getMessage());
    }

    /**
     * 参数校验异常（@RequestBody + @Valid）。
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = firstFieldErrorMessage(ex);
        return Result.error(CommonErrorCode.PARAM_INVALID.getCode(), message);
    }

    /**
     * 参数绑定异常（表单绑定）。
     */
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : CommonErrorCode.PARAM_INVALID.getMessage();
        return Result.error(CommonErrorCode.PARAM_INVALID.getCode(), message);
    }

    /**
     * 缺少请求参数。
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<Void> handleMissingParameter(MissingServletRequestParameterException ex) {
        return Result.error(CommonErrorCode.PARAM_INVALID.getCode(), "缺少参数: " + ex.getParameterName());
    }

    /**
     * 参数类型不匹配。
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Void> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return Result.error(CommonErrorCode.PARAM_INVALID.getCode(), "参数类型错误: " + ex.getName());
    }

    /**
     * 请求体无法解析（JSON 格式错误等）。
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Void> handleMessageNotReadable(HttpMessageNotReadableException ex) {
        return Result.error(CommonErrorCode.PARAM_INVALID.getCode(), "请求体格式错误");
    }

    /**
     * 请求方法不支持。
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Void> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return Result.error(CommonErrorCode.NOT_FOUND.getCode(), "请求方法不支持: " + ex.getMethod());
    }

    /**
     * 接口不存在。
     */
    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    public Result<Void> handleNotFound(Exception ex) {
        return Result.error(CommonErrorCode.NOT_FOUND.getCode(), CommonErrorCode.NOT_FOUND.getMessage());
    }

    /**
     * 兜底异常，避免堆栈细节泄露。
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception ex) {
        log.error("系统异常", ex);
        return Result.error(CommonErrorCode.INTERNAL_ERROR.getCode(), CommonErrorCode.INTERNAL_ERROR.getMessage());
    }

    private String firstFieldErrorMessage(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        return fieldError != null ? fieldError.getDefaultMessage() : CommonErrorCode.PARAM_INVALID.getMessage();
    }
}
