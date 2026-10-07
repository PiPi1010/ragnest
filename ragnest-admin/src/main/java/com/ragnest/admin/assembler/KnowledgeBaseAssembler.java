package com.ragnest.admin.assembler;

import com.ragnest.admin.vo.KnowledgeBaseVO;
import com.ragnest.core.model.KnowledgeBase;

/**
 * 知识库装配器：领域模型与 VO 的相互转换。
 */
public final class KnowledgeBaseAssembler {

    private KnowledgeBaseAssembler() {
    }

    /**
     * 领域模型 → VO。
     */
    public static KnowledgeBaseVO toVO(KnowledgeBase kb) {
        if (kb == null) {
            return null;
        }
        KnowledgeBaseVO vo = new KnowledgeBaseVO();
        vo.setId(kb.getId());
        vo.setName(kb.getName());
        vo.setDescription(kb.getDescription());
        vo.setVectorDimension(kb.getVectorDimension());
        vo.setEmbeddingModel(kb.getEmbeddingModel());
        vo.setTenantId(kb.getTenantId());
        vo.setStatus(kb.getStatus());
        vo.setCreatedAt(kb.getCreatedAt());
        vo.setUpdatedAt(kb.getUpdatedAt());
        return vo;
    }
}
