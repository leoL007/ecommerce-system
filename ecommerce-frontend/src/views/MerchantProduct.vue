<template>
  <div class="page-container">
    <div class="page-head">
      <div>
        <h2 class="page-title">我的商品</h2>
        <p class="page-desc">仅展示并维护当前商家的商品数据。</p>
      </div>
      <el-button type="primary" class="toolbar-btn" @click="openAddDialog">新增商品</el-button>
    </div>

    <el-card class="panel-card table-wrap" shadow="never">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="商品图片" width="110">
          <template #default="{ row }">
            <el-image
              v-if="row.imageUrl"
              :src="row.imageUrl"
              style="width: 56px; height: 56px; border-radius: 6px"
              fit="cover"
              :preview-src-list="[row.imageUrl]"
              preview-teleported
            />
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="商品名称" min-width="140" />
        <el-table-column prop="price" label="价格" width="120" />
        <el-table-column prop="stock" label="库存" width="100" />
        <el-table-column prop="categoryId" label="分类ID" width="100" />
        <el-table-column prop="description" label="描述" min-width="180" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" min-width="180" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" class="action-btn" @click="openEditDialog(row)">编辑</el-button>
            <el-button link type="danger" class="action-btn" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>

  <el-dialog v-model="dialogVisible" :title="dialogTitle" width="560px">
    <el-form :model="form" label-width="90px">
      <el-form-item label="商品名称">
        <el-input v-model="form.name" placeholder="请输入商品名称" />
      </el-form-item>
      <el-form-item label="价格">
        <el-input-number v-model="form.price" :min="0" :precision="2" :step="1" style="width: 100%" />
      </el-form-item>
      <el-form-item label="库存">
        <el-input-number v-model="form.stock" :min="0" :step="1" style="width: 100%" />
      </el-form-item>
      <el-form-item label="分类ID">
        <el-input-number v-model="form.categoryId" :min="1" :step="1" style="width: 100%" />
      </el-form-item>
      <el-form-item label="图片地址">
        <el-input v-model="form.imageUrl" placeholder="请输入 image_url" />
      </el-form-item>
      <el-form-item label="商品描述">
        <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入商品描述" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitForm">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../api/request'

const router = useRouter()
const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)

const form = reactive({
  id: null,
  name: '',
  price: 0,
  stock: 0,
  categoryId: null,
  description: '',
  imageUrl: ''
})

const dialogTitle = computed(() => (isEdit.value ? '编辑商品' : '新增商品'))

const getMerchantId = () => Number(localStorage.getItem('merchantId') || 0)

const resetForm = () => {
  form.id = null
  form.name = ''
  form.price = 0
  form.stock = 0
  form.categoryId = null
  form.description = ''
  form.imageUrl = ''
}

const loadList = async () => {
  const merchantId = getMerchantId()
  if (!merchantId) {
    ElMessage.warning('请先登录商家账号')
    router.push('/merchant/login')
    return
  }

  loading.value = true
  try {
    const res = await request.get(`/product/listByMerchant/${merchantId}`)
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

const openAddDialog = () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

const openEditDialog = (row) => {
  isEdit.value = true
  form.id = row.id
  form.name = row.name || ''
  form.price = Number(row.price ?? 0)
  form.stock = Number(row.stock ?? 0)
  form.categoryId = row.categoryId
  form.description = row.description || ''
  form.imageUrl = row.imageUrl || ''
  dialogVisible.value = true
}

const submitForm = async () => {
  const merchantId = getMerchantId()
  if (!merchantId) {
    ElMessage.warning('请先登录商家账号')
    router.push('/merchant/login')
    return
  }

  if (!form.name.trim()) {
    ElMessage.warning('请输入商品名称')
    return
  }

  const payload = {
    id: form.id,
    name: form.name,
    price: form.price,
    stock: form.stock,
    categoryId: form.categoryId,
    description: form.description,
    imageUrl: form.imageUrl,
    merchantId
  }

  try {
    const api = isEdit.value ? request.put : request.post
    const url = isEdit.value ? '/product/update' : '/product/add'
    const res = await api(url, payload)
    if (res.data?.code === 200 && res.data?.data === true) {
      ElMessage.success(isEdit.value ? '修改成功' : '新增成功')
      dialogVisible.value = false
      await loadList()
      return
    }
    ElMessage.error(res.data?.message || '操作失败')
  } catch (error) {
    ElMessage.error('请求失败，请检查后端服务')
  }
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该商品吗？', '提示', { type: 'warning' })
    const res = await request.delete(`/product/delete/${id}`)
    if (res.data?.code === 200 && res.data?.data === true) {
      ElMessage.success('删除成功')
      await loadList()
      return
    }
    ElMessage.error(res.data?.message || '删除失败')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('请求失败，请检查后端服务')
    }
  }
}

onMounted(() => {
  loadList()
})
</script>
