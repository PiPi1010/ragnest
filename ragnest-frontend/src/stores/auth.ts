import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login as loginApi } from '../api/auth'

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string>(localStorage.getItem('ragnest_token') || '')
  const tenantId = ref<string>(localStorage.getItem('ragnest_tenant') || 'default')
  const username = ref<string>(localStorage.getItem('ragnest_username') || '')

  async function login(user: string, pwd: string) {
    const res = await loginApi({ username: user, password: pwd })
    token.value = res.token
    tenantId.value = res.tenantId || 'default'
    username.value = res.username
    localStorage.setItem('ragnest_token', res.token)
    localStorage.setItem('ragnest_tenant', res.tenantId || 'default')
    localStorage.setItem('ragnest_username', res.username)
  }

  function logout() {
    token.value = ''
    username.value = ''
    localStorage.removeItem('ragnest_token')
    localStorage.removeItem('ragnest_username')
  }

  function isLoggedIn() {
    return !!token.value
  }

  return { token, tenantId, username, login, logout, isLoggedIn }
})
