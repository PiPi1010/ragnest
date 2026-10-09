package com.ragnest.common.tenant;

/**
 * 租户上下文。
 *
 * <p>通过 {@link ThreadLocal} 持有当前请求的租户 ID，贯穿整个请求处理链路。</p>
 *
 * <p>典型用法：</p>
 * <pre>
 * try (var ignored = TenantContext.set(tenantId)) {
 *     // 业务逻辑，此处可通过 TenantContext.getTenantId() 获取租户
 * }
 * </pre>
 */
public final class TenantContext {

    /** 默认租户 ID（未指定租户时使用，通常表示系统级/超级租户） */
    public static final String DEFAULT_TENANT_ID = "default";

    private static final ThreadLocal<String> TENANT_HOLDER = new ThreadLocal<>();

    private TenantContext() {
    }

    /**
     * 设置当前租户 ID，返回可自动清理的句柄。
     *
     * @param tenantId 租户 ID
     * @return 自动清理句柄（try-with-resources 用）
     */
    public static TenantScope set(String tenantId) {
        TENANT_HOLDER.set(tenantId);
        return new TenantScope();
    }

    /**
     * 获取当前租户 ID，未设置时返回默认租户。
     *
     * @return 租户 ID
     */
    public static String getTenantId() {
        String tenantId = TENANT_HOLDER.get();
        return tenantId != null ? tenantId : DEFAULT_TENANT_ID;
    }

    /**
     * 清理租户上下文。
     */
    public static void clear() {
        TENANT_HOLDER.remove();
    }

    /**
     * 自动清理句柄。
     */
    public static final class TenantScope implements AutoCloseable {
        private TenantScope() {
        }

        @Override
        public void close() {
            TENANT_HOLDER.remove();
        }
    }
}
