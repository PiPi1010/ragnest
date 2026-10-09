package com.ragnest.tenant;

import com.ragnest.common.tenant.TenantContext;

/**
 * 租户数据隔离接口。
 *
 * <p>定义多租户数据隔离的通用能力，采用业界最通用的「共享表 + 租户列」方案：</p>
 * <ul>
 *   <li>所有租户数据存在同一张表，通过 {@code tenant_id} 列区分</li>
 *   <li>查询时自动追加租户过滤条件，实现逻辑隔离</li>
 * </ul>
 *
 * <p>具体实现（如 JPA/Hibernate 的拦截器、MyBatis 的插件）由实现层提供。</p>
 */
public interface TenantDataIsolation {

    /** 默认租户字段名 */
    String DEFAULT_TENANT_COLUMN = "tenant_id";

    /**
     * 获取租户字段名。
     *
     * @return 租户字段名
     */
    default String getTenantColumn() {
        return DEFAULT_TENANT_COLUMN;
    }

    /**
     * 获取当前租户 ID。
     *
     * @return 当前租户 ID
     */
    default String getCurrentTenantId() {
        return TenantContext.getTenantId();
    }

    /**
     * 判断是否启用租户隔离。
     *
     * @return true 表示启用
     */
    default boolean isEnabled() {
        return true;
    }
}
