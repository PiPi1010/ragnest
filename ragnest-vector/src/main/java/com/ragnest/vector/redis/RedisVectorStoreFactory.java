package com.ragnest.vector.redis;

import com.ragnest.vector.VectorStoreFactory;
import org.springframework.ai.vectorstore.VectorStore;

/**
 * Redis 向量库工厂（可选实现，占位）。
 *
 * <p>适合已有 Redis 基础设施、低延迟检索场景。接入时需：</p>
 * <ol>
 *   <li>引入 {@code spring-ai-starter-vector-store-redis} 依赖</li>
 *   <li>配置 Redis 连接</li>
 *   <li>实现 {@link #create()} 返回 Redis 的 VectorStore</li>
 * </ol>
 *
 * <p>当前未接入，保留扩展点。</p>
 */
public class RedisVectorStoreFactory implements VectorStoreFactory {

    public static final String TYPE = "redis";

    @Override
    public VectorStore create() {
        // TODO: 接入 Redis 后实现
        throw new UnsupportedOperationException("Redis 向量库尚未接入");
    }

    @Override
    public String type() {
        return TYPE;
    }
}
