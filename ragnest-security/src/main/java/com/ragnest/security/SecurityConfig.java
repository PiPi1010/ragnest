package com.ragnest.security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.DispatcherTypeRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.DispatcherType;
import java.util.List;

/**
 * 安全配置。
 *
 * <p>Spring Security 7（Boot 4）使用强制的 Lambda DSL 配置方式：</p>
 * <ul>
 *   <li>{@code authorizeHttpRequests} + {@code requestMatchers}（不再有 and() 链式）</li>
 *   <li>{@link SecurityFilterChain} bean 方式</li>
 *   <li>{@code @EnableMethodSecurity} 启用方法级权限</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {

    @Bean
    public JwtService jwtService(JwtProperties properties) {
        return new JwtService(properties.getSecret(), properties.getExpiration());
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtService jwtService) {
        return new JwtAuthenticationFilter(jwtService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                // 无状态 JWT，关闭 CSRF
                .csrf(csrf -> csrf.disable())
                // 允许前端跨域（dev: Vite 5173）
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // ASYNC dispatch（响应式流式返回触发）跳过授权检查，
                        // 避免流式输出过程中二次授权导致 Access Denied + chunked 截断
                        .requestMatchers(new DispatcherTypeRequestMatcher(DispatcherType.ASYNC)).permitAll()
                        // 公开端点（认证、健康检查、前端静态资源）
                        .requestMatchers("/api/auth/**", "/actuator/health").permitAll()
                        .requestMatchers("/", "/index.html", "/static/**", "/*.html",
                                "/*.css", "/*.js", "/*.ico", "/*.png", "/*.svg").permitAll()
                        // 管理端需要 ADMIN 角色
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        // 其余默认需认证
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    /**
     * 密码编码器（Argon2，Spring Security 5.8+ 推荐默认参数）。
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

    /**
     * CORS 配置：允许前端跨域访问（开发阶段放开 localhost 任意端口）。
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("http://localhost:*", "http://127.0.0.1:*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
