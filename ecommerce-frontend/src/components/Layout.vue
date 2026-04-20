<template>
  <el-container class="layout-container">
    <el-aside width="220px" class="layout-aside">
      <div class="logo">Ecommerce Admin</div>
      <el-menu :default-active="activePath" router class="menu">
        <el-menu-item index="/dashboard">首页</el-menu-item>
        <el-menu-item index="/category">分类管理</el-menu-item>
        <el-menu-item index="/product">商品管理</el-menu-item>
        <el-menu-item index="/order">订单管理</el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="layout-header">
        <span>电商后台管理系统</span>
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
  background: #f5f7fa;
}

.layout-aside {
  background: #ffffff;
  border-right: 1px solid #e6e8eb;
}

.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  font-size: 18px;
  font-weight: 600;
  border-bottom: 1px solid #e6e8eb;
}

.menu {
  border-right: none;
}

.layout-header {
  display: flex;
  align-items: center;
  height: 60px;
  background: #ffffff;
  border-bottom: 1px solid #e6e8eb;
  font-size: 16px;
  font-weight: 500;
}

.layout-main {
  padding: 16px;
}
</style>
