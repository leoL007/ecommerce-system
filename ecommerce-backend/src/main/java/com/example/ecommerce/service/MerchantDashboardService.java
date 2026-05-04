package com.example.ecommerce.service;

import com.example.ecommerce.entity.Orders;
import com.example.ecommerce.entity.Product;

import java.util.List;
import java.util.Map;

public interface MerchantDashboardService {

    Map<String, Object> getSummary(Long merchantId);

    List<Product> getLowStockProducts(Long merchantId, int limit);

    List<Orders> getRecentOrders(Long merchantId, int limit);
}
