package com.ragnest.core.service;

import com.ragnest.core.model.KnowledgeBase;

import java.util.List;
import java.util.Optional;

/**
 * 知识库领域服务接口。
 *
 * <p>定义知识库核心业务能力，具体实现由实现层提供。</p>
 */
public interface KnowledgeBaseService {

    /** 创建知识库 */
    KnowledgeBase create(KnowledgeBase knowledgeBase);

    /** 更新知识库 */
    KnowledgeBase update(KnowledgeBase knowledgeBase);

    /** 根据 ID 查询 */
    Optional<KnowledgeBase> findById(Long id);

    /** 查询某租户下所有知识库 */
    List<KnowledgeBase> listByTenant(String tenantId);

    /** 删除知识库 */
    void delete(Long id);
}
