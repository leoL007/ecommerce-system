<template>
  <div class="page-container" v-loading="loading">
    <el-card class="panel-card banner-card" shadow="never">
      <h2 class="banner-title">首页统计看板</h2>
      <p class="banner-text">用于查看商家后台核心数据概览，便于快速了解当前业务状态。</p>
    </el-card>

    <el-row :gutter="16">
      <el-col :xs="24" :sm="12" :md="8">
        <el-card class="panel-card stat-card category" shadow="hover">
          <div class="stat-label">分类总数</div>
          <div class="stat-number">{{ stats.categoryCount }}</div>
          <div class="stat-desc">当前系统中已维护的商品分类数量</div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="8">
        <el-card class="panel-card stat-card product" shadow="hover">
          <div class="stat-label">商品总数</div>
          <div class="stat-number">{{ stats.productCount }}</div>
          <div class="stat-desc">当前系统中已录入的商品数量</div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="8">
        <el-card class="panel-card stat-card order" shadow="hover">
          <div class="stat-label">订单总数</div>
          <div class="stat-number">{{ stats.orderCount }}</div>
          <div class="stat-desc">当前系统中已生成的订单数量</div>
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
.banner-card {
  border: none;
  background: linear-gradient(130deg, #eef4ff 0%, #f7fbff 65%, #ffffff 100%);
}

.banner-title {
  margin: 0;
  font-size: 24px;
  color: #1f2a37;
}

.banner-text {
  margin: 10px 0 0;
  color: #4b5563;
  font-size: 14px;
}

.stat-card {
  border: none;
  transition: transform 0.2s ease;
}

.stat-card:hover {
  transform: translateY(-3px);
}

.stat-label {
  color: #6b7280;
  font-size: 14px;
}

.stat-number {
  margin-top: 12px;
  font-size: 34px;
  font-weight: 700;
  color: #111827;
}

.stat-desc {
  margin-top: 10px;
  font-size: 12px;
  color: #6b7280;
}

.category {
  border-left: 4px solid #3b82f6;
}

.product {
  border-left: 4px solid #10b981;
}

.order {
  border-left: 4px solid #f59e0b;
}
</style>
