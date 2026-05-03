<template>
  <div class="page-container">
    <div class="page-head">
      <div>
        <h2 class="page-title">我的订单</h2>
        <p class="page-desc">仅展示当前商家的订单记录。</p>
      </div>
    </div>

    <el-card class="panel-card table-wrap" shadow="never">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="orderNo" label="订单号" min-width="160" />
        <el-table-column prop="userName" label="用户" min-width="110" />
        <el-table-column prop="productName" label="商品名称" min-width="130" />
        <el-table-column prop="quantity" label="数量" width="90" />
        <el-table-column prop="totalPrice" label="总价" width="120" />
        <el-table-column prop="status" label="状态" width="120" />
        <el-table-column prop="createTime" label="创建时间" min-width="180" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../api/request'

const router = useRouter()
const loading = ref(false)
const tableData = ref([])

const getMerchantId = () => Number(localStorage.getItem('merchantId') || 0)

const loadList = async () => {
  const merchantId = getMerchantId()
  if (!merchantId) {
    ElMessage.warning('请先登录商家账号')
    router.push('/merchant/login')
    return
  }

  loading.value = true
  try {
    const res = await request.get(`/order/listByMerchant/${merchantId}`)
    if (res.data?.code === 200) {
      tableData.value = res.data?.data || []
      return
    }
    ElMessage.error(res.data?.message || '获取订单列表失败')
  } catch (error) {
    ElMessage.error('请求失败，请检查后端服务')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadList()
})
</script>
