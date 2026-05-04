<template>
  <div class="page-container" v-loading="loading">
    <el-card class="panel-card banner-card" shadow="never">
      <h2 class="banner-title">商家首页</h2>
      <p class="banner-text">查看我的商品、订单与库存情况</p>
    </el-card>

    <el-card class="panel-card section-card" shadow="never">
      <template #header>
        <div class="section-title">核心统计</div>
      </template>
      <el-row :gutter="16">
        <el-col :xs="24" :sm="12" :md="8" :lg="4">
          <div class="metric-block">
            <div class="metric-label">我的商品数</div>
            <div class="metric-value">{{ summary.productCount }}</div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="4">
          <div class="metric-block">
            <div class="metric-label">在售商品数</div>
            <div class="metric-value">{{ summary.activeProductCount }}</div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="4">
          <div class="metric-block warning-block">
            <div class="metric-label">低库存商品数</div>
            <div class="metric-value warning-value">{{ summary.lowStockCount }}</div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="4">
          <div class="metric-block">
            <div class="metric-label">我的订单数</div>
            <div class="metric-value">{{ summary.orderCount }}</div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="4">
          <div class="metric-block pending-block">
            <div class="metric-label">待处理订单数</div>
            <div class="metric-value pending-value">{{ summary.pendingOrderCount }}</div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="4">
          <div class="metric-block finished-block">
            <div class="metric-label">已完成订单数</div>
            <div class="metric-value finished-value">{{ summary.finishedOrderCount }}</div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <el-row :gutter="16">
      <el-col :xs="24" :lg="10">
        <el-card class="panel-card section-card" shadow="hover">
          <template #header>
            <div class="section-title">库存预警</div>
          </template>

          <div class="low-stock-count">低库存商品数量：{{ summary.lowStockCount }}</div>

          <el-table :data="lowStockList" stripe size="small">
            <el-table-column prop="name" label="商品名称" min-width="140" show-overflow-tooltip />
            <el-table-column prop="stock" label="库存数量" width="100" />
          </el-table>
        </el-card>
      </el-col>

      <el-col :xs="24" :lg="14">
        <el-card class="panel-card section-card" shadow="hover">
          <template #header>
            <div class="section-title">最近订单</div>
          </template>

          <el-table :data="recentOrders" stripe size="small">
            <el-table-column prop="orderNo" label="订单号" min-width="130" show-overflow-tooltip />
            <el-table-column prop="productName" label="商品名称" min-width="120" show-overflow-tooltip />
            <el-table-column prop="quantity" label="数量" width="80" />
            <el-table-column label="订单状态" width="100">
              <template #default="{ row }">
                <el-tag :type="statusTagType(row.status)" effect="light">{{ row.status || '-' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" min-width="150" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  getMerchantDashboardLowStock,
  getMerchantDashboardRecentOrders,
  getMerchantDashboardSummary
} from '../api/merchantDashboard'

const router = useRouter()
const loading = ref(false)
const lowStockList = ref([])
const recentOrders = ref([])

const summary = reactive({
  productCount: 0,
  activeProductCount: 0,
  lowStockCount: 0,
  orderCount: 0,
  pendingOrderCount: 0,
  finishedOrderCount: 0
})

const getMerchantId = () => Number(localStorage.getItem('merchantId') || 0)

const statusTagType = (status) => {
  if (status === '已完成') return 'success'
  if (status === '未处理' || status === '待处理' || status === '待支付') return 'warning'
  return 'info'
}

const loadDashboardData = async () => {
  const merchantId = getMerchantId()
  if (!merchantId) {
    ElMessage.warning('请先登录商家账号')
    router.push('/merchant/login')
    return
  }

  loading.value = true
  try {
    const [summaryRes, lowStockRes, recentRes] = await Promise.all([
      getMerchantDashboardSummary(merchantId),
      getMerchantDashboardLowStock(merchantId, 5),
      getMerchantDashboardRecentOrders(merchantId, 5)
    ])

    if (summaryRes.data?.code === 200 && summaryRes.data?.data) {
      Object.assign(summary, summaryRes.data.data)
    }

    if (lowStockRes.data?.code === 200) {
      lowStockList.value = lowStockRes.data?.data || []
    }

    if (recentRes.data?.code === 200) {
      recentOrders.value = recentRes.data?.data || []
    }
  } catch (error) {
    ElMessage.error('请求失败，请检查后端服务')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadDashboardData()
})
</script>

<style scoped>
.banner-card {
  border: none;
  background: linear-gradient(130deg, #e9fbf8 0%, #f5fdfa 65%, #ffffff 100%);
}

.banner-title {
  margin: 0;
  font-size: 24px;
  color: #134e4a;
}

.banner-text {
  margin: 10px 0 0;
  color: #3f6360;
  font-size: 14px;
}

.section-card {
  border: none;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #0f766e;
}

.metric-block {
  border-radius: 10px;
  background: #f8fafc;
  padding: 14px;
  min-height: 96px;
}

.warning-block {
  background: #fff7ed;
}

.pending-block {
  background: #fffbeb;
}

.finished-block {
  background: #f0fdf4;
}

.metric-label {
  color: #6b7280;
  font-size: 13px;
}

.metric-value {
  margin-top: 10px;
  font-size: 30px;
  font-weight: 700;
  color: #0f766e;
}

.warning-value {
  color: #ea580c;
}

.pending-value {
  color: #d97706;
}

.finished-value {
  color: #16a34a;
}

.low-stock-count {
  margin-bottom: 10px;
  font-size: 14px;
  color: #4b5563;
}
</style>
