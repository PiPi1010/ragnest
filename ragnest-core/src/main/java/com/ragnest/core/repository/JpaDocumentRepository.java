package com.ragnest.core.repository;

import com.ragnest.core.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文档 JPA 仓储。
 */
@Repository
public interface JpaDocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByKnowledgeBaseId(Long knowledgeBaseId);

    List<Document> findByTenantId(String tenantId);
}
