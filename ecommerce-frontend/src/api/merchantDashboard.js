import request from './request'

export const getMerchantDashboardSummary = (merchantId) =>
  request.get('/merchant/dashboard/summary', { params: { merchantId } })

export const getMerchantDashboardLowStock = (merchantId, limit = 5) =>
  request.get('/merchant/dashboard/low-stock', { params: { merchantId, limit } })

export const getMerchantDashboardRecentOrders = (merchantId, limit = 5) =>
  request.get('/merchant/dashboard/recent-orders', { params: { merchantId, limit } })
