package com.ragnest.admin.controller;

import com.ragnest.admin.assembler.ConversationAssembler;
import com.ragnest.admin.dto.ChatRequest;
import com.ragnest.admin.vo.ChatResponseVO;
import com.ragnest.admin.vo.ConversationVO;
import com.ragnest.ai.chat.ChatService;
import com.ragnest.ai.chat.StreamingChatService;
import com.ragnest.common.result.Result;
import com.ragnest.core.model.Conversation;
import com.ragnest.core.model.Message;
import com.ragnest.core.service.ConversationService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;

/**
 * 会话管理接口。
 */
@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    private final ConversationService conversationService;
    private final ChatService chatService;
    private final StreamingChatService streamingChatService;

    public ConversationController(ConversationService conversationService,
                                  ChatService chatService,
                                  StreamingChatService streamingChatService) {
        this.conversationService = conversationService;
        this.chatService = chatService;
        this.streamingChatService = streamingChatService;
    }

    /**
     * 创建会话。
     */
    @PostMapping
    public Result<ConversationVO> create(@RequestBody Conversation conversation) {
        Conversation created = conversationService.create(conversation);
        return Result.success(ConversationAssembler.toVO(created));
    }

    /**
     * 查询会话详情（含消息列表）。
     */
    @GetMapping("/{id}")
    public Result<ConversationVO> getById(@PathVariable Long id) {
        Conversation conversation = conversationService.findById(id).orElse(null);
        return Result.success(ConversationAssembler.toVO(conversation));
    }

    /**
     * 查询当前租户的会话列表。
     */
    @GetMapping
    public Result<List<ConversationVO>> list(@RequestHeader(value = "X-Tenant-Id", required = false) String tenantId) {
        List<ConversationVO> vos = conversationService.listByTenant(tenantId).stream()
                .map(ConversationAssembler::toVO)
                .toList();
        return Result.success(vos);
    }

    /**
     * 发送消息（阻塞式）。
     */
    @PostMapping("/chat")
    public Result<ChatResponseVO> chat(@Valid @RequestBody ChatRequest request) {
        String reply = chatService.chat(request.getMessage());
        ChatResponseVO vo = new ChatResponseVO();
        vo.setContent(reply);
        vo.setConversationId(request.getConversationId());
        return Result.success(vo);
    }

    /**
     * 发送消息（SSE 流式）。
     */
    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(@Valid @RequestBody ChatRequest request) {
        return streamingChatService.chatStream(request.getMessage());
    }

    /**
     * 追加消息到会话。
     */
    @PostMapping("/{id}/messages")
    public Result<ConversationVO> addMessage(@PathVariable Long id, @RequestBody Message message) {
        Conversation conversation = conversationService.addMessage(id, message);
        return Result.success(ConversationAssembler.toVO(conversation));
    }

    /**
     * 删除会话。
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        conversationService.delete(id);
        return Result.success();
    }
}
