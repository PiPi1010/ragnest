package com.ragnest.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 更新知识库请求。
 */
@Data
public class KnowledgeBaseUpdateRequest {

    @NotBlank(message = "知识库名称不能为空")
    private String name;

    private String description;

    private Integer vectorDimension;

    private String embeddingModel;

    private Integer status;
}
