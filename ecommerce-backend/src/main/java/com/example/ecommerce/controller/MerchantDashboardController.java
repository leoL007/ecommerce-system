package com.example.ecommerce.controller;

import com.example.ecommerce.common.Result;
import com.example.ecommerce.entity.Orders;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.service.MerchantDashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/merchant/dashboard")
public class MerchantDashboardController {

    private final MerchantDashboardService merchantDashboardService;

    public MerchantDashboardController(MerchantDashboardService merchantDashboardService) {
        this.merchantDashboardService = merchantDashboardService;
    }

    @GetMapping("/summary")
    public Result<Map<String, Object>> summary(@RequestParam Long merchantId) {
        if (merchantId == null) {
            return Result.error("merchantId\u4e0d\u80fd\u4e3a\u7a7a");
        }
        return Result.success(merchantDashboardService.getSummary(merchantId));
    }

    @GetMapping("/low-stock")
    public Result<List<Product>> lowStock(
            @RequestParam Long merchantId,
            @RequestParam(defaultValue = "5") Integer limit
    ) {
        if (merchantId == null) {
            return Result.error("merchantId\u4e0d\u80fd\u4e3a\u7a7a");
        }
        int safeLimit = (limit == null || limit <= 0) ? 5 : Math.min(limit, 20);
        return Result.success(merchantDashboardService.getLowStockProducts(merchantId, safeLimit));
    }

    @GetMapping("/recent-orders")
    public Result<List<Orders>> recentOrders(
            @RequestParam Long merchantId,
            @RequestParam(defaultValue = "5") Integer limit
    ) {
        if (merchantId == null) {
            return Result.error("merchantId\u4e0d\u80fd\u4e3a\u7a7a");
        }
        int safeLimit = (limit == null || limit <= 0) ? 5 : Math.min(limit, 20);
        return Result.success(merchantDashboardService.getRecentOrders(merchantId, safeLimit));
    }
}
