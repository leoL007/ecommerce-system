-- =========================================
-- demo-data.sql
-- 用途：电商后台毕设演示数据初始化（可重复执行）
-- 说明：
-- 1) 不会删除 admin 表数据
-- 2) 会清空 merchant/category/product/orders 并重建演示数据
-- 3) 自动检查并补充必要字段（product: merchant_id,image_url; orders: merchant_id,user_name）
-- =========================================

SET NAMES utf8mb4;

-- ---------- 字段补充（若缺失） ----------
SET @schema_name = DATABASE();

-- product.merchant_id
SET @sql_stmt = (
  SELECT IF(
    COUNT(*) = 0,
    'ALTER TABLE `product` ADD COLUMN `merchant_id` BIGINT NULL AFTER `description`',
    'SELECT 1'
  )
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = @schema_name
    AND TABLE_NAME = 'product'
    AND COLUMN_NAME = 'merchant_id'
);
PREPARE stmt FROM @sql_stmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- product.image_url
SET @sql_stmt = (
  SELECT IF(
    COUNT(*) = 0,
    'ALTER TABLE `product` ADD COLUMN `image_url` VARCHAR(255) NULL AFTER `merchant_id`',
    'SELECT 1'
  )
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = @schema_name
    AND TABLE_NAME = 'product'
    AND COLUMN_NAME = 'image_url'
);
PREPARE stmt FROM @sql_stmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- orders.merchant_id
SET @sql_stmt = (
  SELECT IF(
    COUNT(*) = 0,
    'ALTER TABLE `orders` ADD COLUMN `merchant_id` BIGINT NULL AFTER `status`',
    'SELECT 1'
  )
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = @schema_name
    AND TABLE_NAME = 'orders'
    AND COLUMN_NAME = 'merchant_id'
);
PREPARE stmt FROM @sql_stmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- orders.user_name
SET @sql_stmt = (
  SELECT IF(
    COUNT(*) = 0,
    'ALTER TABLE `orders` ADD COLUMN `user_name` VARCHAR(100) NULL AFTER `merchant_id`',
    'SELECT 1'
  )
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = @schema_name
    AND TABLE_NAME = 'orders'
    AND COLUMN_NAME = 'user_name'
);
PREPARE stmt FROM @sql_stmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ---------- 清空演示数据（保留 admin） ----------
SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM `orders`;
DELETE FROM `product`;
DELETE FROM `category`;
DELETE FROM `merchant`;

ALTER TABLE `orders` AUTO_INCREMENT = 1;
ALTER TABLE `product` AUTO_INCREMENT = 1;
ALTER TABLE `category` AUTO_INCREMENT = 1;
ALTER TABLE `merchant` AUTO_INCREMENT = 1;
SET FOREIGN_KEY_CHECKS = 1;

-- ---------- 商家（4 条） ----------
INSERT INTO `merchant` (`id`, `username`, `password`, `name`, `phone`, `address`, `create_time`) VALUES
(1, 'merchant_zhang', '123456', '张三数码店', '13800000001', '上海市浦东新区', NOW()),
(2, 'merchant_li',    '123456', '李四服饰店', '13800000002', '杭州市西湖区', NOW()),
(3, 'merchant_wang',  '123456', '王五食品店', '13800000003', '广州市天河区', NOW()),
(4, 'merchant_chen',  '123456', '陈六生活馆', '13800000004', '成都市高新区', NOW());

-- ---------- 分类（6 条） ----------
INSERT INTO `category` (`id`, `name`, `create_time`) VALUES
(1, '手机数码', NOW()),
(2, '服装鞋包', NOW()),
(3, '食品饮料', NOW()),
(4, '电脑办公', NOW()),
(5, '居家生活', NOW()),
(6, '运动户外', NOW());

-- ---------- 商品（18 条） ----------
INSERT INTO `product` (
  `id`, `name`, `price`, `stock`, `category_id`, `description`, `merchant_id`, `image_url`, `create_time`
) VALUES
(1,  '旗舰智能手机 X1',      3999.00, 35, 1, '6.7英寸 OLED 屏，12GB+256GB',                 1, 'https://picsum.photos/seed/p1/600/600', NOW()),
(2,  '真无线蓝牙耳机 Pro',    699.00, 8,  1, '主动降噪，续航 30 小时',                          1, 'https://picsum.photos/seed/p2/600/600', NOW()),
(3,  '轻薄笔记本 Air 14',     5299.00, 12, 4, '14英寸轻薄办公本，16GB内存',                      1, 'https://picsum.photos/seed/p3/600/600', NOW()),
(4,  '机械键盘 K87',          299.00, 18, 4, '87 键机械轴，白光背光',                            1, 'https://picsum.photos/seed/p4/600/600', NOW()),
(5,  '27英寸显示器 2K',       1299.00, 6,  4, '2K 分辨率，75Hz 刷新率',                          1, 'https://picsum.photos/seed/p5/600/600', NOW()),

(6,  '男士连帽卫衣',          199.00, 25, 2, '春秋款纯棉卫衣，舒适透气',                         2, 'https://picsum.photos/seed/p6/600/600', NOW()),
(7,  '女士休闲运动鞋',        259.00, 9,  2, '轻量缓震鞋底，日常百搭',                           2, 'https://picsum.photos/seed/p7/600/600', NOW()),
(8,  '商务双肩电脑包',        169.00, 22, 2, '15.6英寸电脑包，防泼水',                           2, 'https://picsum.photos/seed/p8/600/600', NOW()),
(9,  '夏季速干短袖T恤',       79.00,  40, 2, '吸湿速干，适合跑步健身',                           2, 'https://picsum.photos/seed/p9/600/600', NOW()),
(10, '保暖针织围巾',          59.00,  7,  2, '柔软亲肤，秋冬保暖',                               2, 'https://picsum.photos/seed/p10/600/600', NOW()),

(11, '纯牛奶 250ml*24盒',     89.00,  15, 3, '常温纯牛奶，家庭装',                               3, 'https://picsum.photos/seed/p11/600/600', NOW()),
(12, '每日坚果礼盒 750g',     128.00, 5,  3, '多种坚果混合，独立小包装',                         3, 'https://picsum.photos/seed/p12/600/600', NOW()),
(13, '燕麦片 1kg',            39.90,  28, 3, '高纤维早餐燕麦片',                                 3, 'https://picsum.photos/seed/p13/600/600', NOW()),
(14, '手工饼干组合装',        49.90,  11, 3, '黄油曲奇+蔓越莓饼干',                              3, 'https://picsum.photos/seed/p14/600/600', NOW()),

(15, '不锈钢保温杯 500ml',    99.00,  26, 5, '保温 8 小时，防漏设计',                            4, 'https://picsum.photos/seed/p15/600/600', NOW()),
(16, '家用抽纸 24包',         56.00,  45, 5, '三层加厚，居家常备',                               4, 'https://picsum.photos/seed/p16/600/600', NOW()),
(17, '瑜伽垫 10mm',           89.00,  6,  6, '防滑减震，适合家庭健身',                           4, 'https://picsum.photos/seed/p17/600/600', NOW()),
(18, '户外羽毛球拍套装',      149.00, 14, 6, '2 支球拍 + 6 只羽毛球',                            4, 'https://picsum.photos/seed/p18/600/600', NOW());

-- ---------- 订单（12 条） ----------
INSERT INTO `orders` (
  `id`, `order_no`, `product_name`, `quantity`, `total_price`, `status`, `merchant_id`, `user_name`, `create_time`
) VALUES
(1,  'ORD202605040001', '旗舰智能手机 X1',     1, 3999.00, '待处理', 1, '李同学', NOW()),
(2,  'ORD202605040002', '真无线蓝牙耳机 Pro',   2, 1398.00, '已完成', 1, '王先生', NOW()),
(3,  'ORD202605040003', '27英寸显示器 2K',      1, 1299.00, '待处理', 1, '赵女士', NOW()),

(4,  'ORD202605040004', '男士连帽卫衣',         2, 398.00,  '已完成', 2, '陈同学', NOW()),
(5,  'ORD202605040005', '女士休闲运动鞋',       1, 259.00,  '待处理', 2, '周先生', NOW()),
(6,  'ORD202605040006', '商务双肩电脑包',       1, 169.00,  '已完成', 2, '孙女士', NOW()),

(7,  'ORD202605040007', '纯牛奶 250ml*24盒',    3, 267.00,  '待处理', 3, '吴先生', NOW()),
(8,  'ORD202605040008', '每日坚果礼盒 750g',    1, 128.00,  '已完成', 3, '郑女士', NOW()),
(9,  'ORD202605040009', '手工饼干组合装',       2, 99.80,   '待处理', 3, '钱同学', NOW()),

(10, 'ORD202605040010', '不锈钢保温杯 500ml',   1, 99.00,   '已完成', 4, '冯先生', NOW()),
(11, 'ORD202605040011', '瑜伽垫 10mm',          2, 178.00,  '待处理', 4, '褚女士', NOW()),
(12, 'ORD202605040012', '户外羽毛球拍套装',     1, 149.00,  '已完成', 4, '卫同学', NOW());

-- 完成
SELECT 'demo-data.sql executed successfully' AS message;
