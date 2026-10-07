package com.ragnest.tenant;

import com.ragnest.common.constant.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 租户过滤器。
 *
 * <p>从请求头 {@code X-Tenant-Id} 解析租户 ID 并放入 {@link TenantContext}，
 * 请求结束后自动清理，避免 ThreadLocal 泄漏。</p>
 */
public class TenantFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String tenantId = request.getHeader(Constants.HEADER_TENANT_ID);
        try (TenantContext.TenantScope ignored = TenantContext.set(tenantId)) {
            filterChain.doFilter(request, response);
        }
    }
}
