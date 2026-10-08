package com.ragnest.ai.tool;

import com.ragnest.core.model.KnowledgeBase;
import com.ragnest.core.repository.JpaKnowledgeBaseRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.List;
import java.util.Optional;

/**
 * 知识库工具（Tool Calling）。
 *
 * <p>通过 {@link Tool} 注解将知识库查询能力暴露给模型，供 Agent 调用。</p>
 */
public class KnowledgeBaseTools {

    private final JpaKnowledgeBaseRepository knowledgeBaseRepository;

    public KnowledgeBaseTools(JpaKnowledgeBaseRepository knowledgeBaseRepository) {
        this.knowledgeBaseRepository = knowledgeBaseRepository;
    }

    @Tool(description = "根据知识库名称搜索知识库，返回匹配的知识库列表")
    public List<KnowledgeBase> searchKnowledgeBase(@ToolParam(description = "知识库名称关键字") String name) {
        return knowledgeBaseRepository.findAll().stream()
                .filter(kb -> kb.getName() != null && kb.getName().contains(name))
                .toList();
    }

    @Tool(description = "根据 ID 查询知识库详情")
    public Optional<KnowledgeBase> getKnowledgeBase(@ToolParam(description = "知识库 ID") Long id) {
        return knowledgeBaseRepository.findById(id);
    }

    @Tool(description = "列出当前租户下的所有知识库")
    public List<KnowledgeBase> listKnowledgeBases(@ToolParam(description = "租户 ID") String tenantId) {
        return knowledgeBaseRepository.findByTenantId(tenantId);
    }
}
