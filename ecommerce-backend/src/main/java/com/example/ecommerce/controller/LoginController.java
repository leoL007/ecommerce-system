package com.example.ecommerce.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.ecommerce.common.Result;
import com.example.ecommerce.entity.Admin;
import com.example.ecommerce.mapper.AdminMapper;
import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class LoginController {

    private final AdminMapper adminMapper;

    public LoginController(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    @PostMapping("/login")
    public Result<Boolean> login(@RequestBody LoginRequest request) {
        if (request == null || request.getUsername() == null || request.getPassword() == null) {
            return Result.error("\u7528\u6237\u540d\u6216\u5bc6\u7801\u9519\u8bef");
        }

        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getUsername, request.getUsername())
                .eq(Admin::getPassword, request.getPassword());

        Long count = adminMapper.selectCount(queryWrapper);
        if (count != null && count > 0) {
            return Result.success(true);
        }
        return Result.error("\u7528\u6237\u540d\u6216\u5bc6\u7801\u9519\u8bef");
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }
}
