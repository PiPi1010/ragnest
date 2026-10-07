package com.ragnest.admin.assembler;

import com.ragnest.admin.vo.ConversationVO;
import com.ragnest.core.model.Conversation;
import com.ragnest.core.model.Message;

/**
 * 会话装配器：领域模型与 VO 的相互转换。
 */
public final class ConversationAssembler {

    private ConversationAssembler() {
    }

    /**
     * 领域模型 → VO（含消息列表）。
     */
    public static ConversationVO toVO(Conversation conversation) {
        if (conversation == null) {
            return null;
        }
        ConversationVO vo = new ConversationVO();
        vo.setId(conversation.getId());
        vo.setTitle(conversation.getTitle());
        vo.setKnowledgeBaseId(conversation.getKnowledgeBaseId());
        vo.setTenantId(conversation.getTenantId());
        vo.setCreatedAt(conversation.getCreatedAt());
        vo.setUpdatedAt(conversation.getUpdatedAt());

        if (conversation.getMessages() != null) {
            vo.setMessages(conversation.getMessages().stream()
                    .map(ConversationAssembler::toMessageVO)
                    .toList());
        }
        return vo;
    }

    private static ConversationVO.MessageVO toMessageVO(Message message) {
        ConversationVO.MessageVO vo = new ConversationVO.MessageVO();
        vo.setId(message.getId());
        vo.setRole(message.getRole());
        vo.setContent(message.getContent());
        vo.setCreatedAt(message.getCreatedAt());
        return vo;
    }
}
