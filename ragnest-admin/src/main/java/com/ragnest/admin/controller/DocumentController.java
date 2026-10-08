package com.ragnest.admin.controller;

import com.ragnest.admin.assembler.DocumentAssembler;
import com.ragnest.admin.vo.DocumentVO;
import com.ragnest.common.exception.BizException;
import com.ragnest.common.exception.CommonErrorCode;
import com.ragnest.common.result.Result;
import com.ragnest.core.model.Document;
import com.ragnest.core.repository.JpaDocumentRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文档管理接口。
 */
@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final JpaDocumentRepository documentRepository;

    public DocumentController(JpaDocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    /**
     * 查询文档详情。
     */
    @GetMapping("/{id}")
    public Result<DocumentVO> getById(@PathVariable Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new BizException(CommonErrorCode.NOT_FOUND));
        return Result.success(DocumentAssembler.toVO(document));
    }

    /**
     * 查询某知识库下的文档列表。
     */
    @GetMapping
    public Result<List<DocumentVO>> list(@RequestParam Long knowledgeBaseId) {
        List<DocumentVO> vos = documentRepository.findByKnowledgeBaseId(knowledgeBaseId).stream()
                .map(DocumentAssembler::toVO)
                .toList();
        return Result.success(vos);
    }

    /**
     * 删除文档。
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        documentRepository.deleteById(id);
        return Result.success();
    }
}
