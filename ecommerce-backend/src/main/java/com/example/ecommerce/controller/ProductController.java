package com.example.ecommerce.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.ecommerce.common.Result;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.service.ProductService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/list")
    public Result<List<Product>> list() {
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Product::getCreateTime);
        return Result.success(productService.list(queryWrapper));
    }

    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody Product product) {
        if (product == null || product.getName() == null || product.getName().trim().isEmpty()) {
            return Result.error("\u5546\u54c1\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a");
        }
        if (product.getCreateTime() == null) {
            product.setCreateTime(LocalDateTime.now());
        }
        return productService.save(product) ? Result.success(true) : Result.error("\u65b0\u589e\u5931\u8d25");
    }

    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody Product product) {
        if (product == null || product.getId() == null) {
            return Result.error("id\u4e0d\u80fd\u4e3a\u7a7a");
        }
        return productService.updateById(product) ? Result.success(true) : Result.error("\u4fee\u6539\u5931\u8d25");
    }

    @DeleteMapping("/delete/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return productService.removeById(id) ? Result.success(true) : Result.error("\u5220\u9664\u5931\u8d25");
    }
}
