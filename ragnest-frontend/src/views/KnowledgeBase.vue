<template>
  <div>
    <el-card>
      <template #header>
        <div class="card-header">
          <span>知识库列表</span>
          <el-button type="primary" @click="openDialog">新建知识库</el-button>
        </div>
      </template>

      <el-table :data="list" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="name" label="名称" min-width="150" />
        <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="embeddingModel" label="嵌入模型" width="130" />
        <el-table-column prop="vectorDimension" label="向量维度" width="100" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" title="新建知识库" width="480px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="名称" required>
          <el-input v-model="form.name" placeholder="请输入知识库名称" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="描述（可选）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="creating" @click="handleCreate">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listKnowledgeBases, createKnowledgeBase, deleteKnowledgeBase } from '../api/knowledgeBase'
import type { KnowledgeBase } from '../types'

const list = ref<KnowledgeBase[]>([])
const loading = ref(false)
const creating = ref(false)
const dialogVisible = ref(false)
const form = ref({ name: '', description: '' })

async function load() {
  loading.value = true
  try {
    list.value = await listKnowledgeBases()
  } finally {
    loading.value = false
  }
}

function openDialog() {
  form.value = { name: '', description: '' }
  dialogVisible.value = true
}

async function handleCreate() {
  if (!form.value.name.trim()) {
    ElMessage.warning('请输入名称')
    return
  }
  creating.value = true
  try {
    await createKnowledgeBase({
      name: form.value.name,
      description: form.value.description,
      vectorDimension: 1024,
      embeddingModel: 'bge-m3'
    })
    ElMessage.success('创建成功')
    dialogVisible.value = false
    load()
  } finally {
    creating.value = false
  }
}

async function handleDelete(row: KnowledgeBase) {
  await ElMessageBox.confirm(`确认删除知识库「${row.name}」？`, '提示', { type: 'warning' })
  await deleteKnowledgeBase(row.id)
  ElMessage.success('删除成功')
  load()
}

onMounted(load)
</script>

<style scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>
