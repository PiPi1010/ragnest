package com.ragnest.ai.chat;

import org.springframework.ai.chat.client.ChatClient;
import reactor.core.publisher.Flux;

/**
 * 对话服务（SSE 流式）。
 *
 * <p>返回 {@link Flux}，由上层（Controller）以 text/event-stream 输出。</p>
 */
public class StreamingChatService {

    private final ChatClient chatClient;

    public StreamingChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    /**
     * 流式对话。
     *
     * @param message 用户消息
     * @return 逐 token 的文本流
     */
    public Flux<String> chatStream(String message) {
        return chatClient.prompt()
                .user(message)
                .stream()
                .content();
    }

    /**
     * 带系统提示词的流式对话。
     *
     * @param systemPrompt 系统提示词
     * @param message      用户消息
     * @return 逐 token 的文本流
     */
    public Flux<String> chatStream(String systemPrompt, String message) {
        return chatClient.prompt()
                .system(systemPrompt)
                .user(message)
                .stream()
                .content();
    }
}
