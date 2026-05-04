<template>
  <div class="page-container">
    <div class="page-head">
      <div>
        <h2 class="page-title">全站商品列表</h2>
        <p class="page-desc">平台管理员用于查看全站商品数据。商品上架与维护以商家后台“我的商品”为主。</p>
      </div>
    </div>

    <el-card class="panel-card table-wrap" shadow="never">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="商品图片" width="110">
          <template #default="{ row }">
            <el-image
              v-if="getImage(row)"
              :src="getImage(row)"
              style="width: 56px; height: 56px; border-radius: 6px"
              fit="cover"
              :preview-src-list="[getImage(row)]"
              preview-teleported
            />
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="商品名称" min-width="140" />
        <el-table-column prop="price" label="价格" width="120" />
        <el-table-column prop="stock" label="库存" width="100" />
        <el-table-column prop="categoryId" label="分类ID" width="100" />
        <el-table-column prop="merchantId" label="商家ID" width="100" />
        <el-table-column prop="description" label="商品描述" min-width="180" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" min-width="180" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../api/request'

const loading = ref(false)
const tableData = ref([])

const getImage = (row) => row.imageUrl || row.image_url || ''

const loadList = async () => {
  loading.value = true
  try {
    const res = await request.get('/product/list')
    if (res.data?.code === 200) {
      tableData.value = res.data?.data || []
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
  loadList()
})
</script>
