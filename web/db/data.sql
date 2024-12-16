-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.37 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.7.0.6850
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for colo-shop
CREATE DATABASE IF NOT EXISTS `colo-shop` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `colo-shop`;

-- Dumping structure for table colo-shop.account_type
CREATE TABLE IF NOT EXISTS `account_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` varchar(45) NOT NULL COMMENT 'admin/buyer/seller',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table colo-shop.account_type: ~2 rows (approximately)
INSERT INTO `account_type` (`id`, `type`) VALUES
	(1, 'Admin'),
	(2, 'Buyer'),
	(3, 'Seller');

-- Dumping structure for table colo-shop.admin
CREATE TABLE IF NOT EXISTS `admin` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `email` varchar(65) NOT NULL,
  `password` varchar(45) NOT NULL,
  `token` varchar(45) DEFAULT NULL,
  `status_id` int NOT NULL,
  `verified_status_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_admin_status1_idx` (`status_id`),
  KEY `fk_admin_verified_status1_idx` (`verified_status_id`),
  CONSTRAINT `fk_admin_status1` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`),
  CONSTRAINT `fk_admin_verified_status1` FOREIGN KEY (`verified_status_id`) REFERENCES `verified_status` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table colo-shop.admin: ~0 rows (approximately)
INSERT INTO `admin` (`id`, `first_name`, `last_name`, `email`, `password`, `token`, `status_id`, `verified_status_id`) VALUES
	(1, 'Pasindu', 'Bhathiya', 'p@gmail.com', 'Pasindu328@', NULL, 1, 1);

-- Dumping structure for table colo-shop.brand
CREATE TABLE IF NOT EXISTS `brand` (
  `id` int NOT NULL AUTO_INCREMENT,
  `brand` varchar(45) NOT NULL,
  `status_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_brand_status1_idx` (`status_id`),
  CONSTRAINT `fk_brand_status1` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table colo-shop.brand: ~15 rows (approximately)
INSERT INTO `brand` (`id`, `brand`, `status_id`) VALUES
	(1, 'Veja', 1),
	(2, 'Outerknown', 1),
	(3, 'Allbirds', 1),
	(4, 'Patagonia', 1),
	(5, 'Everlane', 1),
	(6, 'True Religion', 1),
	(7, 'Lee', 1),
	(8, 'GAP', 1),
	(9, 'Diesel', 1),
	(10, 'Wrangler', 1),
	(11, 'The North Face', 1),
	(12, 'Champion', 1),
	(13, 'Vans', 1),
	(14, 'Patagonia', 1),
	(15, 'Puma', 1);

-- Dumping structure for table colo-shop.cart
CREATE TABLE IF NOT EXISTS `cart` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `product_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_cart_user1_idx` (`user_id`),
  KEY `fk_cart_product1_idx` (`product_id`),
  CONSTRAINT `fk_cart_product1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `fk_cart_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table colo-shop.cart: ~5 rows (approximately)
INSERT INTO `cart` (`id`, `user_id`, `product_id`) VALUES
	(1, 2, 8),
	(2, 2, 13),
	(3, 2, 6),
	(4, 2, 15),
	(5, 2, 8);

-- Dumping structure for table colo-shop.category
CREATE TABLE IF NOT EXISTS `category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `category` varchar(25) NOT NULL,
  `status_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_category_status1_idx` (`status_id`),
  CONSTRAINT `fk_category_status1` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table colo-shop.category: ~4 rows (approximately)
INSERT INTO `category` (`id`, `category`, `status_id`) VALUES
	(1, 'Women', 1),
	(2, 'Men', 1),
	(3, 'Child', 1),
	(4, 'Baby', 1);

-- Dumping structure for table colo-shop.message
CREATE TABLE IF NOT EXISTS `message` (
  `id` int NOT NULL AUTO_INCREMENT,
  `message` text NOT NULL,
  `title` varchar(45) NOT NULL,
  `datetime` datetime NOT NULL,
  `message_status_id` int NOT NULL,
  `user_id` int NOT NULL,
  `admin_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_message_message_status1_idx` (`message_status_id`),
  KEY `fk_message_user1_idx` (`user_id`),
  KEY `fk_message_admin1_idx` (`admin_id`),
  CONSTRAINT `fk_message_admin1` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`id`),
  CONSTRAINT `fk_message_message_status1` FOREIGN KEY (`message_status_id`) REFERENCES `message_status` (`id`),
  CONSTRAINT `fk_message_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table colo-shop.message: ~0 rows (approximately)

-- Dumping structure for table colo-shop.message_status
CREATE TABLE IF NOT EXISTS `message_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `status` varchar(15) NOT NULL COMMENT 'read/received/replied',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table colo-shop.message_status: ~2 rows (approximately)
INSERT INTO `message_status` (`id`, `status`) VALUES
	(1, 'Received'),
	(2, 'Read'),
	(3, 'Replied');

-- Dumping structure for table colo-shop.message_to_seller
CREATE TABLE IF NOT EXISTS `message_to_seller` (
  `id` int NOT NULL AUTO_INCREMENT,
  `message` text NOT NULL,
  `seller_id` int NOT NULL,
  `user_id` int NOT NULL,
  `datetime` datetime NOT NULL,
  `message_status_id` int NOT NULL,
  `product_id` int NOT NULL,
  `reply` text,
  PRIMARY KEY (`id`),
  KEY `fk_seller_has_user_user1_idx` (`user_id`),
  KEY `fk_seller_has_user_seller1_idx` (`seller_id`),
  KEY `fk_message_to_seller_message_status1_idx` (`message_status_id`),
  KEY `fk_message_to_seller_product1_idx` (`product_id`),
  CONSTRAINT `fk_message_to_seller_message_status1` FOREIGN KEY (`message_status_id`) REFERENCES `message_status` (`id`),
  CONSTRAINT `fk_message_to_seller_product1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `fk_seller_has_user_seller1` FOREIGN KEY (`seller_id`) REFERENCES `seller` (`id`),
  CONSTRAINT `fk_seller_has_user_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table colo-shop.message_to_seller: ~0 rows (approximately)

-- Dumping structure for table colo-shop.newsletter
CREATE TABLE IF NOT EXISTS `newsletter` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(60) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table colo-shop.newsletter: ~0 rows (approximately)

-- Dumping structure for table colo-shop.orders
CREATE TABLE IF NOT EXISTS `orders` (
  `id` int NOT NULL,
  `datetime` datetime NOT NULL,
  `total_amount` double NOT NULL,
  `delivery_date` date NOT NULL,
  `address` text NOT NULL,
  `text` varchar(250) DEFAULT NULL,
  `user_id` int NOT NULL,
  `order_status_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_order_user1_idx` (`user_id`),
  KEY `fk_order_order_status1_idx` (`order_status_id`),
  CONSTRAINT `fk_order_order_status1` FOREIGN KEY (`order_status_id`) REFERENCES `order_status` (`id`),
  CONSTRAINT `fk_order_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table colo-shop.orders: ~3 rows (approximately)
INSERT INTO `orders` (`id`, `datetime`, `total_amount`, `delivery_date`, `address`, `text`, `user_id`, `order_status_id`) VALUES
	(-831328152, '2024-12-16 13:21:00', 0, '2024-12-16', '70/1/3,george r De Silva Mawath, Police quarters ,colombo 13', '', 2, 4),
	(-808691008, '2024-12-16 19:38:17', 0, '2024-12-16', '70/1/3,george r De Silva Mawath, Police quarters ,colombo 13', '', 2, 4),
	(-808675336, '2024-12-16 19:38:32', 0, '2024-12-16', '70/1/3,george r De Silva Mawath, Police quarters ,colombo 13', '', 2, 4);

-- Dumping structure for table colo-shop.order_item
CREATE TABLE IF NOT EXISTS `order_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `qty` int DEFAULT NULL,
  `product_id` int NOT NULL,
  `orders_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_order_item_product1_idx` (`product_id`),
  KEY `orders_id` (`orders_id`),
  CONSTRAINT `FK_order_item_orders` FOREIGN KEY (`orders_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `fk_order_item_product1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table colo-shop.order_item: ~4 rows (approximately)
INSERT INTO `order_item` (`id`, `qty`, `product_id`, `orders_id`) VALUES
	(1, 3, 8, -831328152),
	(2, 1, 8, -808691008),
	(3, 1, 8, -808675336),
	(4, 1, 15, -808675336);

-- Dumping structure for table colo-shop.order_status
CREATE TABLE IF NOT EXISTS `order_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `status` varchar(15) NOT NULL COMMENT 'pending/shipped/delivered',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table colo-shop.order_status: ~2 rows (approximately)
INSERT INTO `order_status` (`id`, `status`) VALUES
	(1, 'Paid'),
	(2, 'Shipped'),
	(3, 'Delivered'),
	(4, 'Pending');

-- Dumping structure for table colo-shop.product
CREATE TABLE IF NOT EXISTS `product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(75) NOT NULL,
  `description` text NOT NULL,
  `unit_price` double NOT NULL,
  `added` datetime NOT NULL,
  `img1` text NOT NULL,
  `img2` text NOT NULL,
  `img3` text NOT NULL,
  `delivery_fee` double NOT NULL,
  `sold_count` int NOT NULL DEFAULT '0' COMMENT 'how many items from this product has already been sold ',
  `status_id` int NOT NULL,
  `product_color_id` int NOT NULL,
  `size_id` int NOT NULL,
  `brand_id` int NOT NULL,
  `seller_id` int NOT NULL,
  `category_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_product_status1_idx` (`status_id`),
  KEY `fk_product_product_color1_idx` (`product_color_id`),
  KEY `fk_product_size1_idx` (`size_id`),
  KEY `fk_product_brand1_idx` (`brand_id`),
  KEY `fk_product_seller1_idx` (`seller_id`),
  KEY `fk_product_category1_idx` (`category_id`),
  CONSTRAINT `fk_product_brand1` FOREIGN KEY (`brand_id`) REFERENCES `brand` (`id`),
  CONSTRAINT `fk_product_category1` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
  CONSTRAINT `fk_product_product_color1` FOREIGN KEY (`product_color_id`) REFERENCES `product_color` (`id`),
  CONSTRAINT `fk_product_seller1` FOREIGN KEY (`seller_id`) REFERENCES `seller` (`id`),
  CONSTRAINT `fk_product_size1` FOREIGN KEY (`size_id`) REFERENCES `size` (`id`),
  CONSTRAINT `fk_product_status1` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table colo-shop.product: ~20 rows (approximately)
INSERT INTO `product` (`id`, `name`, `description`, `unit_price`, `added`, `img1`, `img2`, `img3`, `delivery_fee`, `sold_count`, `status_id`, `product_color_id`, `size_id`, `brand_id`, `seller_id`, `category_id`) VALUES
	(1, 'GAP Baby Organic Cotton Onesie', 'This ultra-soft onesie is made from 100% organic cotton, ensuring maximum comfort and safety for your babyâs delicate skin. Features a snap closure for easy changes.', 3000, '2024-12-14 22:15:39', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734194739055\\image1.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734194739055\\image2.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734194739055\\image3.png', 0, 0, 1, 2, 3, 8, 1, 4),
	(2, 'Champion Kids\' Hoodie', 'Keep the little ones warm and comfortable with this fleece-lined hoodie, featuring Championâs iconic logo and ribbed cuffs for a snug fit.', 4250, '2024-12-14 23:01:46', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734197505678\\image1.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734197505678\\image2.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734197505678\\image3.png', 300, 0, 1, 1, 3, 12, 1, 3),
	(3, 'Everlane Women\'s Wide-Leg Pants', 'Crafted from organic cotton, these wide-leg pants are versatile and stylish. Pair them with sneakers or boots for a polished yet comfortable look.', 5000, '2024-12-14 23:14:25', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734198265313\\image1.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734198265313\\image2.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734198265313\\image3.png', 0, 0, 1, 2, 4, 5, 1, 1),
	(4, 'Men\'s Down Sweater Jacket', 'Lightweight yet warm, this jacket is made with 100% recycled polyester and features 800-fill-power goose down insulation, perfect for winter adventures or casual outings.', 8000, '2024-12-14 23:16:58', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734198417816\\image1.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734198417816\\image2.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734198417816\\image3.png', 500, 0, 1, 3, 4, 4, 1, 2),
	(5, 'Straight Fit Jeans', 'Durable and versatile, these jeans offer a timeless straight fit, perfect for casual and semi-formal wear.', 4850, '2024-12-14 23:49:44', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734200384240\\image1.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734200384240\\image2.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734200384240\\image3.png', 800, 0, 1, 8, 3, 10, 1, 2),
	(6, 'Graphic Tee', ' A soft cotton t-shirt featuring bold graphic prints and the Diesel logo for a chic and edgy look.', 1550, '2024-12-14 23:53:36', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734200615747\\image1.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734200615747\\image2.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734200615747\\image3.png', 550, 0, 1, 8, 3, 9, 1, 1),
	(7, 'Rain Jacket', 'Keep kids dry and happy in this waterproof, breathable rain jacket designed for outdoor adventures.', 3500, '2024-12-14 23:57:08', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734200828237\\image1.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734200828237\\image2.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734200828237\\image3.png', 1000, 0, 1, 10, 2, 11, 1, 3),
	(8, 'Sweatpants', 'Ultra-comfy fleece sweatpants with an adjustable drawstring waist and iconic Champion logo.', 1000, '2024-12-14 23:59:35', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734200975236\\image1.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734200975236\\image2.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734200975236\\image3.png', 500, 0, 1, 8, 3, 12, 1, 2),
	(9, 'Sports Bra', 'This medium-support sports bra features breathable fabric and moisture-wicking technology for ultimate comfort during workouts.', 800, '2024-12-15 00:02:02', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734201122034\\image1.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734201122034\\image2.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734201122034\\image3.png', 0, 0, 1, 2, 2, 15, 1, 1),
	(10, ' Graphic Hoodie', 'Soft and stylish, this hoodie features playful graphics and GAP branding, perfect for everyday wear.', 4000, '2024-12-15 00:05:04', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734201303755\\image1.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734201303755\\image2.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734201303755\\image3.png', 500, 0, 1, 1, 4, 8, 1, 3),
	(11, ' Baby Organic Cotton Blanket', 'Wrap your baby in comfort with this super-soft organic cotton blanket, ideal for naps and cuddles.', 2500, '2024-12-15 00:10:31', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734201631112\\image1.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734201631112\\image2.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734201631112\\image3.png', 0, 0, 1, 9, 2, 2, 1, 4),
	(12, 'Denim Jacket', 'This classic denim jacket features a slim fit, vintage wash, and iconic True Religion stitching.', 8000, '2024-12-15 00:16:48', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734202007640\\image1.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734202007640\\image2.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734202007640\\image3.png', 0, 0, 1, 9, 3, 6, 1, 1),
	(13, ' Surf Tee', 'A soft, lightweight surf tee made from 100% organic cotton, ideal for sunny beach days.', 5000, '2024-12-15 00:20:53', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734202252950\\image1.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734202252950\\image2.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734202252950\\image3.png', 0, 0, 1, 2, 2, 2, 1, 2),
	(14, ' Fleece Pullover', 'Cozy and warm, this fleece pullover is perfect for layering during chilly outdoor adventures.', 10000, '2024-12-15 00:23:12', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734202391459\\image1.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734202391459\\image2.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734202391459\\image3.png', 250, 0, 1, 8, 5, 14, 1, 1),
	(15, 'Puffer Vest', 'This lightweight, quilted puffer vest is water-resistant and ideal for layering over hoodies or sweaters.', 5000, '2024-12-15 00:25:27', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734202526624\\image1.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734202526624\\image2.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734202526624\\image3.png', 0, 0, 1, 1, 4, 8, 1, 3),
	(16, 'Knit Sweater', 'This adorable knit sweater is made from organic cotton and designed for all-day comfort and warmth.', 600, '2024-12-15 00:28:28', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734202707953\\image1.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734202707953\\image2.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734202707953\\image3.png', 0, 0, 1, 2, 3, 5, 1, 4),
	(17, ' Cargo Shorts', 'These durable cargo shorts are perfect for outdoor adventures, featuring multiple pockets for convenience.', 1200, '2024-12-15 00:30:44', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734202843835\\image1.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734202843835\\image2.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734202843835\\image3.png', 0, 0, 1, 8, 3, 7, 1, 2),
	(18, 'Knit Cardigan', 'This lightweight, open-front cardigan is perfect for layering over casual or formal outfits.', 1250, '2024-12-15 00:35:00', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734203099647\\image1.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734203099647\\image2.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734203099647\\image3.png', 0, 0, 1, 2, 3, 8, 1, 1),
	(19, 'Tracksuit', 'Adorable two-piece tracksuit with a zip-up hoodie and matching pants, made from soft, breathable fabric.', 900, '2024-12-15 00:41:03', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734203462828\\image1.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734203462828\\image2.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734203462828\\image3.png', 50, 0, 1, 9, 3, 15, 1, 4),
	(20, 'Slim Fit Jeans', 'Stylish slim-fit jeans designed for comfort and durability, perfect for casual or semi-formal wear.', 2100, '2024-12-15 00:43:33', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734203613337\\image1.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734203613337\\image2.png', 'F:\\pasindu\\Git\\project\\ColoShop\\web\\product-images\\1734203613337\\image3.png', 250, 0, 1, 8, 4, 10, 1, 2);

-- Dumping structure for table colo-shop.product_color
CREATE TABLE IF NOT EXISTS `product_color` (
  `id` int NOT NULL AUTO_INCREMENT,
  `color` varchar(15) NOT NULL,
  `status_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_product_color_status1_idx` (`status_id`),
  CONSTRAINT `fk_product_color_status1` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table colo-shop.product_color: ~9 rows (approximately)
INSERT INTO `product_color` (`id`, `color`, `status_id`) VALUES
	(1, 'Red', 1),
	(2, 'White', 1),
	(3, 'Green', 1),
	(4, 'Brown', 1),
	(5, 'Ash', 1),
	(6, 'Silver', 1),
	(7, 'Gen', 2),
	(8, 'Black', 1),
	(9, 'Blue', 1),
	(10, 'Navy Blue', 1);

-- Dumping structure for table colo-shop.reply
CREATE TABLE IF NOT EXISTS `reply` (
  `id` int NOT NULL AUTO_INCREMENT,
  `datetime` datetime NOT NULL,
  `reply` text NOT NULL,
  `message_id` int NOT NULL,
  `admin_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_reply_message1_idx` (`message_id`),
  KEY `fk_reply_admin1_idx` (`admin_id`),
  CONSTRAINT `fk_reply_admin1` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`id`),
  CONSTRAINT `fk_reply_message1` FOREIGN KEY (`message_id`) REFERENCES `message` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table colo-shop.reply: ~0 rows (approximately)

-- Dumping structure for table colo-shop.review
CREATE TABLE IF NOT EXISTS `review` (
  `id` int NOT NULL AUTO_INCREMENT,
  `review` text NOT NULL,
  `datetime` datetime NOT NULL,
  `user_id` int NOT NULL,
  `product_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_review_user1_idx` (`user_id`),
  KEY `fk_review_product1_idx` (`product_id`),
  CONSTRAINT `fk_review_product1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `fk_review_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table colo-shop.review: ~1 rows (approximately)

-- Dumping structure for table colo-shop.seller
CREATE TABLE IF NOT EXISTS `seller` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `status_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_seller_user1_idx` (`user_id`),
  KEY `fk_seller_status1_idx` (`status_id`),
  CONSTRAINT `fk_seller_status1` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`),
  CONSTRAINT `fk_seller_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table colo-shop.seller: ~0 rows (approximately)
INSERT INTO `seller` (`id`, `user_id`, `status_id`) VALUES
	(1, 1, 1);

-- Dumping structure for table colo-shop.size
CREATE TABLE IF NOT EXISTS `size` (
  `id` int NOT NULL AUTO_INCREMENT,
  `size` varchar(5) NOT NULL,
  `status_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_size_status1_idx` (`status_id`),
  CONSTRAINT `fk_size_status1` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table colo-shop.size: ~7 rows (approximately)
INSERT INTO `size` (`id`, `size`, `status_id`) VALUES
	(1, 'XS', 1),
	(2, 'S', 1),
	(3, 'M', 1),
	(4, 'L', 1),
	(5, 'XL', 1),
	(6, 'XXL', 1),
	(7, 'XXXL', 1);

-- Dumping structure for table colo-shop.status
CREATE TABLE IF NOT EXISTS `status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(10) NOT NULL COMMENT 'active/deactive',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table colo-shop.status: ~3 rows (approximately)
INSERT INTO `status` (`id`, `name`) VALUES
	(1, 'Active'),
	(2, 'De-Active'),
	(3, 'Pending'),
	(4, 'Deleted');

-- Dumping structure for table colo-shop.user
CREATE TABLE IF NOT EXISTS `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `email` varchar(60) NOT NULL,
  `password` varchar(45) NOT NULL,
  `token` varchar(45) DEFAULT NULL,
  `account_type_id` int NOT NULL,
  `status_id` int NOT NULL,
  `verified_status_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user_account_type1_idx` (`account_type_id`),
  KEY `fk_user_status1_idx` (`status_id`),
  KEY `fk_user_verified_status1_idx` (`verified_status_id`),
  CONSTRAINT `fk_user_account_type1` FOREIGN KEY (`account_type_id`) REFERENCES `account_type` (`id`),
  CONSTRAINT `fk_user_status1` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`),
  CONSTRAINT `fk_user_verified_status1` FOREIGN KEY (`verified_status_id`) REFERENCES `verified_status` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table colo-shop.user: ~2 rows (approximately)
INSERT INTO `user` (`id`, `first_name`, `last_name`, `email`, `password`, `token`, `account_type_id`, `status_id`, `verified_status_id`) VALUES
	(1, 'Pasindu', 'Jayasundara', 'pasindubathiya28@gmail.com', 'Pasindu328@Bhathiya', '760b2368', 3, 1, 1),
	(2, 'Jayasundara', 'Jayasundara', 'pasindujayasundara28@gmail.com', 'Pasindu328@', '14262ce2', 2, 1, 1);

-- Dumping structure for table colo-shop.verified_status
CREATE TABLE IF NOT EXISTS `verified_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `status` varchar(15) NOT NULL COMMENT 'verified\nnotverified',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table colo-shop.verified_status: ~0 rows (approximately)
INSERT INTO `verified_status` (`id`, `status`) VALUES
	(1, 'Verified'),
	(2, 'Not-verified');

-- Dumping structure for table colo-shop.wishlist
CREATE TABLE IF NOT EXISTS `wishlist` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `product_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_wishlist_user1_idx` (`user_id`),
  KEY `fk_wishlist_product1_idx` (`product_id`),
  CONSTRAINT `fk_wishlist_product1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `fk_wishlist_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table colo-shop.wishlist: ~1 rows (approximately)
INSERT INTO `wishlist` (`id`, `user_id`, `product_id`) VALUES
	(4, 2, 13);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
