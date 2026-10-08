package com.ragnest.core.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 知识库领域模型。
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "knowledge_base")
public class KnowledgeBase extends BaseEntity {

    /** 名称 */
    @Column(nullable = false, length = 128)
    private String name;

    /** 描述 */
    @Column(length = 512)
    private String description;

    /** 向量维度 */
    @Column(name = "vector_dimension")
    private Integer vectorDimension;

    /** 嵌入模型标识 */
    @Column(name = "embedding_model", length = 64)
    private String embeddingModel;

    /** 所属租户 ID */
    @Column(name = "tenant_id", length = 64)
    private String tenantId;

    /** 状态（0 正常，1 停用） */
    @Column
    private Integer status;
}
