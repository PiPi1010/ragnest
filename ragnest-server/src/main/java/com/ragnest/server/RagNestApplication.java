package com.ragnest.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * RagNest 应用启动入口。
 *
 * <p>扫描 {@code com.ragnest} 下所有模块的组件（配置、Controller、Service 等）。</p>
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.ragnest")
public class RagNestApplication {

    public static void main(String[] args) {
        SpringApplication.run(RagNestApplication.class, args);
    }
}
