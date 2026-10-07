package com.ragnest.admin.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文档视图对象。
 */
@Data
public class DocumentVO {

    private Long id;

    private Long knowledgeBaseId;

    private String name;

    private String fileType;

    private Integer status;

    private Integer chunkCount;

    private String tenantId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
