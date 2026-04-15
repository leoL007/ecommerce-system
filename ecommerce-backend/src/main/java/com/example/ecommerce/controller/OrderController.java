package com.example.ecommerce.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.ecommerce.common.Result;
import com.example.ecommerce.entity.Orders;
import com.example.ecommerce.service.OrderService;
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
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/list")
    public Result<List<Orders>> list() {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Orders::getCreateTime);
        return Result.success(orderService.list(queryWrapper));
    }

    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody Orders order) {
        if (order == null || order.getOrderNo() == null || order.getOrderNo().trim().isEmpty()) {
            return Result.error("\u8ba2\u5355\u53f7\u4e0d\u80fd\u4e3a\u7a7a");
        }
        if (order.getCreateTime() == null) {
            order.setCreateTime(LocalDateTime.now());
        }
        return orderService.save(order) ? Result.success(true) : Result.error("\u65b0\u589e\u5931\u8d25");
    }

    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody Orders order) {
        if (order == null || order.getId() == null) {
            return Result.error("id\u4e0d\u80fd\u4e3a\u7a7a");
        }
        return orderService.updateById(order) ? Result.success(true) : Result.error("\u4fee\u6539\u5931\u8d25");
    }

    @DeleteMapping("/delete/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return orderService.removeById(id) ? Result.success(true) : Result.error("\u5220\u9664\u5931\u8d25");
    }
}
