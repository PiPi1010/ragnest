package com.ragnest.ai.rag.postprocessor;

import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.postretrieval.document.DocumentPostProcessor;

import java.util.Comparator;
import java.util.List;

/**
 * 重排后处理器（占位实现）。
 *
 * <p>当前按文档 score 降序重排。生产环境可替换为专门的 Rerank 模型
 * （如 bge-reranker）做语义重排，只需替换本实现即可。</p>
 */
public class RerankPostProcessor implements DocumentPostProcessor {

    @Override
    public List<Document> process(Query query, List<Document> documents) {
        return documents.stream()
                .sorted(Comparator.comparing(
                        doc -> doc.getScore() == null ? 0.0 : doc.getScore(),
                        Comparator.reverseOrder()))
                .toList();
    }
}