<template>
  <div class="shop-page" v-loading="loading">
    <div class="shop-head">
      <h1>前台商品展示</h1>
      <p>简单商品浏览页，不包含购物车和支付流程。</p>
    </div>

    <el-row :gutter="16">
      <el-col v-for="item in products" :key="item.id" :xs="24" :sm="12" :md="8" :lg="6">
        <el-card class="product-card" shadow="hover">
          <el-image
            v-if="getImage(item)"
            :src="getImage(item)"
            fit="cover"
            class="product-image"
            :preview-src-list="[getImage(item)]"
            preview-teleported
          />
          <div v-else class="image-placeholder">暂无图片</div>

          <h3 class="product-name">{{ item.name || '-' }}</h3>
          <div class="product-meta">价格：¥{{ item.price ?? 0 }}</div>
          <div class="product-meta">库存：{{ item.stock ?? 0 }}</div>
          <p class="product-desc">{{ item.description || '暂无描述' }}</p>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../api/request'

const loading = ref(false)
const products = ref([])

const getImage = (item) => item.imageUrl || item.image_url || ''

const loadProducts = async () => {
  loading.value = true
  try {
    const res = await request.get('/product/list')
    if (res.data?.code === 200) {
      products.value = res.data?.data || []
      return
    }
    ElMessage.error(res.data?.message || '获取商品列表失败')
  } catch (error) {
    ElMessage.error('请求失败，请检查后端服务')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadProducts()
})
</script>

<style scoped>
.shop-page {
  min-height: 100vh;
  padding: 24px;
  background: #f6f8fb;
}

.shop-head {
  margin-bottom: 18px;
}

.shop-head h1 {
  margin: 0;
  font-size: 28px;
  color: #1f2937;
}

.shop-head p {
  margin: 8px 0 0;
  color: #6b7280;
}

.product-card {
  margin-bottom: 16px;
  border-radius: 10px;
}

.product-image {
  width: 100%;
  height: 180px;
  border-radius: 8px;
}

.image-placeholder {
  width: 100%;
  height: 180px;
  border-radius: 8px;
  background: #eef2f7;
  color: #9ca3af;
  display: flex;
  align-items: center;
  justify-content: center;
}

.product-name {
  margin: 12px 0 8px;
  font-size: 17px;
  color: #111827;
}

.product-meta {
  margin-top: 4px;
  color: #374151;
  font-size: 14px;
}

.product-desc {
  margin: 8px 0 0;
  color: #6b7280;
  font-size: 13px;
  line-height: 1.5;
}
</style>
