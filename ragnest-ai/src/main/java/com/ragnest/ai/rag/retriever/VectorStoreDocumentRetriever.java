package com.ragnest.ai.rag.retriever;

import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.List;

/**
 * 基于向量库的文档检索器。
 *
 * <p>将 Query 向量化后在 VectorStore 中做相似度检索。</p>
 */
public class VectorStoreDocumentRetriever implements DocumentRetriever {

    private final VectorStore vectorStore;
    private final int topK;

    public VectorStoreDocumentRetriever(VectorStore vectorStore, int topK) {
        this.vectorStore = vectorStore;
        this.topK = topK;
    }

    @Override
    public List<Document> retrieve(Query query) {
        return vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query.text())
                        .topK(topK)
                        .build());
    }
}
