package com.ragnest.vector;

import org.springframework.ai.vectorstore.VectorStore;

/**
 * 向量库工厂接口。
 *
 * <p>抽象向量库的创建逻辑，支持多实现切换（pgvector / Milvus / Redis 等）。</p>
 */
public interface VectorStoreFactory {

    /**
     * 创建（或获取）向量库实例。
     *
     * @return VectorStore 实例
     */
    VectorStore create();

    /**
     * 向量库类型标识（如 pgvector、milvus、redis）。
     *
     * @return 类型标识
     */
    String type();
}