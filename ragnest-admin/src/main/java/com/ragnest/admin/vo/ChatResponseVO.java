package com.ragnest.admin.vo;

import lombok.Data;

/**
 * 对话响应视图对象。
 */
@Data
public class ChatResponseVO {

    /** 回复内容 */
    private String content;

    /** 会话 ID */
    private Long conversationId;
}
