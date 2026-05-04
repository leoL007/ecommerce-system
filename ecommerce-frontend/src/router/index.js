import { createRouter, createWebHistory } from 'vue-router'
import AdminLayout from '../components/AdminLayout.vue'
import MerchantLayout from '../components/MerchantLayout.vue'
import AdminLogin from '../views/AdminLogin.vue'
import MerchantLogin from '../views/MerchantLogin.vue'
import Dashboard from '../views/Dashboard.vue'
import Category from '../views/Category.vue'
import Product from '../views/Product.vue'
import Order from '../views/Order.vue'
import Merchant from '../views/Merchant.vue'
import MerchantDashboard from '../views/MerchantDashboard.vue'
import MerchantProfile from '../views/MerchantProfile.vue'
import MerchantProduct from '../views/MerchantProduct.vue'
import MerchantOrder from '../views/MerchantOrder.vue'
import Shop from '../views/Shop.vue'

const routes = [
  {
    path: '/',
    redirect: '/admin/login'
  },
  {
    path: '/login',
    redirect: '/admin/login'
  },
  {
    path: '/admin/login',
    name: 'AdminLogin',
    component: AdminLogin
  },
  {
    path: '/merchant/login',
    name: 'MerchantLogin',
    component: MerchantLogin
  },
  {
    path: '/shop',
    name: 'Shop',
    component: Shop
  },
  {
    path: '/admin',
    component: AdminLayout,
    redirect: '/admin/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: Dashboard
      },
      {
        path: 'merchant',
        name: 'MerchantManage',
        component: Merchant
      },
      {
        path: 'category',
        name: 'AdminCategory',
        component: Category
      },
      {
        path: 'product',
        name: 'AdminProduct',
        component: Product
      },
      {
        path: 'order',
        name: 'AdminOrder',
        component: Order
      }
    ]
  },
  {
    path: '/merchant',
    component: MerchantLayout,
    redirect: '/merchant/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'MerchantDashboard',
        component: MerchantDashboard
      },
      {
        path: 'profile',
        name: 'MerchantProfile',
        component: MerchantProfile
      },
      {
        path: 'product',
        name: 'MerchantProduct',
        component: MerchantProduct
      },
      {
        path: 'order',
        name: 'MerchantOrder',
        component: MerchantOrder
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
