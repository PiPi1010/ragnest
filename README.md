# Ragnest

企业级知识库管理后台，基于 **Spring Boot 4.0.8** + **JDK 21** + **Spring AI 2.0**。

## 技术栈

- Java 21
- Spring Boot 4.0.8（Spring Framework 7.0）
- Spring AI 2.0.1（兼容 Spring Boot 4.x + Framework 7）
- Maven 3.6.3+
- Tomcat 11（内嵌）

## 模块结构

多模块 Maven 项目，共 10 个业务模块：

| 模块 | 职责 |
|---|---|
| `ragnest-common` | 公共模块：统一返回体 `Result<T>`、全局异常、常量/枚举/工具类、通用配置（Jackson、CORS） |
| `ragnest-core` | 核心抽象层：领域模型、仓储接口、领域服务接口 |
| `ragnest-ai` | AI 能力层：Spring AI 配置、Modular RAG、对话服务、Tool Calling |
| `ragnest-parser` | 文档解析层：多格式解析、分块策略、解析流水线 |
| `ragnest-vector` | 向量存储层：向量库工厂与多实现（pgvector / Milvus / Redis） |
| `ragnest-admin` | 管理后台 API：控制器、DTO、VO、装配器 |
| `ragnest-tenant` | 多租户模块：租户上下文、过滤、数据隔离 |
| `ragnest-security` | 安全模块：认证、JWT、RBAC、API Key |
| `ragnest-server` | 启动模块：应用入口与多环境配置 |
| `ragnest-sdk` | 对外 SDK：`RagNestClient` |

### 模块依赖关系

```
ragnest-common
    ├── ragnest-core ──────────> common
    ├── ragnest-tenant ────────> common
    ├── ragnest-security ──────> common, tenant
    ├── ragnest-parser ────────> core
    ├── ragnest-vector ────────> core
    ├── ragnest-ai ────────────> core, vector, tenant
    ├── ragnest-admin ─────────> core, ai, security, tenant
    ├── ragnest-server ────────> common, admin, ai, security, vector, parser, tenant
    └── ragnest-sdk ───────────> common
```

## 构建

```bash
# 编译整个项目（多模块）
mvn clean package

# 只编译单个模块及其依赖
mvn -pl ragnest-server -am clean package
```

## 说明

- 当前为**骨架阶段**：目录结构 + 各模块 pom 已就绪，具体业务代码待开发
- 分支：`main`（稳定）+ `develop`（开发）
