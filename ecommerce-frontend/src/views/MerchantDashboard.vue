<template>
  <div class="page-container">
    <el-card class="panel-card" shadow="never">
      <h2 class="page-title">商家首页</h2>
      <p class="page-desc">查看我的商品和订单数据概览。</p>
    </el-card>

    <el-row :gutter="16" v-loading="loading">
      <el-col :xs="24" :sm="12">
        <el-card class="panel-card metric-card" shadow="hover">
          <div class="metric-label">我的商品数</div>
          <div class="metric-value">{{ stats.productCount }}</div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12">
        <el-card class="panel-card metric-card" shadow="hover">
          <div class="metric-label">我的订单数</div>
          <div class="metric-value">{{ stats.orderCount }}</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../api/request'

const router = useRouter()
const loading = ref(false)
const stats = reactive({ productCount: 0, orderCount: 0 })

const getMerchantId = () => Number(localStorage.getItem('merchantId') || 0)

const loadData = async () => {
  const merchantId = getMerchantId()
  if (!merchantId) {
    ElMessage.warning('请先登录商家账号')
    router.push('/merchant/login')
    return
  }

  loading.value = true
  try {
    const [productRes, orderRes] = await Promise.all([
      request.get(`/product/listByMerchant/${merchantId}`),
      request.get(`/order/listByMerchant/${merchantId}`)
    ])

    stats.productCount = productRes.data?.code === 200 ? (productRes.data?.data || []).length : 0
    stats.orderCount = orderRes.data?.code === 200 ? (orderRes.data?.data || []).length : 0
  } catch (error) {
    ElMessage.error('请求失败，请检查后端服务')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.metric-card {
  min-height: 130px;
}

.metric-label {
  color: #6b7280;
  font-size: 14px;
}

.metric-value {
  margin-top: 14px;
  font-size: 36px;
  font-weight: 700;
  color: #0f766e;
}
</style>
