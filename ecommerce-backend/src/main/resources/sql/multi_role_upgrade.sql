-- 1) merchant table
CREATE TABLE IF NOT EXISTS `merchant` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(64) NOT NULL,
  `password` VARCHAR(128) NOT NULL,
  `name` VARCHAR(100) DEFAULT NULL,
  `phone` VARCHAR(32) DEFAULT NULL,
  `address` VARCHAR(255) DEFAULT NULL,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_merchant_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2) product table add merchant and image
ALTER TABLE `product`
  ADD COLUMN `merchant_id` BIGINT NULL AFTER `description`,
  ADD COLUMN `image_url` VARCHAR(255) NULL AFTER `merchant_id`;

CREATE INDEX `idx_product_merchant_id` ON `product` (`merchant_id`);

-- 3) orders table add merchant and user name
ALTER TABLE `orders`
  ADD COLUMN `merchant_id` BIGINT NULL AFTER `status`,
  ADD COLUMN `user_name` VARCHAR(100) NULL AFTER `merchant_id`;

CREATE INDEX `idx_orders_merchant_id` ON `orders` (`merchant_id`);
