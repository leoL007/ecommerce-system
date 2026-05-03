<template>
  <div class="page-container">
    <div class="page-head">
      <div>
        <h2 class="page-title">我的资料</h2>
        <p class="page-desc">当前登录商家的基础资料信息。</p>
      </div>
    </div>

    <el-card class="panel-card" shadow="never">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="商家ID">{{ info.id || '-' }}</el-descriptions-item>
        <el-descriptions-item label="用户名">{{ info.username || '-' }}</el-descriptions-item>
        <el-descriptions-item label="商家名称">{{ info.name || '-' }}</el-descriptions-item>
        <el-descriptions-item label="电话">{{ info.phone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="地址">{{ info.address || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const info = computed(() => {
  const text = localStorage.getItem('merchantInfo')
  if (!text) return {}
  try {
    return JSON.parse(text)
  } catch (error) {
    return {}
  }
})
</script>
