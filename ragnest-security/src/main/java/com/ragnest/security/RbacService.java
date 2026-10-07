package com.ragnest.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * RBAC 权限服务。
 *
 * <p>基于角色的访问控制（Role-Based Access Control），提供权限判断能力，
 * 供 {@code @PreAuthorize} 或业务代码调用。</p>
 */
public class RbacService {

    /**
     * 判断当前用户是否拥有指定角色。
     *
     * @param role 角色名（如 ADMIN、USER）
     * @return true 表示拥有
     */
    public boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        String targetRole = "ROLE_" + role.toUpperCase();
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(targetRole::equals);
    }

    /**
     * 获取当前用户名。
     *
     * @return 当前用户名，未认证返回 null
     */
    public String currentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : null;
    }
}
