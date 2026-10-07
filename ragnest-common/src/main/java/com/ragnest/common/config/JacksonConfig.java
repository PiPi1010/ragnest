package com.ragnest.common.config;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson 全局配置。
 *
 * <p>注意：Spring Boot 4 + Jackson 3 中，定制器接口从
 * {@code Jackson2ObjectMapperBuilderCustomizer} 变更为
 * {@link JsonMapperBuilderCustomizer}，且 Jackson 3 的核心类迁移到
 * {@code tools.jackson.databind} 包。</p>
 */
@Configuration
public class JacksonConfig {

    /**
     * 统一 Jackson 序列化/反序列化行为。
     *
     * <p>当前配置：</p>
     * <ul>
     *   <li>空 Bean 序列化不抛异常</li>
     *   <li>反序列化遇到未知属性不抛异常（向前兼容）</li>
     * </ul>
     */
    @Bean
    public JsonMapperBuilderCustomizer jsonMapperBuilderCustomizer() {
        return builder -> builder
                .disable(tools.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(tools.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }
}
