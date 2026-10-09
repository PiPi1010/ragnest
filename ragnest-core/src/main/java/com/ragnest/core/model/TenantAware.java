package com.ragnest.core.model;

/**
 * 租户感知接口。
 *
 * <p>带有 {@code tenantId} 字段的实体实现此接口，由
 * {@code TenantEntityListener} 在持久化前自动从租户上下文填充租户 ID。</p>
 */
public interface TenantAware {

    String getTenantId();

    void setTenantId(String tenantId);
}
