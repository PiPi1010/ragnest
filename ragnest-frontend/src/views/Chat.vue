<template>
  <div class="chat-page">
    <!-- 左侧会话列表 -->
    <el-card class="conv-list-card">
      <template #header>
        <div class="conv-list-header">
          <span>历史会话</span>
          <el-button size="small" type="primary" text @click="newConversation">＋ 新会话</el-button>
        </div>
      </template>
      <div class="conv-list">
        <div
          v-for="c in conversations"
          :key="c.id"
          class="conv-item"
          :class="{ active: c.id === conversationId }"
          @click="switchConversation(c.id)"
        >
          <div class="conv-title">{{ c.title || '新会话' }}</div>
          <div class="conv-time">{{ formatTime(c.updatedAt || c.createdAt) }}</div>
          <el-icon class="conv-del" @click.stop="removeConversation(c.id)"><Close /></el-icon>
        </div>
        <div v-if="conversations.length === 0" class="conv-empty">暂无历史会话</div>
      </div>
    </el-card>

    <!-- 右侧对话区 -->
    <el-card class="chat-card">
      <template #header>
        <div class="card-header">
          <span>智能问答</span>
          <div class="header-right">
            <el-select v-model="selectedKb" placeholder="全部知识库" clearable style="width: 200px; margin-right: 12px">
              <el-option v-for="kb in kbList" :key="kb.id" :label="kb.name" :value="kb.id" />
            </el-select>
            <el-button @click="newConversation">新会话</el-button>
          </div>
        </div>
      </template>

      <div class="chat-box" ref="chatBoxRef">
        <div v-if="messages.length === 0" class="empty">
          <p>👋 开始提问吧</p>
          <p class="sub">上传文档后，我可以基于知识库内容回答你的问题</p>
        </div>
        <div v-for="(msg, idx) in messages" :key="idx" class="msg-row" :class="msg.role">
          <div class="avatar">{{ msg.role === 'user' ? '我' : 'AI' }}</div>
          <div class="bubble">{{ msg.content }}<span v-if="loading && idx === messages.length - 1" class="cursor">▍</span></div>
        </div>
      </div>

      <div class="input-area">
        <el-input
          v-model="input"
          type="textarea"
          :rows="3"
          placeholder="输入你的问题，回车发送（Shift+Enter 换行）"
          @keydown.enter.exact.prevent="send"
        />
        <el-button type="primary" :loading="loading" style="margin-top: 12px" @click="send">
          发送
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Close } from '@element-plus/icons-vue'
import { listKnowledgeBases } from '../api/knowledgeBase'
import { listConversations, getConversation, deleteConversation } from '../api/conversation'
import type { KnowledgeBase, Message, Conversation } from '../types'

// 流式接口直连后端（绕过 Vite 代理，避免 SSE 流结束信号丢失）
const STREAM_BASE = import.meta.env.DEV ? 'http://localhost:8080' : ''

const kbList = ref<KnowledgeBase[]>([])
const selectedKb = ref<number | undefined>()
const messages = ref<Message[]>([])
const input = ref('')
const loading = ref(false)
const conversations = ref<Conversation[]>([])
const conversationId = ref<number | undefined>()
const chatBoxRef = ref<HTMLElement>()

onMounted(async () => {
  kbList.value = await listKnowledgeBases()
  await loadConversations()

  // 恢复上次的会话（刷新后不丢失）
  const savedId = localStorage.getItem('ragnest_current_conv')
  if (savedId) {
    await switchConversation(Number(savedId))
  }
})

async function loadConversations() {
  conversations.value = await listConversations()
}

async function switchConversation(id: number) {
  conversationId.value = id
  localStorage.setItem('ragnest_current_conv', String(id))
  // 加载该会话的历史消息
  const conv = await getConversation(id)
  messages.value = conv.messages || []
  scrollToBottom()
}

function newConversation() {
  conversationId.value = undefined
  messages.value = []
  localStorage.removeItem('ragnest_current_conv')
}

async function removeConversation(id: number) {
  try {
    await ElMessageBox.confirm('确定删除这个会话吗？删除后无法恢复。', '提示', {
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      type: 'warning'
    })
  } catch {
    return // 用户取消
  }
  await deleteConversation(id)
  ElMessage.success('已删除')
  // 如果删的是当前会话，清空对话区
  if (conversationId.value === id) {
    newConversation()
  }
  await loadConversations()
}

function formatTime(t: string) {
  if (!t) return ''
  return t.replace('T', ' ').substring(0, 16)
}

async function send() {
  const text = input.value.trim()
  if (!text || loading.value) return
  input.value = ''

  messages.value.push({ id: Date.now(), role: 'user', content: text, createdAt: '' })

  // 先插入一个空的 assistant 消息占位，流式填充
  messages.value.push({ id: Date.now() + 1, role: 'assistant', content: '', createdAt: '' })
  const msgIndex = messages.value.length - 1

  loading.value = true
  scrollToBottom()

  const body: any = { message: text }
  if (conversationId.value) body.conversationId = conversationId.value
  if (selectedKb.value) body.knowledgeBaseId = selectedKb.value

  try {
    const token = localStorage.getItem('ragnest_token')
    const tenantId = localStorage.getItem('ragnest_tenant') || 'default'

    const response = await fetch(`${STREAM_BASE}/api/conversations/chat/stream`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
        'X-Tenant-Id': tenantId
      },
      body: JSON.stringify(body)
    })

    if (!response.ok || !response.body) {
      throw new Error(`请求失败: ${response.status}`)
    }

    const reader = response.body.getReader()
    const decoder = new TextDecoder()
    let buffer = ''

    // 带超时的流式读取，防止连接不关闭导致卡死
    let lastDataTime = Date.now()
    const IDLE_TIMEOUT = 15000 // 15 秒无数据视为流结束

    while (true) {
      const readPromise = reader.read()
      const timeoutPromise = new Promise<{ done: boolean; value?: Uint8Array }>((resolve) => {
        setTimeout(() => resolve({ done: true }), IDLE_TIMEOUT)
      })
      const { done, value } = await Promise.race([readPromise, timeoutPromise])

      if (done) break
      lastDataTime = Date.now()

      buffer += decoder.decode(value, { stream: true })

      // 解析 SSE 事件：格式为 "data:xxx\n\n"，兼容 \r\n
      let newlineIdx
      while ((newlineIdx = buffer.search(/\r?\n/)) !== -1) {
        let line = buffer.substring(0, newlineIdx)
        buffer = buffer.substring(newlineIdx).replace(/^\r?\n/, '')

        const trimmed = line.trim()
        if (trimmed.startsWith('data:')) {
          let piece = trimmed.substring(5)
          if (piece.startsWith(' ')) piece = piece.substring(1)
          // 识别会话 ID 标记（不显示）
          const cidMatch = piece.match(/^\[conversationId:(\d+)\]$/)
          if (cidMatch) {
            conversationId.value = Number(cidMatch[1])
            localStorage.setItem('ragnest_current_conv', String(cidMatch[1]))
            // 新会话首次创建后，刷新会话列表
            loadConversations()
          } else {
            messages.value[msgIndex].content += piece
            scrollToBottom()
          }
        }
      }
    }

    // 处理 buffer 中残留的最后一段
    const lastLine = buffer.trim()
    if (lastLine.startsWith('data:')) {
      let piece = lastLine.substring(5)
      if (piece.startsWith(' ')) piece = piece.substring(1)
      const cidMatch = piece.match(/^\[conversationId:(\d+)\]$/)
      if (cidMatch) {
        conversationId.value = Number(cidMatch[1])
        localStorage.setItem('ragnest_current_conv', String(cidMatch[1]))
        loadConversations()
      } else {
        messages.value[msgIndex].content += piece
        scrollToBottom()
      }
    }
  } catch (e) {
    messages.value[msgIndex].content = '抱歉，请求出错了，请重试'
  } finally {
    loading.value = false
    scrollToBottom()
    loadConversations()
  }
}

function scrollToBottom() {
  nextTick(() => {
    if (chatBoxRef.value) {
      chatBoxRef.value.scrollTop = chatBoxRef.value.scrollHeight
    }
  })
}
</script>

<style scoped>
.chat-page { display: flex; justify-content: center; gap: 16px; align-items: flex-start; }
.chat-card { width: 100%; max-width: 760px; }

/* 左侧会话列表 */
.conv-list-card { width: 240px; flex-shrink: 0; }
.conv-list-header { display: flex; justify-content: space-between; align-items: center; }
.conv-list { max-height: 560px; overflow-y: auto; }
.conv-item {
  padding: 10px 12px;
  border-radius: 6px;
  cursor: pointer;
  margin-bottom: 4px;
  transition: background 0.2s;
  position: relative;
}
.conv-item:hover { background: #f5f7fa; }
.conv-item.active { background: #ecf5ff; }
.conv-title {
  font-size: 13px;
  color: #303133;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  padding-right: 18px;
}
.conv-time { font-size: 11px; color: #909399; margin-top: 4px; }
.conv-del {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  color: #c0c4cc;
  font-size: 14px;
  opacity: 0;
  transition: opacity 0.2s, color 0.2s;
}
.conv-item:hover .conv-del { opacity: 1; }
.conv-del:hover { color: #f56c6c; }
.conv-empty { text-align: center; color: #c0c4cc; font-size: 13px; padding: 20px 0; }

.card-header { display: flex; justify-content: space-between; align-items: center; }
.header-right { display: flex; align-items: center; }
.chat-box {
  height: 480px;
  overflow-y: auto;
  padding: 16px;
  background: #fafafa;
  border-radius: 8px;
}
.empty { text-align: center; color: #909399; margin-top: 160px; }
.empty .sub { font-size: 13px; margin-top: 8px; }
.msg-row { display: flex; margin-bottom: 16px; align-items: flex-start; }
.msg-row.user { flex-direction: row-reverse; }
.avatar {
  width: 32px; height: 32px; border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
  font-size: 12px; color: #fff; flex-shrink: 0;
}
.msg-row.user .avatar { background: #1677ff; }
.msg-row.assistant .avatar { background: #52c41a; }
.bubble {
  max-width: 70%;
  padding: 10px 14px;
  border-radius: 8px;
  line-height: 1.6;
  white-space: pre-wrap;
  word-break: break-word;
}
.msg-row.user .bubble { background: #1677ff; color: #fff; margin-right: 8px; }
.msg-row.assistant .bubble { background: #fff; border: 1px solid #e5e7eb; margin-left: 8px; }
.cursor {
  display: inline-block;
  animation: blink 1s infinite;
  color: #1677ff;
}
@keyframes blink {
  0%, 50% { opacity: 1; }
  50.01%, 100% { opacity: 0; }
}
.input-area { margin-top: 16px; }
</style>
