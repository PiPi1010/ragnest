package com.ragnest.core.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 文档领域模型。
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "document")
public class Document extends BaseEntity {

    /** 所属知识库 ID */
    @Column(name = "knowledge_base_id")
    private Long knowledgeBaseId;

    /** 文档名 */
    @Column(nullable = false, length = 256)
    private String name;

    /** 原始文件类型（pdf/word/markdown/excel 等） */
    @Column(name = "file_type", length = 32)
    private String fileType;

    /** 存储路径或对象存储 key */
    @Column(name = "storage_key", length = 512)
    private String storageKey;

    /** 解析状态（0 待解析，1 解析中，2 已完成，3 失败） */
    @Column
    private Integer status;

    /** 切片数量 */
    @Column(name = "chunk_count")
    private Integer chunkCount;

    /** 所属租户 ID */
    @Column(name = "tenant_id", length = 64)
    private String tenantId;
}
