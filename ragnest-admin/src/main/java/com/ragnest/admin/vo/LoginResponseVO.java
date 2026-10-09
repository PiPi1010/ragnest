package com.ragnest.admin.vo;

import lombok.Data;

/**
 * 登录响应。
 */
@Data
public class LoginResponseVO {

    /** JWT token */
    private String token;

    /** 租户 ID */
    private String tenantId;

    /** 用户名 */
    private String username;
}
