package com.ragnest.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 文档领域模型。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    /** 主键 */
    private Long id;

    /** 所属知识库 ID */
    private Long knowledgeBaseId;

    /** 文档名 */
    private String name;

    /** 原始文件类型（pdf/word/markdown/excel 等） */
    private String fileType;

    /** 存储路径或对象存储 key */
    private String storageKey;

    /** 解析状态（0 待解析，1 解析中，2 已完成，3 失败） */
    private Integer status;

    /** 切片数量 */
    private Integer chunkCount;

    /** 所属租户 ID */
    private String tenantId;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
}
