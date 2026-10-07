package com.ragnest.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 会话消息领域模型。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    /** 主键 */
    private Long id;

    /** 所属会话 ID */
    private Long conversationId;

    /** 角色（user / assistant / system） */
    private String role;

    /** 消息内容 */
    private String content;

    /** 创建时间 */
    private LocalDateTime createdAt;
}
