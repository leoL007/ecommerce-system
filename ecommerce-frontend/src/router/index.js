import { createRouter, createWebHistory } from 'vue-router'
import Layout from '../components/Layout.vue'
import Login from '../views/Login.vue'
import Dashboard from '../views/Dashboard.vue'
import Category from '../views/Category.vue'
import Product from '../views/Product.vue'
import Order from '../views/Order.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: Dashboard
      },
      {
        path: 'category',
        name: 'Category',
        component: Category
      },
      {
        path: 'product',
        name: 'Product',
        component: Product
      },
      {
        path: 'order',
        name: 'Order',
        component: Order
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
