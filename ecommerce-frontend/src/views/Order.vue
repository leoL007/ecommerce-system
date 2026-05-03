<template>
  <div class="page-container">
    <div class="page-head">
      <div>
        <h2 class="page-title">订单管理</h2>
        <p class="page-desc">可在此新增订单、修改订单状态和删除订单。</p>
      </div>
      <el-button type="primary" class="toolbar-btn" @click="openAddDialog">新增订单</el-button>
    </div>

    <el-card class="panel-card table-wrap" shadow="never">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="orderNo" label="订单号" min-width="160" />
        <el-table-column prop="productName" label="商品名称" min-width="120" />
        <el-table-column prop="quantity" label="数量" width="90" />
        <el-table-column prop="totalPrice" label="总价" width="120" />
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="statusType(row.status)" effect="light">{{ statusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" class="action-btn" @click="openEditStatusDialog(row)">编辑状态</el-button>
            <el-button link type="danger" class="action-btn" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>

  <el-dialog v-model="addDialogVisible" title="新增订单" width="520px">
    <el-form :model="addForm" label-width="90px">
      <el-form-item label="订单号">
        <el-input v-model="addForm.orderNo" placeholder="请输入订单号" />
      </el-form-item>
      <el-form-item label="商品名称">
        <el-input v-model="addForm.productName" placeholder="请输入商品名称" />
      </el-form-item>
      <el-form-item label="数量">
        <el-input-number v-model="addForm.quantity" :min="1" :step="1" style="width: 100%" />
      </el-form-item>
      <el-form-item label="总价">
        <el-input-number v-model="addForm.totalPrice" :min="0" :precision="2" :step="1" style="width: 100%" />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="addForm.status" placeholder="请选择状态" style="width: 100%">
          <el-option :value="0" label="待支付" />
          <el-option :value="1" label="已支付" />
          <el-option :value="2" label="已发货" />
          <el-option :value="3" label="已完成" />
        </el-select>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="addDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitAdd">确定</el-button>
    </template>
  </el-dialog>

  <el-dialog v-model="editDialogVisible" title="编辑订单状态" width="420px">
    <el-form :model="editForm" label-width="90px">
      <el-form-item label="订单号">
        <el-input v-model="editForm.orderNo" disabled />
      </el-form-item>
      <el-form-item label="订单状态">
        <el-select v-model="editForm.status" placeholder="请选择状态" style="width: 100%">
          <el-option :value="0" label="待支付" />
          <el-option :value="1" label="已支付" />
          <el-option :value="2" label="已发货" />
          <el-option :value="3" label="已完成" />
        </el-select>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="editDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitEditStatus">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../api/request'

const loading = ref(false)
const tableData = ref([])

const addDialogVisible = ref(false)
const editDialogVisible = ref(false)

const addForm = reactive({
  orderNo: '',
  productName: '',
  quantity: 1,
  totalPrice: 0,
  status: 0
})

const editForm = reactive({
  id: null,
  orderNo: '',
  status: 0
})

const statusText = (status) => {
  if (status === 0) return '待支付'
  if (status === 1) return '已支付'
  if (status === 2) return '已发货'
  if (status === 3) return '已完成'
  return '未知'
}

const statusType = (status) => {
  if (status === 0) return 'warning'
  if (status === 1) return 'primary'
  if (status === 2) return 'success'
  if (status === 3) return 'info'
  return ''
}

const resetAddForm = () => {
  addForm.orderNo = ''
  addForm.productName = ''
  addForm.quantity = 1
  addForm.totalPrice = 0
  addForm.status = 0
}

const loadList = async () => {
  loading.value = true
  try {
    const res = await request.get('/order/list')
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

const openAddDialog = () => {
  resetAddForm()
  addDialogVisible.value = true
}

const submitAdd = async () => {
  if (!addForm.orderNo.trim()) {
    ElMessage.warning('请输入订单号')
    return
  }
  if (!addForm.productName.trim()) {
    ElMessage.warning('请输入商品名称')
    return
  }

  try {
    const res = await request.post('/order/add', {
      orderNo: addForm.orderNo,
      productName: addForm.productName,
      quantity: addForm.quantity,
      totalPrice: addForm.totalPrice,
      status: addForm.status
    })
    if (res.data?.code === 200 && res.data?.data === true) {
      ElMessage.success('新增成功')
      addDialogVisible.value = false
      await loadList()
      return
    }
    ElMessage.error(res.data?.message || '新增失败')
  } catch (error) {
    ElMessage.error('请求失败，请检查后端服务')
  }
}

const openEditStatusDialog = (row) => {
  editForm.id = row.id
  editForm.orderNo = row.orderNo
  editForm.status = row.status
  editDialogVisible.value = true
}

const submitEditStatus = async () => {
  try {
    const res = await request.put('/order/update', {
      id: editForm.id,
      status: editForm.status
    })
    if (res.data?.code === 200 && res.data?.data === true) {
      ElMessage.success('修改成功')
      editDialogVisible.value = false
      await loadList()
      return
    }
    ElMessage.error(res.data?.message || '修改失败')
  } catch (error) {
    ElMessage.error('请求失败，请检查后端服务')
  }
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该订单吗？', '提示', {
      type: 'warning'
    })
    const res = await request.delete(`/order/delete/${id}`)
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
