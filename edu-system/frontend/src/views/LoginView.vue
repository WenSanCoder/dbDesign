<template>
  <div class="login-page">
    <div class="login-header">
      <img class="login-logo" :src="logoUrl" alt="浙江工业大学" />
      <span>浙江工业大学反方教务管理系统</span>
    </div>
    <div class="login-box">
      <h1>用户登录</h1>
      <el-form label-position="top" @submit.prevent="login">
        <el-form-item label="账号">
          <el-input v-model="form.username" placeholder="请输入账号" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" />
        </el-form-item>
        <el-alert
          title="演示账号：admin、teacher1、student1；密码均为 123456"
          type="info"
          :show-icon="false"
          :closable="false"
          class="login-tip"
        />
        <el-button type="primary" class="login-button" :loading="loading" @click="login">登录</el-button>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { apiPost } from '../api/http'
import { useSessionStore, type SessionUser } from '../stores/session'

const router = useRouter()
const session = useSessionStore()
const loading = ref(false)
const logoUrl = new URL('../../static/zjut.png', import.meta.url).href
const form = reactive({ username: 'admin', password: '123456' })

async function login() {
  loading.value = true
  try {
    const user = await apiPost<SessionUser>('/auth/login', form)
    session.setUser(user)
    router.push('/')
  } catch (error) {
    ElMessage.error((error as Error).message)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  background: #eef2f5;
}

.login-header {
  height: 66px;
  padding: 0 90px;
  display: flex;
  align-items: center;
  gap: 12px;
  background: #0787cf;
  color: #fff;
  font-size: 24px;
  font-weight: 600;
}

.login-logo {
  width: 42px;
  height: 42px;
  object-fit: contain;
  background: #fff;
  border: 1px solid rgba(255, 255, 255, 0.8);
}

.login-box {
  width: 420px;
  margin: 84px auto 0;
  padding: 28px;
  background: #fff;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

.login-box h1 {
  margin: 0 0 22px;
  font-size: 22px;
  font-weight: 600;
}

.login-tip {
  margin-bottom: 16px;
}

.login-button {
  width: 100%;
}
</style>
