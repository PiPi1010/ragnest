package com.ragnest.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * JWT 配置属性。
 *
 * <p>对应配置前缀 {@code ragnest.jwt}。</p>
 */
@ConfigurationProperties(prefix = "ragnest.jwt")
public class JwtProperties {

    /** 签名密钥 */
    private String secret;

    /** token 有效期（毫秒） */
    private long expiration = 86400000L;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }
}
