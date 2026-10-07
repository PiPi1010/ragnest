package com.ragnest.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * ChatClient 配置。
 *
 * <p>基于注入的 {@link ChatModel} 构建 ChatClient。具体使用哪个模型由
 * 启动模块（ragnest-server）通过配置决定，此处只负责装配。</p>
 *
 * <p>Spring AI 2.0 中 ChatClient 是推荐的统一入口，ChatModel 降级为底层构建块。</p>
 */
@Configuration
public class ChatClientConfig {

    /**
     * 默认 ChatClient。
     *
     * <p>若存在多个 ChatModel，可用 @Primary 标记默认模型。</p>
     */
    @Bean
    @Primary
    public ChatClient chatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
    }
}
