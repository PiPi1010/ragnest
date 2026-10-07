package com.ragnest.admin.assembler;

import com.ragnest.admin.vo.DocumentVO;
import com.ragnest.core.model.Document;

/**
 * 文档装配器：领域模型与 VO 的相互转换。
 */
public final class DocumentAssembler {

    private DocumentAssembler() {
    }

    /**
     * 领域模型 → VO。
     */
    public static DocumentVO toVO(Document document) {
        if (document == null) {
            return null;
        }
        DocumentVO vo = new DocumentVO();
        vo.setId(document.getId());
        vo.setKnowledgeBaseId(document.getKnowledgeBaseId());
        vo.setName(document.getName());
        vo.setFileType(document.getFileType());
        vo.setStatus(document.getStatus());
        vo.setChunkCount(document.getChunkCount());
        vo.setTenantId(document.getTenantId());
        vo.setCreatedAt(document.getCreatedAt());
        vo.setUpdatedAt(document.getUpdatedAt());
        return vo;
    }
}
