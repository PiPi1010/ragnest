package com.ragnest.ai.tool;

import com.ragnest.common.tenant.TenantContext;
import com.ragnest.core.model.Document;
import com.ragnest.core.model.KnowledgeBase;
import com.ragnest.core.repository.JpaDocumentRepository;
import com.ragnest.core.repository.JpaKnowledgeBaseRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.List;
import java.util.Optional;

/**
 * 知识库工具（Tool Calling）。
 *
 * <p>通过 {@link Tool} 注解将知识库、文档查询能力暴露给模型，供 Agent 调用。</p>
 *
 * <p>租户 ID 属于服务端上下文，由 {@link TenantContext} 提供，
 * 不作为模型参数（模型无从知晓租户 ID）。</p>
 */
public class KnowledgeBaseTools {

    private final JpaKnowledgeBaseRepository knowledgeBaseRepository;
    private final JpaDocumentRepository documentRepository;

    public KnowledgeBaseTools(JpaKnowledgeBaseRepository knowledgeBaseRepository,
                              JpaDocumentRepository documentRepository) {
        this.knowledgeBaseRepository = knowledgeBaseRepository;
        this.documentRepository = documentRepository;
    }

    @Tool(description = "根据知识库名称关键字搜索知识库，返回匹配的知识库列表")
    public List<KnowledgeBase> searchKnowledgeBase(@ToolParam(description = "知识库名称关键字") String name) {
        String queryName = name == null ? "" : name.replace(" ", "");
        return knowledgeBaseRepository.findByTenantId(TenantContext.getTenantId()).stream()
                .filter(kb -> kb.getName() != null && kb.getName().replace(" ", "").contains(queryName))
                .toList();
    }

    @Tool(description = "根据 ID 查询知识库详情")
    public Optional<KnowledgeBase> getKnowledgeBase(@ToolParam(description = "知识库 ID") Long id) {
        return knowledgeBaseRepository.findById(id);
    }

    @Tool(description = "列出当前租户下的所有知识库")
    public List<KnowledgeBase> listKnowledgeBases() {
        return knowledgeBaseRepository.findByTenantId(TenantContext.getTenantId());
    }

    @Tool(description = "查询某个知识库下的所有文档，返回文档名称、类型、切片数等信息")
    public List<DocumentInfo> listDocumentsByKnowledgeBase(
            @ToolParam(description = "知识库 ID") Long knowledgeBaseId) {
        return documentRepository.findByKnowledgeBaseId(knowledgeBaseId).stream()
                .map(doc -> new DocumentInfo(doc.getName(), doc.getFileType(), doc.getChunkCount(), doc.getStatus()))
                .toList();
    }

    @Tool(description = "根据知识库名称查询该知识库下的所有文档（内部会自动先按名称定位知识库，再查询其文档）")
    public List<DocumentInfo> listDocumentsByKnowledgeBaseName(
            @ToolParam(description = "知识库名称") String knowledgeBaseName) {
        // 先按名称找到知识库（忽略空格做匹配，提升容错）
        return knowledgeBaseRepository.findByTenantId(TenantContext.getTenantId()).stream()
                .filter(kb -> {
                    if (kb.getName() == null) {
                        return false;
                    }
                    String dbName = kb.getName().replace(" ", "");
                    String queryName = knowledgeBaseName == null ? "" : knowledgeBaseName.replace(" ", "");
                    return !queryName.isEmpty() && dbName.contains(queryName);
                })
                .findFirst()
                .map(kb -> documentRepository.findByKnowledgeBaseId(kb.getId()).stream()
                        .map(doc -> new DocumentInfo(doc.getName(), doc.getFileType(), doc.getChunkCount(), doc.getStatus()))
                        .toList())
                .orElse(List.of());
    }

    /**
     * 文档精简信息（暴露给模型的视图，避免返回内部字段）。
     */
    public record DocumentInfo(String name, String fileType, Integer chunkCount, Integer status) {
    }
}
