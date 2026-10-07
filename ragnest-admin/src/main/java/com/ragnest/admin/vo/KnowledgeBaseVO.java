package com.ragnest.admin.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 知识库视图对象。
 */
@Data
public class KnowledgeBaseVO {

    private Long id;

    private String name;

    private String description;

    private Integer vectorDimension;

    private String embeddingModel;

    private String tenantId;

    private Integer status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
