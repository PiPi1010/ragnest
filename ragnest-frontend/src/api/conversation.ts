import { get, post, del } from './request'
import type { Conversation, ChatRequest, ChatResponse } from '../types'

export function listConversations(): Promise<Conversation[]> {
  return get<Conversation[]>('/api/conversations')
}

export function getConversation(id: number): Promise<Conversation> {
  return get<Conversation>(`/api/conversations/${id}`)
}

export function chat(data: ChatRequest): Promise<ChatResponse> {
  return post<ChatResponse>('/api/conversations/chat', data)
}

export function deleteConversation(id: number): Promise<void> {
  return del<void>(`/api/conversations/${id}`)
}
