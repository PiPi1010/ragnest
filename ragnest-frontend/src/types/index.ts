// 统一返回体
export interface Result<T = any> {
  code: number
  message: string
  data: T
  success: boolean
}

// 登录
export interface LoginRequest {
  username: string
  password: string
}

export interface LoginResponse {
  token: string
  tenantId: string
  username: string
}

// 知识库
export interface KnowledgeBase {
  id: number
  name: string
  description: string
  vectorDimension: number
  embeddingModel: string
  tenantId: string
  status: number
  createdAt: string
  updatedAt: string
}

// 文档
export interface Document {
  id: number
  knowledgeBaseId: number
  name: string
  fileType: string
  status: number
  chunkCount: number
  tenantId: string
  createdAt: string
  updatedAt: string
}

// 会话
export interface Conversation {
  id: number
  title: string
  knowledgeBaseId: number | null
  tenantId: string
  messages: Message[]
  createdAt: string
  updatedAt: string
}

export interface Message {
  id: number
  role: 'user' | 'assistant' | 'system'
  content: string
  createdAt: string
}

// 对话请求/响应
export interface ChatRequest {
  message: string
  conversationId?: number
  knowledgeBaseId?: number
}

export interface ChatResponse {
  content: string
  conversationId: number
}
