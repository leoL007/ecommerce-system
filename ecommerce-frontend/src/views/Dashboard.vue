<template>
  <div class="dashboard-page" v-loading="loading">
    <el-card class="welcome-card" shadow="never">
      <h2>首页概览</h2>
      <p>欢迎使用电商后台管理系统</p>
    </el-card>

    <el-row :gutter="16" class="stats-row">
      <el-col :xs="24" :sm="12" :md="8">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-title">分类总数</div>
          <div class="stat-value">{{ stats.categoryCount }}</div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="8">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-title">商品总数</div>
          <div class="stat-value">{{ stats.productCount }}</div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="8">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-title">订单总数</div>
          <div class="stat-value">{{ stats.orderCount }}</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../api/request'

const loading = ref(false)

const stats = reactive({
  categoryCount: 0,
  productCount: 0,
  orderCount: 0
})

const loadCount = async () => {
  loading.value = true
  try {
    const res = await request.get('/dashboard/count')
    if (res.data?.code === 200 && res.data?.data) {
      stats.categoryCount = res.data.data.categoryCount || 0
      stats.productCount = res.data.data.productCount || 0
      stats.orderCount = res.data.data.orderCount || 0
      return
    }
    ElMessage.error(res.data?.message || '获取统计数据失败')
  } catch (error) {
    ElMessage.error('请求失败，请检查后端服务')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadCount()
})
</script>

<style scoped>
.dashboard-page {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.welcome-card h2 {
  margin: 0 0 8px;
  font-size: 22px;
}

.welcome-card p {
  margin: 0;
  color: #606266;
}

.stats-row {
  margin-bottom: 0;
}

.stat-card {
  border-radius: 8px;
}

.stat-title {
  color: #909399;
  font-size: 14px;
}

.stat-value {
  margin-top: 12px;
  font-size: 30px;
  font-weight: 600;
  color: #303133;
}
</style>
