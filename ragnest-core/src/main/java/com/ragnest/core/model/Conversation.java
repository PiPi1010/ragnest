package com.ragnest.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 会话领域模型。
 *
 * <p>会话包含完整的消息列表，便于一次加载整个对话上下文。</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Conversation {

    /** 主键 */
    private Long id;

    /** 会话标题 */
    private String title;

    /** 关联知识库 ID（可为空，表示通用对话） */
    private Long knowledgeBaseId;

    /** 所属租户 ID */
    private String tenantId;

    /** 消息列表（按时间顺序） */
    @Builder.Default
    private List<Message> messages = new ArrayList<>();

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

    /** 追加一条消息 */
    public void addMessage(Message message) {
        if (this.messages == null) {
            this.messages = new ArrayList<>();
        }
        this.messages.add(message);
    }
}
