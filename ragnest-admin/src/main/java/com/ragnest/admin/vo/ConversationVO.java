package com.ragnest.admin.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 会话视图对象（含消息列表）。
 */
@Data
public class ConversationVO {

    private Long id;

    private String title;

    private Long knowledgeBaseId;

    private String tenantId;

    private List<MessageVO> messages;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /**
     * 消息视图对象。
     */
    @Data
    public static class MessageVO {
        private Long id;
        private String role;
        private String content;
        private LocalDateTime createdAt;
    }
}
