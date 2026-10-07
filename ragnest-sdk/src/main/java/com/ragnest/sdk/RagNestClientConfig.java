package com.ragnest.sdk;

import lombok.Builder;
import lombok.Data;

/**
 * RagNestClient 配置。
 */
@Data
@Builder
public class RagNestClientConfig {

    /** 服务端地址（如 http://localhost:8080） */
    private String baseUrl;

    /** API Key（可选，走 API Key 认证时使用） */
    private String apiKey;

    /** JWT Token（可选，走 JWT 认证时使用） */
    private String token;

    /** 租户 ID */
    private String tenantId;

    /** 连接超时（毫秒） */
    @Builder.Default
    private int connectTimeoutMillis = 5000;

    /** 读取超时（毫秒） */
    @Builder.Default
    private int readTimeoutMillis = 30000;
}
