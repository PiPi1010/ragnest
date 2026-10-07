package com.ragnest.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 服务。
 *
 * <p>负责 JWT 的生成与解析。</p>
 */
public class JwtService {

    /** 默认密钥（生产环境务必通过配置注入强密钥） */
    private final SecretKey secretKey;

    /** token 有效期（毫秒），默认 24 小时 */
    private final long expirationMillis;

    public JwtService(String secret, long expirationMillis) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMillis = expirationMillis;
    }

    /**
     * 生成 JWT。
     *
     * @param subject  用户标识
     * @param tenantId 租户 ID
     * @return JWT 字符串
     */
    public String generateToken(String subject, String tenantId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .subject(subject)
                .claim("tenantId", tenantId)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    /**
     * 解析 JWT，返回 Claims。
     *
     * @param token JWT 字符串
     * @return Claims
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 校验 token 是否有效。
     *
     * @param token JWT 字符串
     * @return true 表示有效
     */
    public boolean isValid(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
