package com.ragnest.ai.advisor;

import com.ragnest.ai.rag.RagPipeline;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;

/**
 * RAG 增强 Advisor。
 *
 * <p>在请求进入模型前，将检索到的知识文档注入到上下文中。</p>
 *
 * <p>Spring AI 2.0 中 Advisor 接口重构为 {@link BaseAdvisor}，需实现
 * {@code before} / {@code after} 两个钩子方法。</p>
 *
 * <p>TODO：完整的文档注入逻辑待核心链路跑通后细化。</p>
 */
public class RagAdvisor implements BaseAdvisor {

    private final RagPipeline ragPipeline;

    public RagAdvisor(RagPipeline ragPipeline) {
        this.ragPipeline = ragPipeline;
    }

    @Override
    public String getName() {
        return "rag-advisor";
    }

    @Override
    public int getOrder() {
        return 100;
    }

    @Override
    public ChatClientRequest before(ChatClientRequest request, AdvisorChain chain) {
        // TODO: 提取用户 query → 调用 ragPipeline.retrieve() → 将文档拼入 system prompt
        return request;
    }

    @Override
    public ChatClientResponse after(ChatClientResponse response, AdvisorChain chain) {
        return response;
    }
}
