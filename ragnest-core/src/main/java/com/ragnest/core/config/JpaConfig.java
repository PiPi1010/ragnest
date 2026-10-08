package com.ragnest.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA 配置。
 *
 * <p>启用审计功能，自动填充 {@code createdAt} / {@code updatedAt} 字段。</p>
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
