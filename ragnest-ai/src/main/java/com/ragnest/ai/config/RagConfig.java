package com.ragnest.ai.config;

import com.ragnest.ai.advisor.RagAdvisor;
import com.ragnest.ai.rag.RagPipeline;
import com.ragnest.ai.rag.postprocessor.DeduplicationPostProcessor;
import com.ragnest.ai.rag.postprocessor.RerankPostProcessor;
import com.ragnest.ai.rag.retriever.VectorStoreDocumentRetriever;
import com.ragnest.ai.rag.transformer.NoopQueryTransformer;
import com.ragnest.ai.tool.KnowledgeBaseTools;
import com.ragnest.core.repository.JpaDocumentRepository;
import com.ragnest.core.repository.JpaKnowledgeBaseRepository;
import org.springframework.ai.rag.postretrieval.document.DocumentPostProcessor;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.retrieval.join.ConcatenationDocumentJoiner;
import org.springframework.ai.rag.retrieval.join.DocumentJoiner;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * RAG 链路装配。
 *
 * <p>装配 Modular RAG 的四个阶段组件，串联成 {@link RagPipeline}，
 * 并注册 {@link RagAdvisor} 与 {@link KnowledgeBaseTools}。</p>
 */
@Configuration
public class RagConfig {

    @Bean
    public QueryTransformer queryTransformer() {
        return new NoopQueryTransformer();
    }

    @Bean
    public List<DocumentRetriever> documentRetrievers(VectorStore vectorStore,
                                                      @Value("${ragnest.rag.top-k:5}") int topK) {
        return List.of(new VectorStoreDocumentRetriever(vectorStore, topK));
    }

    @Bean
    public DocumentJoiner documentJoiner() {
        return new ConcatenationDocumentJoiner();
    }

    @Bean
    public List<DocumentPostProcessor> documentPostProcessors() {
        return List.of(new DeduplicationPostProcessor(), new RerankPostProcessor());
    }

    @Bean
    public RagPipeline ragPipeline(QueryTransformer queryTransformer,
                                   List<DocumentRetriever> retrievers,
                                   DocumentJoiner documentJoiner,
                                   List<DocumentPostProcessor> postProcessors) {
        return new RagPipeline(queryTransformer, retrievers, documentJoiner, postProcessors);
    }

    @Bean
    public RagAdvisor ragAdvisor(RagPipeline ragPipeline) {
        return new RagAdvisor(ragPipeline);
    }

    @Bean
    public KnowledgeBaseTools knowledgeBaseTools(JpaKnowledgeBaseRepository knowledgeBaseRepository,
                                                 JpaDocumentRepository documentRepository) {
        return new KnowledgeBaseTools(knowledgeBaseRepository, documentRepository);
    }
}
