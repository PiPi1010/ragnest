package com.ragnest.core.service;

import com.ragnest.core.model.Conversation;
import com.ragnest.core.model.Message;

import java.util.List;
import java.util.Optional;

/**
 * 会话领域服务接口。
 */
public interface ConversationService {

    /** 创建会话 */
    Conversation create(Conversation conversation);

    /** 追加消息 */
    Conversation addMessage(Long conversationId, Message message);

    /** 根据 ID 查询（含消息列表） */
    Optional<Conversation> findById(Long id);

    /** 查询某租户下所有会话 */
    List<Conversation> listByTenant(String tenantId);

    /** 删除会话 */
    void delete(Long id);
}
