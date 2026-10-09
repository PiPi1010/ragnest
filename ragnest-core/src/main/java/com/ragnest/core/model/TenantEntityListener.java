package com.ragnest.core.model;

import com.ragnest.common.tenant.TenantContext;
import jakarta.persistence.PrePersist;

/**
 * 租户实体监听器。
 *
 * <p>在 {@link TenantAware} 实体持久化前，若其 {@code tenantId} 为空，
 * 则自动从 {@link TenantContext} 填充当前租户 ID，实现多租户数据的统一写入。</p>
 */
public class TenantEntityListener {

    @PrePersist
    public void prePersist(Object entity) {
        if (entity instanceof TenantAware tenantAware && tenantAware.getTenantId() == null) {
            tenantAware.setTenantId(TenantContext.getTenantId());
        }
    }
}
