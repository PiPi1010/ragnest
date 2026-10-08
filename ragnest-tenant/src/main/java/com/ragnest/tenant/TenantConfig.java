package com.ragnest.tenant;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 租户过滤器装配。
 */
@Configuration
public class TenantConfig {

    /**
     * 注册租户过滤器。
     *
     * <p>优先级设为较高（1），确保在安全过滤器链之前解析租户上下文。</p>
     */
    @Bean
    public FilterRegistrationBean<TenantFilter> tenantFilterRegistration() {
        FilterRegistrationBean<TenantFilter> registration = new FilterRegistrationBean<>(new TenantFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }
}
