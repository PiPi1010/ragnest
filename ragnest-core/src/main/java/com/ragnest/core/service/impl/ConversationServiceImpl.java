package com.ragnest.core.service.impl;

import com.ragnest.core.model.Conversation;
import com.ragnest.core.model.Message;
import com.ragnest.core.repository.JpaConversationRepository;
import com.ragnest.core.service.ConversationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 会话领域服务实现。
 */
@Service
public class ConversationServiceImpl implements ConversationService {

    private final JpaConversationRepository conversationRepository;

    public ConversationServiceImpl(JpaConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    @Override
    @Transactional
    public Conversation create(Conversation conversation) {
        return conversationRepository.save(conversation);
    }

    @Override
    @Transactional
    public Conversation addMessage(Long conversationId, Message message) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("会话不存在: " + conversationId));
        message.setConversationId(conversationId);
        conversation.addMessage(message);
        return conversationRepository.save(conversation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> getHistory(Long conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("会话不存在: " + conversationId));
        return List.copyOf(conversation.getMessages());
    }

    @Override
    public Optional<Conversation> findById(Long id) {
        return conversationRepository.findById(id);
    }

    @Override
    public List<Conversation> listByTenant(String tenantId) {
        return conversationRepository.findByTenantId(tenantId);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        conversationRepository.deleteById(id);
    }
}
