package com.ragnest.core.service.impl;

import com.ragnest.core.model.KnowledgeBase;
import com.ragnest.core.repository.JpaKnowledgeBaseRepository;
import com.ragnest.core.service.KnowledgeBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 知识库领域服务实现。
 */
@Service
public class KnowledgeBaseServiceImpl implements KnowledgeBaseService {

    private final JpaKnowledgeBaseRepository knowledgeBaseRepository;

    public KnowledgeBaseServiceImpl(JpaKnowledgeBaseRepository knowledgeBaseRepository) {
        this.knowledgeBaseRepository = knowledgeBaseRepository;
    }

    @Override
    @Transactional
    public KnowledgeBase create(KnowledgeBase knowledgeBase) {
        if (knowledgeBase.getStatus() == null) {
            knowledgeBase.setStatus(0);
        }
        return knowledgeBaseRepository.save(knowledgeBase);
    }

    @Override
    @Transactional
    public KnowledgeBase update(KnowledgeBase knowledgeBase) {
        return knowledgeBaseRepository.save(knowledgeBase);
    }

    @Override
    public Optional<KnowledgeBase> findById(Long id) {
        return knowledgeBaseRepository.findById(id);
    }

    @Override
    public List<KnowledgeBase> listByTenant(String tenantId) {
        return knowledgeBaseRepository.findByTenantId(tenantId);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        knowledgeBaseRepository.deleteById(id);
    }
}
