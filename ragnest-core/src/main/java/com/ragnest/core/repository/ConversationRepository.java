package com.ragnest.core.repository;

import com.ragnest.core.model.Conversation;

import java.util.List;
import java.util.Optional;

/**
 * 会话仓储接口。
 */
public interface ConversationRepository {

    Conversation save(Conversation conversation);

    Optional<Conversation> findById(Long id);

    List<Conversation> findByTenantId(String tenantId);

    List<Conversation> findByKnowledgeBaseId(Long knowledgeBaseId);

    void deleteById(Long id);
}
