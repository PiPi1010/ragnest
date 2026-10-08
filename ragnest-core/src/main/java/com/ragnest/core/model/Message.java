package com.ragnest.core.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 会话消息领域模型。
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "message")
public class Message extends BaseEntity {

    /** 所属会话 ID */
    @Column(name = "conversation_id")
    private Long conversationId;

    /** 角色（user / assistant / system） */
    @Column(length = 16)
    private String role;

    /** 消息内容 */
    @Column(columnDefinition = "text")
    private String content;
}
