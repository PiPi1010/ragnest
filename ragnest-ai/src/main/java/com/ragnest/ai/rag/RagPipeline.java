package com.ragnest.ai.rag;

import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.postretrieval.document.DocumentPostProcessor;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.retrieval.join.DocumentJoiner;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;

import java.util.List;
import java.util.Map;

/**
 * RAG 流水线编排服务。
 *
 * <p>将 Modular RAG 的四个阶段串联起来：</p>
 * <ol>
 *   <li>Query 转换（QueryTransformer）</li>
 *   <li>检索（DocumentRetriever）</li>
 *   <li>合并（DocumentJoiner）</li>
 *   <li>后处理（DocumentPostProcessor）</li>
 * </ol>
 *
 * <p>对应 Spring AI 2.0 内置的 Modular RAG 接口。</p>
 */
public class RagPipeline {

    private final QueryTransformer queryTransformer;
    private final List<DocumentRetriever> retrievers;
    private final DocumentJoiner documentJoiner;
    private final List<DocumentPostProcessor> postProcessors;

    public RagPipeline(QueryTransformer queryTransformer,
                       List<DocumentRetriever> retrievers,
                       DocumentJoiner documentJoiner,
                       List<DocumentPostProcessor> postProcessors) {
        this.queryTransformer = queryTransformer;
        this.retrievers = retrievers;
        this.documentJoiner = documentJoiner;
        this.postProcessors = postProcessors;
    }

    /**
     * 执行 RAG 检索，返回最终的相关文档列表。
     *
     * @param queryText 用户问题
     * @return 排序、去重、后处理后的文档列表
     */
    public List<Document> retrieve(String queryText) {
        Query query = new Query(queryText);

        // 1. Query 转换
        Query transformed = queryTransformer != null ? queryTransformer.transform(query) : query;

        // 2. 多路检索
        List<List<Document>> retrieved = retrievers.stream()
                .map(retriever -> retriever.retrieve(transformed))
                .toList();

        // 3. 合并
        List<Document> documents = documentJoiner.join(Map.of(transformed, retrieved));

        // 4. 后处理（去重、重排等）
        for (DocumentPostProcessor processor : postProcessors) {
            documents = processor.process(transformed, documents);
        }

        return documents;
    }
}
