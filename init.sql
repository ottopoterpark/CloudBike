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
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` varchar(50) DEFAULT NULL COMMENT '自行车类型',
  `pic` text COMMENT '图片',
  `price` int NOT NULL DEFAULT '0' COMMENT '售价',
  `status` int NOT NULL DEFAULT '0' COMMENT '状态（0：正常，1：租赁中，2：已售，3：待归还）',
  `age` int DEFAULT NULL COMMENT '车龄',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `size` int DEFAULT NULL COMMENT '车身大小（单位：寸）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='自行车表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bike`
--

LOCK TABLES `bike` WRITE;
/*!40000 ALTER TABLE `bike` DISABLE KEYS */;
INSERT INTO `bike` VALUES (1,'山地车','images/bike1.jpg,images/bike1a.jpg',1500,1,2,'九九新',NULL),(2,'公路车','images/bike2.jpg,images/bike2a.jpg',2500,1,1,'九九新',NULL),(3,'折叠车','images/bike3.jpg,images/bike3a.jpg',800,2,3,'九九新',NULL),(4,'城市车','images/bike4.jpg,images/bike4a.jpg',4000,1,2,'九九新',NULL),(5,'电动自行车','images/bike5.jpg,images/bike5a.jpg',1000,1,1,'九九新',NULL);
/*!40000 ALTER TABLE `bike` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(100) DEFAULT NULL COMMENT '昵称',
  `password` varchar(100) NOT NULL DEFAULT 'E10ADC3949BA59ABBE56E057F20F883E' COMMENT '密码',
  `authority` int NOT NULL DEFAULT '0' COMMENT '权限（0：普通，1：管理员）',
  `name` varchar(30) NOT NULL COMMENT '姓名',
  `status` int NOT NULL DEFAULT '1' COMMENT '账号状态（0：禁用，1：启用）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `employee_pk` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='运营团队表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (1,'admin','e10adc3949ba59abbe56e057f20f883e',1,'管理者',1),(2,'zhangsan','e10adc3949ba59abbe56e057f20f883e',0,'张三',1),(3,'LiSi','e10adc3949ba59abbe56e057f20f883e',0,'李四',1),(6,'WangWu','e10adc3949ba59abbe56e057f20f883e',0,'老王',1);
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
  `type` int NOT NULL COMMENT '业务类型（0：租赁，1：收购）',
  `duration` int DEFAULT NULL COMMENT '租赁类型（0：天，1：月，2：学期）',
  `create_time` datetime NOT NULL DEFAULT (now()) COMMENT '订单创建时间',
  `payment` decimal(10,2) NOT NULL COMMENT '实付款',
  `user_id` int NOT NULL COMMENT '创建订单的用户',
  `bike_id` int NOT NULL COMMENT '关联的自行车',
  `status` int NOT NULL DEFAULT '0' COMMENT '订单状态（0：待付款，1：待提车，2：租赁中，3：已完成，4：待归还，5：已取消）',
  `update_time` datetime DEFAULT (now()) COMMENT '订单状态更新时间',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `bike_id` (`bike_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order`
--

LOCK TABLES `order` WRITE;
/*!40000 ALTER TABLE `order` DISABLE KEYS */;
INSERT INTO `order` VALUES (1,0,0,'2024-11-19 08:00:00',12.00,1,1,2,'2024-11-19 22:54:24'),(2,0,1,'2024-11-19 09:00:00',50.00,1,2,2,'2024-11-19 22:54:26'),(3,1,NULL,'2024-11-19 22:43:41',1500.00,1,3,3,'2024-11-19 22:54:27'),(4,0,2,'2024-11-19 10:00:00',200.00,1,4,2,'2024-11-19 22:54:29'),(5,0,0,'2024-11-02 11:00:00',20.00,1,5,2,'2024-11-19 22:54:30');
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
  `image1` varchar(255) DEFAULT NULL COMMENT '图片1',
  `image2` varchar(255) DEFAULT NULL COMMENT '图片2',
  `image3` varchar(255) DEFAULT NULL COMMENT '图片3',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` int NOT NULL DEFAULT '0' COMMENT '申请状态（0：审核中，1：已通过，2：已驳回，3：已结束）',
  `participants` int NOT NULL DEFAULT '1' COMMENT '参加人数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='骑行团表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ride`
--

LOCK TABLES `ride` WRITE;
/*!40000 ALTER TABLE `ride` DISABLE KEYS */;
INSERT INTO `ride` VALUES (5,7,'深大侠客行','2024-12-02 08:30:00','深圳大学沧海校区东北门','环绕深圳湾骑行，中途无休息，扫码进骑行群','https://uniquealuo-javaweb.oss-cn-shenzhen.aliyuncs.com/01c6be10-6a17-41ea-a128-af9717723fa0.jpeg','https://uniquealuo-javaweb.oss-cn-shenzhen.aliyuncs.com/fc3cc630-dc53-43f9-a407-09c8db870163.jpg','https://uniquealuo-javaweb.oss-cn-shenzhen.aliyuncs.com/f92802bc-a815-4f26-9dee-27077b542b8d.jpg','2024-12-01 19:56:16','2024-12-01 19:56:16',0,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='骑行团明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ride_detail`
--

LOCK TABLES `ride_detail` WRITE;
/*!40000 ALTER TABLE `ride_detail` DISABLE KEYS */;
INSERT INTO `ride_detail` VALUES (2,5,7);
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
  `balance` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '虚拟货币',
  `openid` varchar(50) NOT NULL COMMENT '微信用户唯一标识',
  `phone` varchar(11) DEFAULT NULL COMMENT '电话号码',
  `ride_times` int NOT NULL DEFAULT '0' COMMENT '发起骑行次数',
  `credit` int NOT NULL DEFAULT '1' COMMENT '信用（0：冻结，1：正常）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_pk` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (7,'用户b9bf28a4-d4f3-4989-b655-51c0d19c0148',0.00,'1',NULL,3,1);
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

-- Dump completed on 2024-12-01 19:57:51
