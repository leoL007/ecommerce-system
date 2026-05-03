<template>
  <div class="page-container">
    <div class="page-head">
      <div>
        <h2 class="page-title">分类管理</h2>
        <p class="page-desc">可在此新增、编辑和删除商品分类信息。</p>
      </div>
      <el-button type="primary" class="toolbar-btn" @click="openAddDialog">新增分类</el-button>
    </div>

    <el-card class="panel-card table-wrap" shadow="never">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="100" />
        <el-table-column prop="name" label="分类名称" min-width="180" />
        <el-table-column prop="createTime" label="创建时间" min-width="220" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" class="action-btn" @click="openEditDialog(row)">编辑</el-button>
            <el-button link type="danger" class="action-btn" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>

  <el-dialog v-model="dialogVisible" :title="dialogTitle" width="420px">
    <el-form :model="form" label-width="80px">
      <el-form-item label="分类名称">
        <el-input v-model="form.name" placeholder="请输入分类名称" />
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
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../api/request'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)

const form = reactive({
  id: null,
  name: ''
})

const dialogTitle = computed(() => (isEdit.value ? '编辑分类' : '新增分类'))

const resetForm = () => {
  form.id = null
  form.name = ''
}

const loadList = async () => {
  loading.value = true
  try {
    const res = await request.get('/category/list')
    if (res.data?.code === 200) {
      tableData.value = res.data?.data || []
      return
    }
    ElMessage.error(res.data?.message || '获取分类列表失败')
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
  form.name = row.name
  dialogVisible.value = true
}

const submitForm = async () => {
  if (!form.name.trim()) {
    ElMessage.warning('请输入分类名称')
    return
  }

  try {
    const api = isEdit.value ? request.put : request.post
    const url = isEdit.value ? '/category/update' : '/category/add'
    const res = await api(url, { id: form.id, name: form.name })

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
    await ElMessageBox.confirm('确定删除该分类吗？', '提示', {
      type: 'warning'
    })
    const res = await request.delete(`/category/delete/${id}`)
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
