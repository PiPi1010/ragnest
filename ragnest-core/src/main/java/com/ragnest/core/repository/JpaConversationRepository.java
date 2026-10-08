package com.ragnest.core.repository;

import com.ragnest.core.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 会话 JPA 仓储。
 */
@Repository
public interface JpaConversationRepository extends JpaRepository<Conversation, Long> {

    List<Conversation> findByTenantId(String tenantId);

    List<Conversation> findByKnowledgeBaseId(Long knowledgeBaseId);
}
