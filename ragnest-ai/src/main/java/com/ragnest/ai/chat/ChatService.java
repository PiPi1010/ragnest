package com.ragnest.ai.chat;

import org.springframework.ai.chat.client.ChatClient;

/**
 * 对话服务（阻塞式）。
 */
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    /**
     * 单轮对话。
     *
     * @param message 用户消息
     * @return 模型回复文本
     */
    public String chat(String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    /**
     * 带系统提示词的对话。
     *
     * @param systemPrompt 系统提示词
     * @param message      用户消息
     * @return 模型回复文本
     */
    public String chat(String systemPrompt, String message) {
        return chatClient.prompt()
                .system(systemPrompt)
                .user(message)
                .call()
                .content();
    }
}
