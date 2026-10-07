package com.ragnest.vector.milvus;

import com.ragnest.vector.VectorStoreFactory;
import org.springframework.ai.vectorstore.VectorStore;

/**
 * Milvus 向量库工厂（可选实现，占位）。
 *
 * <p>Milvus 适合大规模、高性能向量检索场景。接入时需：</p>
 * <ol>
 *   <li>引入 {@code spring-ai-starter-vector-store-milvus} 依赖</li>
 *   <li>配置 Milvus 连接（host/port 等）</li>
 *   <li>实现 {@link #create()} 返回 Milvus 的 VectorStore</li>
 * </ol>
 *
 * <p>当前未接入，保留扩展点。</p>
 */
public class MilvusVectorStoreFactory implements VectorStoreFactory {

    public static final String TYPE = "milvus";

    @Override
    public VectorStore create() {
        // TODO: 接入 Milvus 后实现
        throw new UnsupportedOperationException("Milvus 向量库尚未接入");
    }

    @Override
    public String type() {
        return TYPE;
    }
}
