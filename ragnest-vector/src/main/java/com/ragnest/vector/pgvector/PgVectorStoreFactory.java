package com.ragnest.vector.pgvector;

import com.ragnest.vector.VectorStoreFactory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * pgvector 向量库工厂。
 *
 * <p>基于 Spring AI 的 {@link PgVectorStore} 创建向量库，底层依赖 PostgreSQL 的
 * pgvector 扩展。</p>
 */
public class PgVectorStoreFactory implements VectorStoreFactory {

    public static final String TYPE = "pgvector";

    private final JdbcTemplate jdbcTemplate;
    private final EmbeddingModel embeddingModel;

    /** 向量维度（需与 Embedding 模型输出维度一致） */
    private final int dimensions;

    /** 向量表名 */
    private final String tableName;

    public PgVectorStoreFactory(JdbcTemplate jdbcTemplate,
                                EmbeddingModel embeddingModel,
                                int dimensions,
                                String tableName) {
        this.jdbcTemplate = jdbcTemplate;
        this.embeddingModel = embeddingModel;
        this.dimensions = dimensions;
        this.tableName = tableName;
    }

    @Override
    public VectorStore create() {
        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .dimensions(dimensions)
                .vectorTableName(tableName)
                .initializeSchema(true)
                .build();
    }

    @Override
    public String type() {
        return TYPE;
    }
}
