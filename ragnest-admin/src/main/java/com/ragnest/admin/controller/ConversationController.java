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
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
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
    public Result<ConversationVO> create(@RequestBody Conversation conversation,
                                         @RequestHeader(value = "X-Tenant-Id", required = false) String tenantId) {
        conversation.setTenantId(tenantId);
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
     * 发送消息（阻塞式，支持多轮对话）。
     *
     * <p>传入 conversationId 时，加载会话历史作为上下文，并在对话后将问答保存到会话。</p>
     */
    @PostMapping("/chat")
    public Result<ChatResponseVO> chat(@Valid @RequestBody ChatRequest request) {
        String reply;
        if (request.getConversationId() != null) {
            // 多轮对话：加载历史 + 带上下文问答 + 持久化
            List<Message> history = conversationService.getHistory(request.getConversationId());
            reply = chatService.chatWithHistory(history, request.getMessage());

            conversationService.addMessage(request.getConversationId(),
                    Message.builder().role("user").content(request.getMessage()).build());
            conversationService.addMessage(request.getConversationId(),
                    Message.builder().role("assistant").content(reply).build());
        } else {
            // 单轮对话
            reply = chatService.chat(request.getMessage());
        }

        ChatResponseVO vo = new ChatResponseVO();
        vo.setContent(reply);
        vo.setConversationId(request.getConversationId());
        return Result.success(vo);
    }

    /**
     * 发送消息（SSE 流式，支持多轮）。
     *
     * <p>传入 conversationId 时加载历史作为上下文；通过 {@link SseEmitter} 逐 token 推送。
     * 未传 conversationId 时自动创建会话，并在首条消息返回会话 ID，对话完成后持久化问答。</p>
     */
    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter chatStream(@Valid @RequestBody ChatRequest request) {
        SseEmitter emitter = new SseEmitter(120_000L);

        // 会话处理：未传则自动创建
        Long conversationId = request.getConversationId();
        if (conversationId == null) {
            Conversation conversation = new Conversation();
            conversation.setTitle(request.getMessage().length() > 20
                    ? request.getMessage().substring(0, 20)
                    : request.getMessage());
            conversation = conversationService.create(conversation);
            conversationId = conversation.getId();
        }
        final Long cid = conversationId;

        // 首条消息返回会话 ID，供前端保存
        try {
            emitter.send(SseEmitter.event().data("[conversationId:" + cid + "]"));
        } catch (Exception e) {
            emitter.completeWithError(e);
            return emitter;
        }

        List<Message> history = conversationService.getHistory(cid);
        Flux<String> flux = streamingChatService.chatStreamWithHistory(history, request.getMessage());

        StringBuilder fullReply = new StringBuilder();
        flux.subscribe(
                token -> {
                    fullReply.append(token);
                    try {
                        emitter.send(SseEmitter.event().data(token));
                    } catch (Exception e) {
                        emitter.completeWithError(e);
                    }
                },
                emitter::completeWithError,
                () -> {
                    // 流结束后持久化问答
                    try {
                        conversationService.addMessage(cid,
                                Message.builder().role("user").content(request.getMessage()).build());
                        conversationService.addMessage(cid,
                                Message.builder().role("assistant").content(fullReply.toString()).build());
                    } catch (Exception e) {
                        // 持久化失败不影响流式返回
                    }
                    emitter.complete();
                }
        );

        return emitter;
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
