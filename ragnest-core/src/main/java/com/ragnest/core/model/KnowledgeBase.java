package com.ragnest.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 知识库领域模型。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeBase {

    /** 主键 */
    private Long id;

    /** 名称 */
    private String name;

    /** 描述 */
    private String description;

    /** 向量维度 */
    private Integer vectorDimension;

    /** 嵌入模型标识 */
    private String embeddingModel;

    /** 所属租户 ID */
    private String tenantId;

    /** 状态（0 正常，1 停用） */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
}
