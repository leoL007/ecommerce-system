<template>
  <el-container class="layout-container">
    <el-aside width="230px" class="layout-aside">
      <div class="brand-block">
        <div class="brand-title">商家后台管理</div>
        <div class="brand-sub">Ecommerce Console</div>
      </div>

      <el-menu :default-active="activePath" router class="menu">
        <el-menu-item index="/dashboard">首页</el-menu-item>
        <el-menu-item index="/category">分类管理</el-menu-item>
        <el-menu-item index="/product">商品管理</el-menu-item>
        <el-menu-item index="/order">订单管理</el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="layout-header">
        <div>
          <div class="header-title">商家后台管理系统</div>
          <div class="header-sub">用于分类、商品与订单的日常管理</div>
        </div>
        <el-tag type="primary" effect="light" round>商家后台管理人员</el-tag>
      </el-header>

      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const activePath = computed(() => route.path)
</script>

<style scoped>
.layout-container {
  min-height: 100vh;
  background: #f3f6fb;
}

.layout-aside {
  border-right: none;
  background: linear-gradient(180deg, #1f3b8f 0%, #1b2f6f 100%);
  box-shadow: inset -1px 0 0 rgba(255, 255, 255, 0.06);
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
  box-shadow: 0 2px 12px rgba(15, 23, 42, 0.04);
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
</style>
