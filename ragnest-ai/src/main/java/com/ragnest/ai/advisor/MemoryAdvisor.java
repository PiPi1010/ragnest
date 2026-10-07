package com.ragnest.ai.advisor;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;

/**
 * 会话记忆 Advisor。
 *
 * <p>负责多轮对话的上下文维护。</p>
 *
 * <p>Spring AI 2.0 已内置 {@code MessageChatMemoryAdvisor} 与
 * {@code ChatMemory} 抽象，生产环境建议优先复用内置实现，本类作为
 * 自定义记忆策略的扩展点。</p>
 *
 * <p>TODO：自定义记忆策略待核心链路跑通后细化。</p>
 */
public class MemoryAdvisor implements BaseAdvisor {

    @Override
    public String getName() {
        return "memory-advisor";
    }

    @Override
    public int getOrder() {
        return 50;
    }

    @Override
    public ChatClientRequest before(ChatClientRequest request, AdvisorChain chain) {
        return request;
    }

    @Override
    public ChatClientResponse after(ChatClientResponse response, AdvisorChain chain) {
        return response;
    }
}
