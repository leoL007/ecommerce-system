package com.example.ecommerce.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.ecommerce.common.Result;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.service.CategoryService;
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
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public Result<List<Category>> list() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Category::getCreateTime);
        return Result.success(categoryService.list(queryWrapper));
    }

    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody Category category) {
        if (category == null || category.getName() == null || category.getName().trim().isEmpty()) {
            return Result.error("\u5206\u7c7b\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a");
        }
        if (category.getCreateTime() == null) {
            category.setCreateTime(LocalDateTime.now());
        }
        return categoryService.save(category) ? Result.success(true) : Result.error("\u65b0\u589e\u5931\u8d25");
    }

    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody Category category) {
        if (category == null || category.getId() == null) {
            return Result.error("id\u4e0d\u80fd\u4e3a\u7a7a");
        }
        return categoryService.updateById(category) ? Result.success(true) : Result.error("\u4fee\u6539\u5931\u8d25");
    }

    @DeleteMapping("/delete/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return categoryService.removeById(id) ? Result.success(true) : Result.error("\u5220\u9664\u5931\u8d25");
    }
}
