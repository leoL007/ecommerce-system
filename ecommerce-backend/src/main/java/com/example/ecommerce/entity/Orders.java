package com.example.ecommerce.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Orders {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("order_no")
    private String orderNo;

    @TableField("product_name")
    private String productName;

    @TableField("quantity")
    private Integer quantity;

    @TableField("total_price")
    private BigDecimal totalPrice;

    @TableField("status")
    private Integer status;

    @TableField("create_time")
    private LocalDateTime createTime;
}
