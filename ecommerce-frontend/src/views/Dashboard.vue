<template>
  <div class="page-container" v-loading="loading">
    <el-card class="panel-card banner-card" shadow="never">
      <h2 class="banner-title">首页统计看板</h2>
      <p class="banner-text">用于查看商家、商品、库存、价格和订单状态等基础统计数据。</p>
    </el-card>

    <el-card class="panel-card section-card" shadow="never">
      <template #header>
        <div class="section-title">基础统计</div>
      </template>
      <el-row :gutter="16">
        <el-col :xs="24" :sm="12" :md="6">
          <div class="metric-block">
            <div class="metric-label">商家总数</div>
            <div class="metric-value">{{ stats.merchantCount }}</div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <div class="metric-block">
            <div class="metric-label">分类总数</div>
            <div class="metric-value">{{ stats.categoryCount }}</div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <div class="metric-block">
            <div class="metric-label">商品总数</div>
            <div class="metric-value">{{ stats.productCount }}</div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <div class="metric-block">
            <div class="metric-label">订单总数</div>
            <div class="metric-value">{{ stats.orderCount }}</div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <el-row :gutter="16">
      <el-col :xs="24" :md="12">
        <el-card class="panel-card section-card stock-card" shadow="hover">
          <template #header>
            <div class="section-title">库存预警</div>
          </template>
          <div class="alert-value">{{ stats.lowStockCount }}</div>
          <div class="section-desc">库存低于 10 的商品数量</div>
        </el-card>
      </el-col>

      <el-col :xs="24" :md="12">
        <el-card class="panel-card section-card" shadow="hover">
          <template #header>
            <div class="section-title">订单状态</div>
          </template>
          <div class="status-row">
            <div class="status-item">
              <div class="status-label">待处理订单</div>
              <div class="status-value pending">{{ stats.pendingOrderCount }}</div>
            </div>
            <div class="status-item">
              <div class="status-label">已完成订单</div>
              <div class="status-value finished">{{ stats.finishedOrderCount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="panel-card section-card" shadow="never">
      <template #header>
        <div class="section-title">价格概览</div>
      </template>
      <el-row :gutter="16">
        <el-col :xs="24" :sm="8">
          <div class="price-item">
            <div class="metric-label">最高价</div>
            <div class="price-value">¥{{ formatPrice(stats.maxPrice) }}</div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="8">
          <div class="price-item">
            <div class="metric-label">最低价</div>
            <div class="price-value">¥{{ formatPrice(stats.minPrice) }}</div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="8">
          <div class="price-item">
            <div class="metric-label">平均价</div>
            <div class="price-value">¥{{ formatPrice(stats.avgPrice) }}</div>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../api/request'

const loading = ref(false)

const stats = reactive({
  merchantCount: 0,
  categoryCount: 0,
  productCount: 0,
  orderCount: 0,
  lowStockCount: 0,
  maxPrice: 0,
  minPrice: 0,
  avgPrice: 0,
  pendingOrderCount: 0,
  finishedOrderCount: 0
})

const formatPrice = (value) => {
  const num = Number(value || 0)
  return num.toFixed(2)
}

const loadCount = async () => {
  loading.value = true
  try {
    const res = await request.get('/dashboard/count')
    if (res.data?.code === 200 && res.data?.data) {
      Object.assign(stats, res.data.data)
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

.section-card {
  border: none;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
}

.metric-block {
  border-radius: 10px;
  background: #f8fafc;
  padding: 14px;
  min-height: 96px;
}

.metric-label {
  color: #6b7280;
  font-size: 13px;
}

.metric-value {
  margin-top: 10px;
  font-size: 30px;
  font-weight: 700;
  color: #111827;
}

.stock-card {
  background: linear-gradient(160deg, #fff7ed 0%, #ffffff 100%);
}

.alert-value {
  font-size: 40px;
  color: #ea580c;
  font-weight: 700;
  margin-top: 8px;
}

.section-desc {
  margin-top: 8px;
  color: #6b7280;
  font-size: 13px;
}

.status-row {
  display: flex;
  gap: 14px;
}

.status-item {
  flex: 1;
  padding: 12px;
  border-radius: 10px;
  background: #f8fafc;
}

.status-label {
  font-size: 13px;
  color: #6b7280;
}

.status-value {
  margin-top: 10px;
  font-size: 30px;
  font-weight: 700;
}

.pending {
  color: #d97706;
}

.finished {
  color: #16a34a;
}

.price-item {
  border-radius: 10px;
  padding: 14px;
  background: #f8fafc;
}

.price-value {
  margin-top: 10px;
  font-size: 30px;
  font-weight: 700;
  color: #1d4ed8;
}
</style>
