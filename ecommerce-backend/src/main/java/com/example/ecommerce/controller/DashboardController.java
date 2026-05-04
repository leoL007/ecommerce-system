package com.example.ecommerce.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.ecommerce.common.Result;
import com.example.ecommerce.entity.Orders;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.service.CategoryService;
import com.example.ecommerce.service.MerchantService;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final MerchantService merchantService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final OrderService orderService;

    public DashboardController(
            MerchantService merchantService,
            CategoryService categoryService,
            ProductService productService,
            OrderService orderService
    ) {
        this.merchantService = merchantService;
        this.categoryService = categoryService;
        this.productService = productService;
        this.orderService = orderService;
    }

    @GetMapping("/count")
    public Result<Map<String, Object>> count() {
        Map<String, Object> data = new LinkedHashMap<>();

        long merchantCount = merchantService.count();
        long categoryCount = categoryService.count();
        long productCount = productService.count();
        long orderCount = orderService.count();

        long lowStockCount = productService.count(new LambdaQueryWrapper<Product>()
                .lt(Product::getStock, 10));

        List<Product> products = productService.list();
        BigDecimal maxPrice = null;
        BigDecimal minPrice = null;
        BigDecimal avgPrice = BigDecimal.ZERO;

        BigDecimal totalPrice = BigDecimal.ZERO;
        long priceCount = 0;
        for (Product product : products) {
            if (product == null || product.getPrice() == null) {
                continue;
            }
            BigDecimal price = product.getPrice();
            if (maxPrice == null || price.compareTo(maxPrice) > 0) {
                maxPrice = price;
            }
            if (minPrice == null || price.compareTo(minPrice) < 0) {
                minPrice = price;
            }
            totalPrice = totalPrice.add(price);
            priceCount++;
        }

        if (priceCount > 0) {
            avgPrice = totalPrice.divide(BigDecimal.valueOf(priceCount), 2, RoundingMode.HALF_UP);
        }

        long pendingOrderCount = orderService.count(new LambdaQueryWrapper<Orders>()
                .in(Orders::getStatus, "未处理", "待处理"));

        long finishedOrderCount = orderService.count(new LambdaQueryWrapper<Orders>()
                .eq(Orders::getStatus, "已完成"));

        data.put("merchantCount", merchantCount);
        data.put("categoryCount", categoryCount);
        data.put("productCount", productCount);
        data.put("orderCount", orderCount);
        data.put("lowStockCount", lowStockCount);
        data.put("maxPrice", maxPrice == null ? BigDecimal.ZERO : maxPrice);
        data.put("minPrice", minPrice == null ? BigDecimal.ZERO : minPrice);
        data.put("avgPrice", avgPrice);
        data.put("pendingOrderCount", pendingOrderCount);
        data.put("finishedOrderCount", finishedOrderCount);

        return Result.success(data);
    }
}
