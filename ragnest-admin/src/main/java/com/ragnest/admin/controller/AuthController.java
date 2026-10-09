package com.ragnest.admin.controller;

import com.ragnest.admin.dto.LoginRequest;
import com.ragnest.admin.vo.LoginResponseVO;
import com.ragnest.common.exception.BizException;
import com.ragnest.common.result.Result;
import com.ragnest.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证接口。
 *
 * <p>提供登录接口，校验配置的固定账号并签发 JWT。</p>
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;

    @Value("${ragnest.auth.username:admin}")
    private String username;

    @Value("${ragnest.auth.password:admin123}")
    private String password;

    @Value("${ragnest.auth.tenant-id:default}")
    private String tenantId;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * 登录。
     *
     * <p>校验配置的固定账号，通过后签发 JWT（含租户信息）。</p>
     */
    @PostMapping("/login")
    public Result<LoginResponseVO> login(@Valid @RequestBody LoginRequest request) {
        if (!username.equals(request.getUsername()) || !password.equals(request.getPassword())) {
            throw new BizException(5001, "用户名或密码错误");
        }

        String token = jwtService.generateToken(request.getUsername(), tenantId);

        LoginResponseVO vo = new LoginResponseVO();
        vo.setToken(token);
        vo.setTenantId(tenantId);
        vo.setUsername(request.getUsername());
        return Result.success(vo);
    }
}
