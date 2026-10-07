package com.ragnest.common.constant;

/**
 * 全局常量。
 */
public final class Constants {

    private Constants() {
    }

    /** 请求头：租户 ID */
    public static final String HEADER_TENANT_ID = "X-Tenant-Id";

    /** 请求头：认证令牌 */
    public static final String HEADER_AUTHORIZATION = "Authorization";

    /** 认证令牌前缀 */
    public static final String TOKEN_PREFIX = "Bearer ";

    /** 时间格式 */
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    /** 默认时区 */
    public static final String DEFAULT_ZONE = "Asia/Shanghai";
}
