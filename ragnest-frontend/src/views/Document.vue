<template>
  <div>
    <el-card>
      <template #header>
        <div class="card-header">
          <span>文档管理</span>
          <div class="upload-area">
            <el-select v-model="selectedKb" placeholder="选择知识库" style="width: 200px; margin-right: 12px" @change="load">
              <el-option v-for="kb in kbList" :key="kb.id" :label="kb.name" :value="kb.id" />
            </el-select>
            <el-upload
              :show-file-list="false"
              :http-request="handleUpload"
              :disabled="!selectedKb"
              multiple
            >
              <el-button type="primary" :disabled="!selectedKb">上传文档</el-button>
            </el-upload>
          </div>
        </div>
      </template>

      <div class="tip">支持 md / txt / pdf / docx / xlsx / csv</div>

      <el-table :data="docs" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="name" label="文件名" min-width="200" />
        <el-table-column prop="fileType" label="类型" width="100" />
        <el-table-column prop="chunkCount" label="切片数" width="100" />
        <el-table-column prop="createdAt" label="上传时间" min-width="180">
          <template #default="{ row }">{{ formatTime(row.createdAt) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { UploadRequestOptions } from 'element-plus'
import { listKnowledgeBases } from '../api/knowledgeBase'
import { listDocuments, uploadDocument, deleteDocument } from '../api/document'
import type { KnowledgeBase, Document } from '../types'

const kbList = ref<KnowledgeBase[]>([])
const selectedKb = ref<number | undefined>()
const docs = ref<Document[]>([])
const loading = ref(false)

async function loadKbs() {
  kbList.value = await listKnowledgeBases()
  if (kbList.value.length > 0 && !selectedKb.value) {
    selectedKb.value = kbList.value[0].id
  }
}

async function load() {
  if (!selectedKb.value) return
  loading.value = true
  try {
    docs.value = await listDocuments(selectedKb.value)
  } finally {
    loading.value = false
  }
}

async function handleUpload(options: UploadRequestOptions) {
  if (!selectedKb.value) return
  const file = options.file as File
  try {
    await uploadDocument(selectedKb.value, file)
    ElMessage.success(`${file.name} 上传成功`)
    load()
  } catch (e) {
    // 错误已在拦截器提示
  }
}

async function handleDelete(row: Document) {
  await ElMessageBox.confirm(`确认删除文档「${row.name}」？`, '提示', { type: 'warning' })
  await deleteDocument(row.id)
  ElMessage.success('删除成功')
  load()
}

function formatTime(s: string) {
  return s ? s.replace('T', ' ').substring(0, 19) : ''
}

onMounted(async () => {
  await loadKbs()
  await load()
})
</script>

<style scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
.upload-area { display: flex; align-items: center; }
.tip { color: #909399; font-size: 13px; margin-bottom: 16px; }
</style>
