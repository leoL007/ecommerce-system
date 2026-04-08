package com.example.ecommerce.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.example.ecommerce.mapper")
public class MybatisPlusConfig {
}
