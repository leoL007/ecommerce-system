package com.example.ecommerce.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.ecommerce.common.Result;
import com.example.ecommerce.entity.Merchant;
import com.example.ecommerce.service.MerchantService;
import lombok.Data;
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
@RequestMapping("/merchant")
public class MerchantController {

    private final MerchantService merchantService;

    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @GetMapping("/list")
    public Result<List<Merchant>> list() {
        LambdaQueryWrapper<Merchant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Merchant::getCreateTime);
        return Result.success(merchantService.list(queryWrapper));
    }

    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody Merchant merchant) {
        if (merchant == null || isBlank(merchant.getUsername()) || isBlank(merchant.getPassword())) {
            return Result.error("\u7528\u6237\u540d\u548c\u5bc6\u7801\u4e0d\u80fd\u4e3a\u7a7a");
        }
        if (merchant.getCreateTime() == null) {
            merchant.setCreateTime(LocalDateTime.now());
        }
        return merchantService.save(merchant) ? Result.success(true) : Result.error("\u65b0\u589e\u5931\u8d25");
    }

    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody Merchant merchant) {
        if (merchant == null || merchant.getId() == null) {
            return Result.error("id\u4e0d\u80fd\u4e3a\u7a7a");
        }
        return merchantService.updateById(merchant) ? Result.success(true) : Result.error("\u4fee\u6539\u5931\u8d25");
    }

    @DeleteMapping("/delete/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return merchantService.removeById(id) ? Result.success(true) : Result.error("\u5220\u9664\u5931\u8d25");
    }

    @PostMapping("/login")
    public Result<Merchant> login(@RequestBody LoginRequest request) {
        if (request == null || isBlank(request.getUsername()) || isBlank(request.getPassword())) {
            return Result.error("\u7528\u6237\u540d\u6216\u5bc6\u7801\u9519\u8bef");
        }

        LambdaQueryWrapper<Merchant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Merchant::getUsername, request.getUsername())
                .eq(Merchant::getPassword, request.getPassword())
                .last("limit 1");

        Merchant merchant = merchantService.getOne(queryWrapper, false);
        if (merchant == null) {
            return Result.error("\u7528\u6237\u540d\u6216\u5bc6\u7801\u9519\u8bef");
        }

        merchant.setPassword(null);
        return Result.success(merchant);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }
}
