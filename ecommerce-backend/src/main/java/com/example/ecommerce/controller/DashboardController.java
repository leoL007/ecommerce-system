package com.example.ecommerce.controller;

import com.example.ecommerce.common.Result;
import com.example.ecommerce.service.CategoryService;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final OrderService orderService;

    public DashboardController(CategoryService categoryService, ProductService productService, OrderService orderService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.orderService = orderService;
    }

    @GetMapping("/count")
    public Result<Map<String, Long>> count() {
        Map<String, Long> data = new LinkedHashMap<>();
        data.put("categoryCount", categoryService.count());
        data.put("productCount", productService.count());
        data.put("orderCount", orderService.count());
        return Result.success(data);
    }
}
