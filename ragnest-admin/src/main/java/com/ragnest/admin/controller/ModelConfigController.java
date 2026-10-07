package com.ragnest.admin.controller;

import com.ragnest.common.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 模型配置接口。
 *
 * <p>提供当前可用的模型配置信息（聊天模型、嵌入模型等）。</p>
 */
@RestController
@RequestMapping("/api/model-configs")
public class ModelConfigController {

    /**
     * 查询当前模型配置。
     *
     * <p>TODO：从配置中心/数据库读取实际模型配置，此处返回占位信息。</p>
     */
    @GetMapping
    public Result<List<Map<String, Object>>> list() {
        Map<String, Object> config = Map.of(
                "provider", "ollama",
                "chatModel", "qwen2.5:7b",
                "embeddingModel", "bge-m3",
                "vectorStore", "pgvector"
        );
        return Result.success(List.of(config));
    }
}
