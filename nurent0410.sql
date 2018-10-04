-- MySQL dump 10.13  Distrib 5.7.23, for Linux (x86_64)
--
-- Host: localhost    Database: nurent
-- ------------------------------------------------------
-- Server version	5.7.23-0ubuntu0.16.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Accounts`
--

DROP TABLE IF EXISTS `Accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Accounts` (
  `name` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `country` varchar(100) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `info` varchar(3000) DEFAULT NULL,
  PRIMARY KEY (`name`),
  UNIQUE KEY `Accounts_email_uindex` (`email`),
  UNIQUE KEY `Accounts_phone_uindex` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Accounts`
--

LOCK TABLES `Accounts` WRITE;
/*!40000 ALTER TABLE `Accounts` DISABLE KEYS */;
INSERT INTO `Accounts` VALUES ('Alik7896','917891b0ccfa94cabc309269a5fa17fdcc5c2d0b','Kazakhstan','Astana','Alik7896@mail.ru','8 777 665 9090','inho placeholder'),('Jony112','323bab10263cfa7249395aaa100870bb02641064',NULL,NULL,NULL,NULL,NULL),('Jony114','dfaf612609d64067072295e38def7eaf788a3dd3',NULL,NULL,NULL,NULL,NULL),('user1','password1','Kazakhstan','Kokshetau','user1@mail.ru','87770889976','info1'),('user2','password2','country2','city2','email2','phone2','info2'),('user3','password3','country3','city3','email3','phone3','info3'),('user4','349bb98cb5173c851692a1f67d51547a0270932a','country4','city4','email4','phone4','info4');
/*!40000 ALTER TABLE `Accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Listings`
--

DROP TABLE IF EXISTS `Listings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Listings` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `title` varchar(200) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `description` varchar(3000) DEFAULT NULL,
  `image` blob,
  `price` int(11) DEFAULT NULL,
  `postdate` varchar(100) DEFAULT NULL,
  `contactinfo` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Listings_Accounts_name_fk` (`username`),
  CONSTRAINT `Listings_Accounts_name_fk` FOREIGN KEY (`username`) REFERENCES `Accounts` (`name`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Listings`
--

LOCK TABLES `Listings` WRITE;
/*!40000 ALTER TABLE `Listings` DISABLE KEYS */;
INSERT INTO `Listings` VALUES (1,'user1','title1','addres1','listing1',NULL,4000,'03.10.18 21:54','phone: 8 777 033 7676');
/*!40000 ALTER TABLE `Listings` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-10-04 20:06:08
