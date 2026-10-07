package com.ragnest.core.repository;

import com.ragnest.core.model.Document;

import java.util.List;
import java.util.Optional;

/**
 * 文档仓储接口。
 */
public interface DocumentRepository {

    Document save(Document document);

    Optional<Document> findById(Long id);

    List<Document> findByKnowledgeBaseId(Long knowledgeBaseId);

    List<Document> findByTenantId(String tenantId);

    void deleteById(Long id);
}
