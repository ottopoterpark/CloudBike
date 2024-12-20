-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: cloudbike
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bike`
--

DROP TABLE IF EXISTS `bike`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bike` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键（单车编号）',
  `number` varchar(15) NOT NULL COMMENT '单车编号',
  `name` varchar(100) NOT NULL COMMENT '单车名称',
  `type` int NOT NULL COMMENT '自行车类型（0：公路车，1：旅行车，2：折叠车，3：死飞，4：山地车，5：其他）',
  `price` int NOT NULL DEFAULT '0' COMMENT '售价',
  `image` text COMMENT '图片路径',
  `daily` int NOT NULL COMMENT '日租金',
  `monthly` int NOT NULL COMMENT '月租金',
  `status` int NOT NULL DEFAULT '0' COMMENT '状态（0：正常，1：待提车，2：租赁中，3：已售，4：待归还）',
  `age` int DEFAULT NULL COMMENT '车龄',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `size` int DEFAULT NULL COMMENT '车身大小（单位：寸）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `bike_pk` (`number`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='自行车表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bike`
--

LOCK TABLES `bike` WRITE;
/*!40000 ALTER TABLE `bike` DISABLE KEYS */;
INSERT INTO `bike` VALUES (6,'001','鸭梨牌山地车',4,6000,'https://uniquealuo-javaweb.oss-cn-shenzhen.aliyuncs.com/db1b47fe-8d21-4250-929b-f0057c686c43.png,https://uniquealuo-javaweb.oss-cn-shenzhen.aliyuncs.com/5ecd3ac2-0881-45a1-a2c9-049699796fee.jpg,https://uniquealuo-javaweb.oss-cn-shenzhen.aliyuncs.com/3e4339e5-1c44-460e-9090-f0d7e5a90543.png',20,550,2,2,'九九新',27),(7,'002','芒果牌旅行车',1,4500,'https://uniquealuo-javaweb.oss-cn-shenzhen.aliyuncs.com/db1b47fe-8d21-4250-929b-f0057c686c43.png,https://uniquealuo-javaweb.oss-cn-shenzhen.aliyuncs.com/5ecd3ac2-0881-45a1-a2c9-049699796fee.jpg,https://uniquealuo-javaweb.oss-cn-shenzhen.aliyuncs.com/3e4339e5-1c44-460e-9090-f0d7e5a90543.png',18,520,0,6,NULL,22),(8,'003','菠萝牌死飞',3,6500,'https://uniquealuo-javaweb.oss-cn-shenzhen.aliyuncs.com/db1b47fe-8d21-4250-929b-f0057c686c43.png,https://uniquealuo-javaweb.oss-cn-shenzhen.aliyuncs.com/5ecd3ac2-0881-45a1-a2c9-049699796fee.jpg,https://uniquealuo-javaweb.oss-cn-shenzhen.aliyuncs.com/3e4339e5-1c44-460e-9090-f0d7e5a90543.png',25,300,0,4,NULL,24),(11,'005','红枣牌折叠车',2,6500,'https://uniquealuo-javaweb.oss-cn-shenzhen.aliyuncs.com/db1b47fe-8d21-4250-929b-f0057c686c43.png,https://uniquealuo-javaweb.oss-cn-shenzhen.aliyuncs.com/5ecd3ac2-0881-45a1-a2c9-049699796fee.jpg,https://uniquealuo-javaweb.oss-cn-shenzhen.aliyuncs.com/3e4339e5-1c44-460e-9090-f0d7e5a90543.png',25,715,0,4,NULL,24),(14,'004','凤梨牌折叠车',2,6500,'https://uniquealuo-javaweb.oss-cn-shenzhen.aliyuncs.com/db1b47fe-8d21-4250-929b-f0057c686c43.png,https://uniquealuo-javaweb.oss-cn-shenzhen.aliyuncs.com/5ecd3ac2-0881-45a1-a2c9-049699796fee.jpg,https://uniquealuo-javaweb.oss-cn-shenzhen.aliyuncs.com/3e4339e5-1c44-460e-9090-f0d7e5a90543.png',25,720,0,3,NULL,25);
/*!40000 ALTER TABLE `bike` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart`
--

DROP TABLE IF EXISTS `cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `bike_id` int NOT NULL COMMENT '关联的单车id',
  `user_id` int NOT NULL COMMENT '关联的用户id',
  `type` int NOT NULL COMMENT '业务类型（0：日租，1：月租，2：购买）',
  `count` int NOT NULL DEFAULT '1' COMMENT '业务叠加数量',
  `payment` int NOT NULL COMMENT '合计',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='购物车表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart`
--

LOCK TABLES `cart` WRITE;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
INSERT INTO `cart` VALUES (2,8,10,1,4,1200),(3,7,10,1,2,1040),(6,11,10,0,10,250),(7,14,10,0,10,250);
/*!40000 ALTER TABLE `cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` char(13) NOT NULL COMMENT '用户名（手机号码）',
  `password` varchar(100) NOT NULL DEFAULT 'E10ADC3949BA59ABBE56E057F20F883E' COMMENT '密码',
  `authority` int NOT NULL DEFAULT '0' COMMENT '权限（0：普通，1：管理员）',
  `name` varchar(30) NOT NULL COMMENT '姓名',
  `status` int NOT NULL DEFAULT '1' COMMENT '账号状态（0：禁用，1：启用）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `employee_pk` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='运营团队表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (1,'66666666666','e10adc3949ba59abbe56e057f20f883e',1,'管理者',1),(2,'13560586666','e10adc3949ba59abbe56e057f20f883e',0,'张三',1),(3,'13560586667','e10adc3949ba59abbe56e057f20f883e',0,'李四',1),(6,'13560586668','e10adc3949ba59abbe56e057f20f883e',0,'王五',0);
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order`
--

DROP TABLE IF EXISTS `order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `number` char(10) NOT NULL COMMENT '订单编号（长度为10）',
  `type` int NOT NULL COMMENT '业务类型（0：日租，1：月租，2：购买）',
  `count` int NOT NULL DEFAULT '1' COMMENT '业务叠加数量',
  `create_time` datetime NOT NULL DEFAULT (now()) COMMENT '订单创建时间',
  `pick_time` datetime DEFAULT NULL COMMENT '提车时间',
  `payment` int NOT NULL COMMENT '实付款',
  `user_id` int NOT NULL COMMENT '创建订单的用户',
  `bike_id` int NOT NULL COMMENT '关联的自行车',
  `status` int NOT NULL DEFAULT '0' COMMENT '订单状态（0：待付款，1：待提车，2：租赁中，3：已完成，4：待归还，5：已取消）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_pk` (`number`),
  KEY `user_id` (`user_id`),
  KEY `bike_id` (`bike_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order`
--

LOCK TABLES `order` WRITE;
/*!40000 ALTER TABLE `order` DISABLE KEYS */;
INSERT INTO `order` VALUES (6,'0000000006',1,2,'2024-12-07 02:02:50','2024-12-07 14:00:00',600,8,6,2),(8,'11eee1eea2',0,10,'2024-12-15 13:44:10','2024-12-16 11:11:01',250,10,11,3),(9,'6a92a01991',1,4,'2024-12-15 21:28:41',NULL,1200,10,8,5);
/*!40000 ALTER TABLE `order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ride`
--

DROP TABLE IF EXISTS `ride`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ride` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int NOT NULL COMMENT '发起者',
  `name` varchar(255) NOT NULL COMMENT '活动名称',
  `departure_time` datetime NOT NULL COMMENT '出发时间',
  `meeting_point` varchar(255) NOT NULL COMMENT '集合地点',
  `description` varchar(500) NOT NULL COMMENT '活动描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `image` text COMMENT '图片路径',
  `status` int NOT NULL DEFAULT '0' COMMENT '状态（0：审核中，1：已通过，2：已驳回，3：已结束）',
  `participants` int NOT NULL DEFAULT '1' COMMENT '参加人数',
  `max_people` int NOT NULL COMMENT '限制人数',
  `reason` varchar(255) DEFAULT NULL COMMENT '审核意见',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='骑行团表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ride`
--

LOCK TABLES `ride` WRITE;
/*!40000 ALTER TABLE `ride` DISABLE KEYS */;
INSERT INTO `ride` VALUES (5,8,'深大侠客行','2024-12-08 08:30:00','深圳大学沧海校区东北门','环绕深圳湾骑行，中途无休息，扫码进骑行群','2024-12-01 19:56:16',NULL,1,2,50,NULL),(6,9,'深大侠客行2.0','2024-12-10 08:30:00','深圳大学沧海校区东北门','环绕深圳湾骑行，中途无休息，扫码进骑行群','2024-12-07 02:32:16',NULL,1,2,60,NULL),(7,10,'深大侠客行3.0','2024-12-15 08:30:00','深圳大学沧海校区东北门','环绕深圳湾骑行，中途无休息，扫码进骑行群','2024-12-08 17:23:28',NULL,2,1,30,'图片违规'),(8,10,'深大侠客行4.0','2024-12-20 08:30:00','深圳大学沧海校区东北门','环绕深圳湾骑行，中途无休息，扫码进骑行群','2024-12-09 18:22:56','https://uniquealuo-javaweb.oss-cn-shenzhen.aliyuncs.com/01c6be10-6a17-41ea-a128-af9717723fa0.jpeg,https://uniquealuo-javaweb.oss-cn-shenzhen.aliyuncs.com/fc3cc630-dc53-43f9-a407-09c8db870163.jpg,https://uniquealuo-javaweb.oss-cn-shenzhen.aliyuncs.com/f92802bc-a815-4f26-9dee-27077b542b8d.jpg',1,1,30,NULL),(9,10,'深大侠客行5.0','2024-12-22 08:30:00','深圳大学沧海校区东北门','环绕深圳湾骑行，中途无休息，扫码进骑行群','2024-12-09 18:24:39','https://uniquealuo-javaweb.oss-cn-shenzhen.aliyuncs.com/01c6be10-6a17-41ea-a128-af9717723fa0.jpeg,https://uniquealuo-javaweb.oss-cn-shenzhen.aliyuncs.com/fc3cc630-dc53-43f9-a407-09c8db870163.jpg,https://uniquealuo-javaweb.oss-cn-shenzhen.aliyuncs.com/f92802bc-a815-4f26-9dee-27077b542b8d.jpg',1,1,30,NULL);
/*!40000 ALTER TABLE `ride` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ride_detail`
--

DROP TABLE IF EXISTS `ride_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ride_detail` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ride_id` int NOT NULL COMMENT '骑行团',
  `user_id` int NOT NULL COMMENT '参与者',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='骑行团明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ride_detail`
--

LOCK TABLES `ride_detail` WRITE;
/*!40000 ALTER TABLE `ride_detail` DISABLE KEYS */;
INSERT INTO `ride_detail` VALUES (2,5,8),(3,6,9),(8,5,9),(9,6,10),(10,7,10),(11,8,10),(12,9,10);
/*!40000 ALTER TABLE `ride_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(255) NOT NULL COMMENT '用户名',
  `balance` int NOT NULL DEFAULT '0' COMMENT '虚拟货币',
  `openid` varchar(50) NOT NULL COMMENT '微信用户唯一标识',
  `phone` varchar(11) DEFAULT NULL COMMENT '电话号码',
  `ride_times` int NOT NULL DEFAULT '0' COMMENT '发起骑行次数',
  `credit` int NOT NULL DEFAULT '1' COMMENT '信用（0：冻结，1：正常）',
  `image` text COMMENT '头像图片路径',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_pk` (`username`),
  UNIQUE KEY `user_pk_2` (`openid`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (8,'用户897504204280681553',2368,'1','15813944123',1,1,NULL),(9,'用户213882074905056262',2048,'2','',1,1,NULL),(10,'用户380981567271531510',1798,'3','13560576544',2,1,'https://uniquealuo-javaweb.oss-cn-shenzhen.aliyuncs.com/db1b47fe-8d21-4250-929b-f0057c686c43.png'),(11,'用户391047278315086854',0,'owvYO7dfZ0BO0zr_j8KoahBnFdoY',NULL,0,1,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-12-16 15:23:22
