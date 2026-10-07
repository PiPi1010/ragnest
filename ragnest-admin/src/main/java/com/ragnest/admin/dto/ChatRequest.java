package com.ragnest.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 对话请求。
 */
@Data
public class ChatRequest {

    @NotBlank(message = "消息内容不能为空")
    private String message;

    /** 会话 ID（可选，多轮对话时传入） */
    private Long conversationId;

    /** 知识库 ID（可选，指定时进行 RAG 检索） */
    private Long knowledgeBaseId;
}
