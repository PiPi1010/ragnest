package com.ragnest.sdk;

import com.ragnest.common.result.Result;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.JsonNode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * RagNest 对外客户端。
 *
 * <p>供外部系统通过 HTTP 调用 RagNest 服务，封装了：</p>
 * <ul>
 *   <li>对话（chat）</li>
 *   <li>知识库查询</li>
 *   <li>健康检查</li>
 * </ul>
 *
 * <p>实现基于 Java 内置 {@link HttpClient}，无额外重依赖。</p>
 */
public class RagNestClient {

    private final RagNestClientConfig config;
    private final HttpClient httpClient;
    private final JsonMapper jsonMapper;

    public RagNestClient(RagNestClientConfig config) {
        this.config = config;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(config.getConnectTimeoutMillis()))
                .build();
        this.jsonMapper = JsonMapper.shared();
    }

    /**
     * 发送对话消息。
     *
     * @param message 用户消息
     * @return 模型回复文本
     */
    public String chat(String message) {
        try {
            String body = jsonMapper.writeValueAsString(
                    java.util.Map.of("message", message));

            HttpRequest request = buildRequest("/api/conversations/chat", "POST", body);
            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            JsonNode node = jsonMapper.readTree(response.body());
            return node.path("data").path("content").asText();
        } catch (Exception e) {
            throw new RagNestException("对话请求失败", e);
        }
    }

    /**
     * 查询知识库列表。
     *
     * @return 知识库 JSON 数组
     */
    public JsonNode listKnowledgeBases() {
        try {
            HttpRequest request = buildRequest("/api/knowledge-bases", "GET", null);
            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            return jsonMapper.readTree(response.body()).path("data");
        } catch (Exception e) {
            throw new RagNestException("查询知识库失败", e);
        }
    }

    /**
     * 健康检查。
     *
     * @return true 表示服务健康
     */
    public boolean isHealthy() {
        try {
            HttpRequest request = buildRequest("/actuator/health", "GET", null);
            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            return response.statusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 构建 HTTP 请求，统一附加认证与租户头。
     */
    private HttpRequest buildRequest(String path, String method, String body) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(config.getBaseUrl() + path))
                .timeout(Duration.ofMillis(config.getReadTimeoutMillis()));

        if (config.getToken() != null) {
            builder.header("Authorization", "Bearer " + config.getToken());
        } else if (config.getApiKey() != null) {
            builder.header("X-Api-Key", config.getApiKey());
        }
        if (config.getTenantId() != null) {
            builder.header("X-Tenant-Id", config.getTenantId());
        }

        if ("POST".equals(method)) {
            builder.header("Content-Type", "application/json");
            builder.POST(HttpRequest.BodyPublishers.ofString(body != null ? body : "",
                    StandardCharsets.UTF_8));
        } else {
            builder.GET();
        }

        return builder.build();
    }
}
