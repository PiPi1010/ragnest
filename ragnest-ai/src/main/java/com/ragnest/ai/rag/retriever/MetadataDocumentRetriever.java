package com.ragnest.ai.rag.retriever;

import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;

import java.util.List;

/**
 * 基于元数据过滤的文档检索器。
 *
 * <p>在向量检索基础上叠加元数据过滤条件（如按租户、知识库过滤），
 * 实现检索范围隔离。</p>
 */
public class MetadataDocumentRetriever implements DocumentRetriever {

    private final VectorStore vectorStore;
    private final int topK;

    public MetadataDocumentRetriever(VectorStore vectorStore, int topK) {
        this.vectorStore = vectorStore;
        this.topK = topK;
    }

    @Override
    public List<Document> retrieve(Query query) {
        // 从 query.context() 中读取过滤条件（如 tenantId），此处演示按 metadata 过滤
        Object tenantId = query.context().get("tenantId");
        SearchRequest.Builder builder = SearchRequest.builder()
                .query(query.text())
                .topK(topK);

        if (tenantId != null) {
            Filter.Expression filter = new Filter.Expression(
                    Filter.ExpressionType.EQ,
                    new Filter.Key("tenantId"),
                    new Filter.Value(tenantId));
            builder.filterExpression(filter);
        }

        return vectorStore.similaritySearch(builder.build());
    }
}
