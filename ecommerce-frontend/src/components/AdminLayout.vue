<template>
  <el-container class="layout-container">
    <el-aside width="230px" class="layout-aside admin-aside">
      <div class="brand-block">
        <div class="brand-title">平台管理员后台</div>
        <div class="brand-sub">Platform Console</div>
      </div>

      <el-menu :default-active="activePath" router class="menu">
        <el-menu-item index="/admin/dashboard">首页统计</el-menu-item>
        <el-menu-item index="/admin/merchant">商家管理</el-menu-item>
        <el-menu-item index="/admin/category">分类管理</el-menu-item>
        <el-menu-item index="/admin/product">商品列表</el-menu-item>
        <el-menu-item index="/admin/order">订单管理</el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="layout-header">
        <div>
          <div class="header-title">平台管理员后台</div>
          <div class="header-sub">管理商家账号与全站业务数据</div>
        </div>
        <div class="header-actions">
          <el-button type="primary" plain @click="goShop">查看前台商品页</el-button>
          <el-button type="danger" plain @click="logout">退出登录</el-button>
        </div>
      </el-header>

      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const activePath = computed(() => route.path)

const logout = () => {
  router.push('/admin/login')
}

const goShop = () => {
  window.open('/shop', '_blank')
}
</script>

<style scoped>
.layout-container {
  min-height: 100vh;
  background: #f3f6fb;
}

.layout-aside {
  border-right: none;
  box-shadow: inset -1px 0 0 rgba(255, 255, 255, 0.08);
}

.admin-aside {
  background: linear-gradient(180deg, #1f3b8f 0%, #1b2f6f 100%);
}

.brand-block {
  padding: 20px 18px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.12);
}

.brand-title {
  font-size: 20px;
  font-weight: 700;
  color: #ffffff;
}

.brand-sub {
  margin-top: 6px;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.75);
}

.menu {
  margin-top: 8px;
  border-right: none;
  background: transparent;
}

:deep(.el-menu-item) {
  height: 46px;
  margin: 8px 12px;
  border-radius: 8px;
  color: rgba(255, 255, 255, 0.88);
}

:deep(.el-menu-item:hover) {
  background: rgba(255, 255, 255, 0.14);
  color: #ffffff;
}

:deep(.el-menu-item.is-active) {
  background: #ffffff;
  color: #1f3b8f;
  font-weight: 600;
}

.layout-header {
  height: 70px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #ffffff;
  border-bottom: 1px solid #e5e7eb;
}

.header-title {
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
}

.header-sub {
  margin-top: 4px;
  font-size: 12px;
  color: #6b7280;
}

.layout-main {
  padding: 18px;
}

.header-actions {
  display: flex;
  gap: 10px;
  align-items: center;
}
</style>
