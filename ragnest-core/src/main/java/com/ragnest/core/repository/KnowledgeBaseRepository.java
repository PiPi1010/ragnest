package com.ragnest.core.repository;

import com.ragnest.core.model.KnowledgeBase;

import java.util.List;
import java.util.Optional;

/**
 * 知识库仓储接口。
 *
 * <p>只定义数据访问契约，具体实现（JPA 等）由实现层提供。</p>
 */
public interface KnowledgeBaseRepository {

    KnowledgeBase save(KnowledgeBase knowledgeBase);

    Optional<KnowledgeBase> findById(Long id);

    List<KnowledgeBase> findByTenantId(String tenantId);

    List<KnowledgeBase> findAll();

    void deleteById(Long id);
}
