package com.ragnest.core.config;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * JPA 配置。
 *
 * <p>启用审计功能（自动填充 {@code createdAt} / {@code updatedAt}），
 * 并指定 JPA 仓储与实体扫描路径，确保多模块下的
 * {@code Jpa*Repository} 接口与 {@code @Entity} 实体被正确扫描到。</p>
 */
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.ragnest")
@EntityScan(basePackages = "com.ragnest.core.model")
public class JpaConfig {
}
