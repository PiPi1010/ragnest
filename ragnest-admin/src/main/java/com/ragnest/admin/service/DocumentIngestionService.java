package com.ragnest.admin.service;

import com.ragnest.core.model.Chunk;
import com.ragnest.core.model.Document;
import com.ragnest.core.repository.JpaChunkRepository;
import com.ragnest.core.repository.JpaDocumentRepository;
import com.ragnest.parser.pipeline.DocumentIngestionPipeline;
import com.ragnest.tenant.TenantContext;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文档入库服务。
 *
 * <p>串联「解析 → 分块 → 向量化写库 → 持久化元数据」的完整闭环。</p>
 */
@Service
public class DocumentIngestionService {

    private final DocumentIngestionPipeline ingestionPipeline;
    private final VectorStore vectorStore;
    private final JpaDocumentRepository documentRepository;
    private final JpaChunkRepository chunkRepository;

    public DocumentIngestionService(DocumentIngestionPipeline ingestionPipeline,
                                    VectorStore vectorStore,
                                    JpaDocumentRepository documentRepository,
                                    JpaChunkRepository chunkRepository) {
        this.ingestionPipeline = ingestionPipeline;
        this.vectorStore = vectorStore;
        this.documentRepository = documentRepository;
        this.chunkRepository = chunkRepository;
    }

    /**
     * 入库一个文档文件。
     *
     * @param knowledgeBaseId 目标知识库 ID
     * @param file            上传的文件
     * @return 持久化后的文档实体
     */
    @Transactional
    public Document ingest(Long knowledgeBaseId, MultipartFile file) {
        String filename = file.getOriginalFilename();
        String fileType = StringUtils.getFilenameExtension(filename);

        // 1. 解析并分块
        List<org.springframework.ai.document.Document> chunks;
        try {
            chunks = ingestionPipeline.ingest(file.getInputStream(), filename, fileType);
        } catch (Exception e) {
            throw new IllegalArgumentException("文档解析失败: " + filename, e);
        }

        // 2. 补元数据（知识库 ID、租户 ID）
        String tenantId = TenantContext.getTenantId();
        for (org.springframework.ai.document.Document chunk : chunks) {
            chunk.getMetadata().put("knowledgeBaseId", knowledgeBaseId);
            chunk.getMetadata().put("tenantId", tenantId);
        }

        // 3. 向量化并写入向量库
        vectorStore.add(chunks);

        // 4. 持久化文档元数据
        Document document = Document.builder()
                .knowledgeBaseId(knowledgeBaseId)
                .name(filename)
                .fileType(fileType)
                .status(2) // 2 = 已完成
                .chunkCount(chunks.size())
                .tenantId(tenantId)
                .build();
        document = documentRepository.save(document);

        // 5. 持久化切片
        int seq = 0;
        for (org.springframework.ai.document.Document chunk : chunks) {
            Chunk entity = Chunk.builder()
                    .documentId(document.getId())
                    .sequence(seq++)
                    .content(chunk.getText())
                    .metadata(chunk.getMetadata().toString())
                    .tenantId(tenantId)
                    .build();
            chunkRepository.save(entity);
        }

        return document;
    }
}
