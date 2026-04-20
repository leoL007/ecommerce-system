<template>
  <div class="login-page">
    <el-card class="login-card">
      <h2>后台登录</h2>
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin">登录</el-button>
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
  background: #eef2f7;
}

.login-card {
  width: 420px;
}

h2 {
  margin: 0 0 20px;
  text-align: center;
}
</style>
