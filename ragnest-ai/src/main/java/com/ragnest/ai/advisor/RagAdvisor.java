package com.ragnest.ai.advisor;

import com.ragnest.ai.rag.RagPipeline;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * RAG 增强 Advisor。
 *
 * <p>在请求进入模型前，从用户输入中检索相关文档，并将检索结果作为上下文
 * 注入到系统提示词中，使模型基于知识库回答问题。</p>
 *
 * <p>对于「元数据查询」（如查询有哪些文档/知识库/文件列表），跳过检索，
 * 交由 Tool Calling 处理，避免语义检索干扰结构化查询。</p>
 */
public class RagAdvisor implements BaseAdvisor {

    /** 元数据查询特征（命中则跳过 RAG 检索，交给工具调用） */
    private static final Pattern METADATA_QUERY = Pattern.compile(
            "有哪些|有哪些文档|哪些文档|哪些文件|文件列表|文档列表|列出|查询.*文档|什么文档|什么文件|"
                    + "有多少|几个知识库|哪些知识库|知识库列表|查一下.*文档|看看.*文档");

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
        // 提取用户输入
        UserMessage userMessage = request.prompt().getUserMessage();
        if (userMessage == null) {
            return request;
        }
        String query = userMessage.getText();
        if (query == null || query.isBlank()) {
            return request;
        }

        // 元数据查询：跳过检索，交给工具调用
        if (METADATA_QUERY.matcher(query).find()) {
            return request;
        }

        // 检索相关文档
        List<Document> documents = ragPipeline.retrieve(query);
        if (documents == null || documents.isEmpty()) {
            return request;
        }

        // 拼接上下文注入系统提示词
        String context = documents.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n---\n\n"));

        String augmented = "请基于以下参考资料回答用户问题。如果资料中没有相关信息，请如实说明。\n\n"
                + "参考资料：\n" + context;

        var newPrompt = request.prompt().augmentSystemMessage(augmented);
        return request.mutate().prompt(newPrompt).build();
    }

    @Override
    public ChatClientResponse after(ChatClientResponse response, AdvisorChain chain) {
        return response;
    }
}
