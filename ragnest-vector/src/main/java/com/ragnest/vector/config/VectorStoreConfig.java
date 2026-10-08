package com.ragnest.vector.config;

import com.ragnest.vector.pgvector.PgVectorStoreFactory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 向量库装配。
 *
 * <p>基于 pgvector 创建 {@link VectorStore} bean，供 RAG 检索与文档入库使用。</p>
 */
@Configuration
public class VectorStoreConfig {

    @Bean
    public VectorStore vectorStore(JdbcTemplate jdbcTemplate,
                                   EmbeddingModel embeddingModel,
                                   @Value("${ragnest.vector.dimensions:1024}") int dimensions,
                                   @Value("${ragnest.vector.table-name:vector_store}") String tableName) {
        return new PgVectorStoreFactory(jdbcTemplate, embeddingModel, dimensions, tableName).create();
    }
}
