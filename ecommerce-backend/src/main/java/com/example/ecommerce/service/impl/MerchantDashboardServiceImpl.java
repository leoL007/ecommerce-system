package com.example.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.ecommerce.entity.Orders;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.service.MerchantDashboardService;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class MerchantDashboardServiceImpl implements MerchantDashboardService {

    private final ProductService productService;
    private final OrderService orderService;

    public MerchantDashboardServiceImpl(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    @Override
    public Map<String, Object> getSummary(Long merchantId) {
        Map<String, Object> summary = new LinkedHashMap<>();

        long productCount = productService.count(new LambdaQueryWrapper<Product>()
                .eq(Product::getMerchantId, merchantId));

        long activeProductCount = productService.count(new LambdaQueryWrapper<Product>()
                .eq(Product::getMerchantId, merchantId)
                .gt(Product::getStock, 0));

        long lowStockCount = productService.count(new LambdaQueryWrapper<Product>()
                .eq(Product::getMerchantId, merchantId)
                .lt(Product::getStock, 10));

        long orderCount = orderService.count(new LambdaQueryWrapper<Orders>()
                .eq(Orders::getMerchantId, merchantId));

        long pendingOrderCount = orderService.count(new LambdaQueryWrapper<Orders>()
                .eq(Orders::getMerchantId, merchantId)
                .in(Orders::getStatus, Arrays.asList("\u672a\u5904\u7406", "\u5f85\u5904\u7406", "\u5f85\u652f\u4ed8", "0")));

        long finishedOrderCount = orderService.count(new LambdaQueryWrapper<Orders>()
                .eq(Orders::getMerchantId, merchantId)
                .eq(Orders::getStatus, "\u5df2\u5b8c\u6210"));

        summary.put("productCount", productCount);
        summary.put("activeProductCount", activeProductCount);
        summary.put("lowStockCount", lowStockCount);
        summary.put("orderCount", orderCount);
        summary.put("pendingOrderCount", pendingOrderCount);
        summary.put("finishedOrderCount", finishedOrderCount);

        return summary;
    }

    @Override
    public List<Product> getLowStockProducts(Long merchantId, int limit) {
        return productService.list(new LambdaQueryWrapper<Product>()
                .eq(Product::getMerchantId, merchantId)
                .lt(Product::getStock, 10)
                .orderByAsc(Product::getStock)
                .last("limit " + limit));
    }

    @Override
    public List<Orders> getRecentOrders(Long merchantId, int limit) {
        return orderService.list(new LambdaQueryWrapper<Orders>()
                .eq(Orders::getMerchantId, merchantId)
                .orderByDesc(Orders::getCreateTime)
                .last("limit " + limit));
    }
}
