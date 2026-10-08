package com.ragnest.core.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * 会话领域模型。
 *
 * <p>会话包含完整的消息列表（一对多映射），便于一次加载整个对话上下文。</p>
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "conversation")
public class Conversation extends BaseEntity {

    /** 会话标题 */
    @Column(length = 256)
    private String title;

    /** 关联知识库 ID（可为空，表示通用对话） */
    @Column(name = "knowledge_base_id")
    private Long knowledgeBaseId;

    /** 所属租户 ID */
    @Column(name = "tenant_id", length = 64)
    private String tenantId;

    /** 消息列表（按时间顺序） */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt ASC")
    private List<Message> messages = new ArrayList<>();

    /** 追加一条消息 */
    public void addMessage(Message message) {
        if (this.messages == null) {
            this.messages = new ArrayList<>();
        }
        this.messages.add(message);
    }
}
