import { get, post, put, del } from './request'
import type { KnowledgeBase } from '../types'

export function listKnowledgeBases(): Promise<KnowledgeBase[]> {
  return get<KnowledgeBase[]>('/api/knowledge-bases')
}

export function createKnowledgeBase(data: Partial<KnowledgeBase>): Promise<KnowledgeBase> {
  return post<KnowledgeBase>('/api/knowledge-bases', data)
}

export function updateKnowledgeBase(id: number, data: Partial<KnowledgeBase>): Promise<KnowledgeBase> {
  return put<KnowledgeBase>(`/api/knowledge-bases/${id}`, data)
}

export function deleteKnowledgeBase(id: number): Promise<void> {
  return del<void>(`/api/knowledge-bases/${id}`)
}
