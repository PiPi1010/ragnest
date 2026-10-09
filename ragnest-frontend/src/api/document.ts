import { get, upload, del } from './request'
import type { Document } from '../types'

export function listDocuments(knowledgeBaseId: number): Promise<Document[]> {
  return get<Document[]>('/api/documents', { knowledgeBaseId })
}

export function uploadDocument(knowledgeBaseId: number, file: File): Promise<Document> {
  const fd = new FormData()
  fd.append('file', file)
  return upload<Document>(`/api/documents/upload?knowledgeBaseId=${knowledgeBaseId}`, fd)
}

export function deleteDocument(id: number): Promise<void> {
  return del<void>(`/api/documents/${id}`)
}
