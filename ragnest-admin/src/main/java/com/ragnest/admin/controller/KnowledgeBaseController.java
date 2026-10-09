package com.ragnest.admin.controller;

import com.ragnest.admin.assembler.KnowledgeBaseAssembler;
import com.ragnest.admin.dto.KnowledgeBaseCreateRequest;
import com.ragnest.admin.dto.KnowledgeBaseUpdateRequest;
import com.ragnest.admin.vo.KnowledgeBaseVO;
import com.ragnest.common.exception.BizException;
import com.ragnest.common.exception.CommonErrorCode;
import com.ragnest.common.result.Result;
import com.ragnest.core.model.KnowledgeBase;
import com.ragnest.core.service.KnowledgeBaseService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 知识库管理接口。
 */
@RestController
@RequestMapping("/api/knowledge-bases")
public class KnowledgeBaseController {

    private final KnowledgeBaseService knowledgeBaseService;

    public KnowledgeBaseController(KnowledgeBaseService knowledgeBaseService) {
        this.knowledgeBaseService = knowledgeBaseService;
    }

    /**
     * 创建知识库。
     */
    @PostMapping
    public Result<KnowledgeBaseVO> create(@Valid @RequestBody KnowledgeBaseCreateRequest request,
                                          @RequestHeader(value = "X-Tenant-Id", required = false) String tenantId) {
        KnowledgeBase kb = KnowledgeBase.builder()
                .name(request.getName())
                .description(request.getDescription())
                .vectorDimension(request.getVectorDimension())
                .embeddingModel(request.getEmbeddingModel())
                .tenantId(tenantId)
                .build();

        KnowledgeBase created = knowledgeBaseService.create(kb);
        return Result.success(KnowledgeBaseAssembler.toVO(created));
    }

    /**
     * 更新知识库。
     */
    @PutMapping("/{id}")
    public Result<KnowledgeBaseVO> update(@PathVariable Long id,
                                          @Valid @RequestBody KnowledgeBaseUpdateRequest request) {
        KnowledgeBase kb = KnowledgeBase.builder()
                .id(id)
                .name(request.getName())
                .description(request.getDescription())
                .vectorDimension(request.getVectorDimension())
                .embeddingModel(request.getEmbeddingModel())
                .status(request.getStatus())
                .build();

        KnowledgeBase updated = knowledgeBaseService.update(kb);
        return Result.success(KnowledgeBaseAssembler.toVO(updated));
    }

    /**
     * 查询知识库详情。
     */
    @GetMapping("/{id}")
    public Result<KnowledgeBaseVO> getById(@PathVariable Long id) {
        KnowledgeBase kb = knowledgeBaseService.findById(id)
                .orElseThrow(() -> new BizException(CommonErrorCode.NOT_FOUND));
        return Result.success(KnowledgeBaseAssembler.toVO(kb));
    }

    /**
     * 查询当前租户的知识库列表。
     */
    @GetMapping
    public Result<List<KnowledgeBaseVO>> list(@RequestHeader(value = "X-Tenant-Id", required = false) String tenantId) {
        List<KnowledgeBaseVO> vos = knowledgeBaseService.listByTenant(tenantId).stream()
                .map(KnowledgeBaseAssembler::toVO)
                .toList();
        return Result.success(vos);
    }

    /**
     * 删除知识库。
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        knowledgeBaseService.delete(id);
        return Result.success();
    }
}
