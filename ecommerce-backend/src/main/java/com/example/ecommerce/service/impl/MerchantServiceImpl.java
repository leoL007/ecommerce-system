package com.example.ecommerce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ecommerce.entity.Merchant;
import com.example.ecommerce.mapper.MerchantMapper;
import com.example.ecommerce.service.MerchantService;
import org.springframework.stereotype.Service;

@Service
public class MerchantServiceImpl extends ServiceImpl<MerchantMapper, Merchant> implements MerchantService {
}
