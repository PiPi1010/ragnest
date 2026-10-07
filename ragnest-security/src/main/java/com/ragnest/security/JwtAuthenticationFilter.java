package com.ragnest.security;

import com.ragnest.common.constant.Constants;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JWT 认证过滤器。
 *
 * <p>从请求头 {@code Authorization: Bearer xxx} 解析 JWT，校验通过后
 * 将认证信息写入 {@link SecurityContextHolder}。</p>
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(Constants.HEADER_AUTHORIZATION);

        if (header != null && header.startsWith(Constants.TOKEN_PREFIX)) {
            String token = header.substring(Constants.TOKEN_PREFIX.length());
            try {
                Claims claims = jwtService.parseToken(token);
                String subject = claims.getSubject();
                String tenantId = claims.get("tenantId", String.class);

                // TODO: 从数据库/缓存加载用户的角色权限，此处先用占位角色
                var authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
                var authentication = new UsernamePasswordAuthenticationToken(subject, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);
                request.setAttribute(Constants.HEADER_TENANT_ID, tenantId);
            } catch (Exception e) {
                // token 无效，不设置认证，交给后续安全链处理
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}
