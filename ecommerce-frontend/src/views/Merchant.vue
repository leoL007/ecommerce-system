<template>
  <div class="page-container">
    <div class="page-head">
      <div>
        <h2 class="page-title">商家管理</h2>
        <p class="page-desc">平台管理员可在此维护商家账号。</p>
      </div>
      <el-button type="primary" class="toolbar-btn" @click="openAddDialog">新增商家</el-button>
    </div>

    <el-card class="panel-card table-wrap" shadow="never">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" min-width="120" />
        <el-table-column prop="name" label="商家名称" min-width="140" />
        <el-table-column prop="phone" label="电话" min-width="130" />
        <el-table-column prop="address" label="地址" min-width="180" show-overflow-tooltip />
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

  <el-dialog v-model="dialogVisible" :title="dialogTitle" width="520px">
    <el-form :model="form" label-width="90px">
      <el-form-item label="用户名">
        <el-input v-model="form.username" placeholder="请输入用户名" />
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="form.password" placeholder="请输入密码" show-password />
      </el-form-item>
      <el-form-item label="商家名称">
        <el-input v-model="form.name" placeholder="请输入商家名称" />
      </el-form-item>
      <el-form-item label="电话">
        <el-input v-model="form.phone" placeholder="请输入电话" />
      </el-form-item>
      <el-form-item label="地址">
        <el-input v-model="form.address" placeholder="请输入地址" />
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
  username: '',
  password: '',
  name: '',
  phone: '',
  address: ''
})

const dialogTitle = computed(() => (isEdit.value ? '编辑商家' : '新增商家'))

const resetForm = () => {
  form.id = null
  form.username = ''
  form.password = ''
  form.name = ''
  form.phone = ''
  form.address = ''
}

const loadList = async () => {
  loading.value = true
  try {
    const res = await request.get('/merchant/list')
    if (res.data?.code === 200) {
      tableData.value = res.data?.data || []
      return
    }
    ElMessage.error(res.data?.message || '获取商家列表失败')
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
  form.username = row.username || ''
  form.password = ''
  form.name = row.name || ''
  form.phone = row.phone || ''
  form.address = row.address || ''
  dialogVisible.value = true
}

const submitForm = async () => {
  if (!form.username.trim()) {
    ElMessage.warning('请输入用户名')
    return
  }
  if (!isEdit.value && !form.password.trim()) {
    ElMessage.warning('请输入密码')
    return
  }

  const payload = {
    id: form.id,
    username: form.username,
    name: form.name,
    phone: form.phone,
    address: form.address
  }
  if (form.password.trim()) {
    payload.password = form.password
  }

  try {
    const api = isEdit.value ? request.put : request.post
    const url = isEdit.value ? '/merchant/update' : '/merchant/add'
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
    await ElMessageBox.confirm('确定删除该商家吗？', '提示', { type: 'warning' })
    const res = await request.delete(`/merchant/delete/${id}`)
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
