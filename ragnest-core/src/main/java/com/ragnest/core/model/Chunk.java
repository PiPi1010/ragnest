package com.ragnest.core.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 文档切片领域模型。
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "chunk")
public class Chunk extends BaseEntity {

    /** 所属文档 ID */
    @Column(name = "document_id")
    private Long documentId;

    /** 切片在文档中的顺序 */
    @Column
    private Integer sequence;

    /** 切片文本内容 */
    @Column(columnDefinition = "text")
    private String content;

    /** 元数据（如标题、页码等），以 JSON 字符串存储 */
    @Column(columnDefinition = "text")
    private String metadata;

    /** 所属租户 ID */
    @Column(name = "tenant_id", length = 64)
    private String tenantId;
}
