package com.ragnest.core.repository;

import com.ragnest.core.model.KnowledgeBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 知识库 JPA 仓储。
 *
 * <p>继承 Spring Data JPA 的 {@link JpaRepository}，自动获得基础 CRUD 能力。
 * {@code findByTenantId} 由 Spring Data 按命名约定自动派生 SQL。</p>
 */
@Repository
public interface JpaKnowledgeBaseRepository extends JpaRepository<KnowledgeBase, Long> {

    List<KnowledgeBase> findByTenantId(String tenantId);
}
