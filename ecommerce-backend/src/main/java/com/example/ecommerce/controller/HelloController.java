package com.example.ecommerce.controller;

import com.example.ecommerce.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class HelloController {

    @GetMapping("/hello")
    public Result<String> hello() {
        return Result.success("hello backend");
    }
}
