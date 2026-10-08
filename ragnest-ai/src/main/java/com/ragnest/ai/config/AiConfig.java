package com.ragnest.ai.config;

import com.ragnest.ai.chat.ChatService;
import com.ragnest.ai.chat.StreamingChatService;
import com.ragnest.ai.embedding.EmbeddingService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AI 服务装配。
 *
 * <p>将对话、流式、嵌入服务注册为 Spring Bean。</p>
 */
@Configuration
public class AiConfig {

    @Bean
    public ChatService chatService(ChatClient chatClient) {
        return new ChatService(chatClient);
    }

    @Bean
    public StreamingChatService streamingChatService(ChatClient chatClient) {
        return new StreamingChatService(chatClient);
    }

    @Bean
    public EmbeddingService embeddingService(EmbeddingModel embeddingModel) {
        return new EmbeddingService(embeddingModel);
    }
}
