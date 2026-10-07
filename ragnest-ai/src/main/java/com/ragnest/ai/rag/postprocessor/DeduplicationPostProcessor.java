package com.ragnest.ai.rag.postprocessor;

import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.postretrieval.document.DocumentPostProcessor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 文档去重后处理器。
 *
 * <p>按文档 ID 去重，保留首次出现的文档，维持原有顺序。</p>
 */
public class DeduplicationPostProcessor implements DocumentPostProcessor {

    @Override
    public List<Document> process(Query query, List<Document> documents) {
        Map<String, Document> seen = new LinkedHashMap<>();
        for (Document doc : documents) {
            String id = doc.getId();
            if (id != null && !id.isBlank()) {
                seen.putIfAbsent(id, doc);
            }
        }
        return new ArrayList<>(seen.values());
    }
}
