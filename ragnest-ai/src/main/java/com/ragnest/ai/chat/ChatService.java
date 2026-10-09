package com.ragnest.ai.chat;

import com.ragnest.core.model.Message;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * 多轮对话（带历史消息）。
     *
     * <p>将历史消息（按时间顺序）拼接到当前提问之前，使模型具备上下文记忆。</p>
     *
     * @param history 历史消息列表（按时间升序，不含当前提问）
     * @param message 当前用户消息
     * @return 模型回复文本
     */
    public String chatWithHistory(List<Message> history, String message) {
        List<org.springframework.ai.chat.messages.Message> messages = new ArrayList<>();

        if (history != null) {
            for (Message m : history) {
                messages.add(toSpringAiMessage(m));
            }
        }
        // 追加当前用户消息
        messages.add(new UserMessage(message));

        return chatClient.prompt()
                .messages(messages)
                .call()
                .content();
    }

    /**
     * 将领域消息模型转换为 Spring AI 消息。
     */
    private org.springframework.ai.chat.messages.Message toSpringAiMessage(Message m) {
        if (m.getRole() == null || m.getContent() == null) {
            return new UserMessage("");
        }
        return switch (m.getRole()) {
            case "assistant" -> new AssistantMessage(m.getContent());
            case "system" -> new SystemMessage(m.getContent());
            default -> new UserMessage(m.getContent());
        };
    }
}
