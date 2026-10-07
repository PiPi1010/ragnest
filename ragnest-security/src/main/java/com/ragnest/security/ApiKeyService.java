package com.ragnest.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * API Key 服务。
 *
 * <p>提供 API Key 的生成、校验能力，用于对外 SDK 或第三方系统的机器调用。
 * 与用户 JWT 认证并存，满足 M2M（机器对机器）场景。</p>
 */
public class ApiKeyService {

    /** API Key 存储（生产环境应持久化到数据库，并支持过期、吊销） */
    private final Map<String, String> apiKeyStore = new ConcurrentHashMap<>();

    /**
     * 生成 API Key。
     *
     * @param owner 所属方标识（如租户/应用）
     * @return API Key
     */
    public String generate(String owner) {
        String apiKey = "rk_" + randomHex(32);
        apiKeyStore.put(apiKey, owner);
        return apiKey;
    }

    /**
     * 校验 API Key 是否有效。
     *
     * @param apiKey API Key
     * @return true 表示有效
     */
    public boolean isValid(String apiKey) {
        return apiKey != null && apiKeyStore.containsKey(apiKey);
    }

    /**
     * 吊销 API Key。
     *
     * @param apiKey API Key
     */
    public void revoke(String apiKey) {
        apiKeyStore.remove(apiKey);
    }

    private String randomHex(int bytes) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(Long.toHexString(System.nanoTime()).getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes; i++) {
                sb.append(String.format("%02x", hash[i % hash.length]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 不可用", e);
        }
    }
}
