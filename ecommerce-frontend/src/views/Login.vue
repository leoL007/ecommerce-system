<template>
  <div class="login-page">
    <div class="bg-shape"></div>
    <el-card class="login-card panel-card" shadow="never">
      <div class="login-head">
        <h2>商家后台登录</h2>
        <p>请输入账号信息进入商家后台管理系统</p>
      </div>

      <el-form :model="form" label-position="top" class="login-form">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" size="large" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" size="large" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" class="login-btn" @click="handleLogin">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../api/request'

const router = useRouter()

const form = reactive({
  username: '',
  password: ''
})

const handleLogin = async () => {
  if (!form.username || !form.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }

  try {
    const res = await request.post('/login', form)
    if (res.data?.code === 200 && res.data?.data === true) {
      ElMessage.success('登录成功')
      router.push('/dashboard')
      return
    }
    ElMessage.error(res.data?.message || '登录失败')
  } catch (error) {
    ElMessage.error('请求失败，请检查后端服务')
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  padding: 20px;
  background: linear-gradient(145deg, #eef3ff 0%, #f7faff 45%, #f3f6fb 100%);
}

.bg-shape {
  position: absolute;
  width: 420px;
  height: 420px;
  right: 8%;
  top: 8%;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(37, 99, 235, 0.14) 0%, rgba(37, 99, 235, 0.03) 55%, transparent 75%);
  pointer-events: none;
}

.login-card {
  width: 430px;
  z-index: 1;
  border: none;
}

.login-head h2 {
  margin: 0;
  font-size: 26px;
  color: #1f2937;
  text-align: center;
}

.login-head p {
  margin: 10px 0 0;
  font-size: 13px;
  color: #6b7280;
  text-align: center;
}

.login-form {
  margin-top: 14px;
}

.login-btn {
  width: 100%;
  font-weight: 600;
}
</style>
