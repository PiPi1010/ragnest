package com.ragnest.ai.chat;

import com.ragnest.core.model.Message;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

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
     * 流式对话（单轮）。
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
     * 流式对话（多轮，带历史）。
     *
     * @param history 历史消息列表（按时间升序，不含当前提问）
     * @param message 当前用户消息
     * @return 逐 token 的文本流
     */
    public Flux<String> chatStreamWithHistory(List<Message> history, String message) {
        List<org.springframework.ai.chat.messages.Message> messages = new ArrayList<>();

        if (history != null) {
            for (Message m : history) {
                messages.add(toSpringAiMessage(m));
            }
        }
        messages.add(new UserMessage(message));

        return chatClient.prompt()
                .messages(messages)
                .stream()
                .content();
    }

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
