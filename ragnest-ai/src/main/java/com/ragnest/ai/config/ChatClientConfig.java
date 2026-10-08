package com.ragnest.ai.config;

import com.ragnest.ai.advisor.RagAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * ChatClient 配置。
 *
 * <p>基于注入的 {@link ChatModel} 构建 ChatClient，并挂载 {@link RagAdvisor}
 * 使对话自动注入检索上下文。具体使用哪个模型由启动模块（ragnest-server）
 * 通过配置决定，此处只负责装配。</p>
 */
@Configuration
public class ChatClientConfig {

    /**
     * 默认 ChatClient（挂载 RAG Advisor）。
     */
    @Bean
    @Primary
    public ChatClient chatClient(ChatModel chatModel, RagAdvisor ragAdvisor) {
        return ChatClient.builder(chatModel)
                .defaultAdvisors(ragAdvisor)
                .build();
    }
}
