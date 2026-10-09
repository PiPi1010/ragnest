import axios, { type AxiosInstance, type InternalAxiosRequestConfig, type AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'
import type { Result } from '../types'
import router from '../router'

const request: AxiosInstance = axios.create({
  baseURL: '/',
  timeout: 120000
})

// 请求拦截器：自动附加 token 和租户 ID
request.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  const token = localStorage.getItem('ragnest_token')
  const tenantId = localStorage.getItem('ragnest_tenant') || 'default'
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  config.headers['X-Tenant-Id'] = tenantId
  return config
})

// 响应拦截器：统一处理 Result 和 401
request.interceptors.response.use(
  (response: AxiosResponse<Result>) => {
    const res = response.data
    if (res.code !== 0) {
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    return response
  },
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('ragnest_token')
      ElMessage.error('登录已过期，请重新登录')
      router.push('/login')
    } else {
      ElMessage.error(error.message || '网络错误')
    }
    return Promise.reject(error)
  }
)

// 泛型封装：直接返回 data
export async function get<T>(url: string, params?: any): Promise<T> {
  const res = await request.get<Result<T>>(url, { params })
  return res.data.data
}

export async function post<T>(url: string, data?: any): Promise<T> {
  const res = await request.post<Result<T>>(url, data)
  return res.data.data
}

export async function put<T>(url: string, data?: any): Promise<T> {
  const res = await request.put<Result<T>>(url, data)
  return res.data.data
}

export async function del<T>(url: string): Promise<T> {
  const res = await request.delete<Result<T>>(url)
  return res.data.data
}

// 文件上传专用
export async function upload<T>(url: string, formData: FormData): Promise<T> {
  const res = await request.post<Result<T>>(url, formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
  return res.data.data
}

export default request
