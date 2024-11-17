-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: CloudBike
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

create database if not exists CloudBike;
use CloudBike;

--
-- Table structure for table `bike`
--

DROP TABLE IF EXISTS `bike`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bike` (
  `bike_id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `bike_type` varchar(50) DEFAULT NULL COMMENT '自行车类型',
  `bike_pic` text COMMENT '图片',
  `price` int NOT NULL DEFAULT '0' COMMENT '售价',
  `status` int NOT NULL DEFAULT '3' COMMENT '状态（0：正常，1：租赁中，2：已售，3：调整）',
  PRIMARY KEY (`bike_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='自行车表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bike`
--

LOCK TABLES `bike` WRITE;
/*!40000 ALTER TABLE `bike` DISABLE KEYS */;
INSERT INTO `bike` VALUES (1,'山地车','images/bike1.jpg,images/bike1a.jpg',1500,3),(2,'公路车','images/bike2.jpg,images/bike2a.jpg',2500,3),(3,'折叠车','images/bike3.jpg,images/bike3a.jpg',800,3),(4,'城市车','images/bike4.jpg,images/bike4a.jpg',4000,3),(5,'电动自行车','images/bike5.jpg,images/bike5a.jpg',1000,3);
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
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL DEFAULT 'E10ADC3949BA59ABBE56E057F20F883E' COMMENT '密码',
  PRIMARY KEY (`id`),
  UNIQUE KEY `employee_pk` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='运营团队表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (1,'unique','E10ADC3949BA59ABBE56E057F20F883E');
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order`
--

DROP TABLE IF EXISTS `order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order` (
  `order_id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_type` int NOT NULL COMMENT '业务类型（0：租赁，1：收购）',
  `duration` int DEFAULT NULL COMMENT '租赁类型（0：天，1：月，2：学期）',
  `rent` decimal(10,2) DEFAULT NULL COMMENT '租金',
  `create_time` datetime DEFAULT NULL COMMENT '订单创建时间',
  `price` decimal(10,2) DEFAULT NULL COMMENT '售价',
  `user_id` int NOT NULL COMMENT '创建订单的用户',
  `bike_id` int NOT NULL COMMENT '关联的自行车',
  PRIMARY KEY (`order_id`),
  KEY `user_id` (`user_id`),
  KEY `bike_id` (`bike_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order`
--

LOCK TABLES `order` WRITE;
/*!40000 ALTER TABLE `order` DISABLE KEYS */;
INSERT INTO `order` VALUES (1,1,1,1.50,'2024-11-01 08:00:00',NULL,1,1),(2,1,2,50.00,'2024-10-01 09:00:00',NULL,2,2),(3,2,NULL,NULL,NULL,1500.00,3,3),(4,1,3,1000.00,'2024-09-01 10:00:00',NULL,4,4),(5,1,1,15.00,'2024-11-02 11:00:00',NULL,5,5);
/*!40000 ALTER TABLE `order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `balance` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '虚拟货币',
  `openid` varchar(50) NOT NULL COMMENT '微信用户唯一标识',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_pk` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'张三',100.00,'oMFqf7ZzyxSEs_Ls6npsy5yMBr8M');
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

-- Dump completed on 2024-11-17 22:05:42
