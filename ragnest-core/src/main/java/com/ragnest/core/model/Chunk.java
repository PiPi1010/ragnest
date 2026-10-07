package com.ragnest.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文档切片领域模型。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chunk {

    /** 主键 */
    private Long id;

    /** 所属文档 ID */
    private Long documentId;

    /** 切片在文档中的顺序 */
    private Integer sequence;

    /** 切片文本内容 */
    private String content;

    /** 向量（可空，向量通常单独存储于向量库） */
    private List<Float> embedding;

    /** 元数据（如标题、页码等），以 JSON 字符串存储 */
    private String metadata;

    /** 所属租户 ID */
    private String tenantId;

    /** 创建时间 */
    private LocalDateTime createdAt;
}
