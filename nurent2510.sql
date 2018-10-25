-- MySQL dump 10.13  Distrib 5.7.24, for Linux (x86_64)
--
-- Host: localhost    Database: nurent
-- ------------------------------------------------------
-- Server version	5.7.24-0ubuntu0.16.04.1

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
  `email` varchar(100) NOT NULL,
  `password` varchar(500) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `surname` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `rating` float DEFAULT '0',
  `votes` int(11) DEFAULT '0',
  `token` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`email`),
  UNIQUE KEY `Accounts_phone_uindex` (`phone`),
  UNIQUE KEY `Accounts_token_uindex` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Accounts`
--

LOCK TABLES `Accounts` WRITE;
/*!40000 ALTER TABLE `Accounts` DISABLE KEYS */;
INSERT INTO `Accounts` VALUES ('a@a.com','ee1f0a870c1b20c74cbc44cfbdb01c44d5aaac6f',NULL,NULL,NULL,0,0,'0c6c5806-6534-4521-a969-bda4644c2f91'),('adil@example.com','15cc99139a2f7c3c09b10adae1ed04145f95614b',NULL,'',NULL,NULL,0,NULL),('Alik7896','917891b0ccfa94cabc309269a5fa17fdcc5c2d0b','Alik7896@mail.ru','','8 777 665 9090',NULL,0,NULL),('Jony112','323bab10263cfa7249395aaa100870bb02641064',NULL,'',NULL,NULL,0,NULL),('Jony114','dfaf612609d64067072295e38def7eaf788a3dd3',NULL,'',NULL,NULL,0,NULL),('Jony115','3c3c33b7d52f10e20edd67f35123af2e723aa7b7',NULL,'',NULL,NULL,0,NULL),('test1','fd',NULL,NULL,NULL,0,0,'f425aad0-6f5c-431a-afc8-15e7cf474649'),('test2','fg',NULL,NULL,NULL,0,0,NULL),('test3','dfg',NULL,NULL,NULL,0,0,NULL),('test4','dfdfv',NULL,NULL,NULL,0,0,NULL),('user1','password1','user1@mail.ru','','87770889976',NULL,0,NULL),('user1@mail.ru','3c3c33b7d52f10e20edd67f35123af2e723aa7b7',NULL,'',NULL,NULL,0,NULL),('user2','password2','email2','','phone2',NULL,0,NULL),('user3','password3','email3','','phone3',NULL,0,NULL),('user4','349bb98cb5173c851692a1f67d51547a0270932a','email4','','phone4',NULL,0,NULL);
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
  `email` varchar(100) NOT NULL,
  `title` varchar(200) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `building` varchar(200) DEFAULT NULL,
  `num_of_rooms` int(11) DEFAULT NULL,
  `description` varchar(3000) DEFAULT NULL,
  `image` blob,
  `price` int(11) DEFAULT NULL,
  `postdate` varchar(100) DEFAULT NULL,
  `contact_info` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Listings_Accounts_name_fk` (`email`),
  CONSTRAINT `Listings_Accounts_name_fk` FOREIGN KEY (`email`) REFERENCES `Accounts` (`email`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Listings`
--

LOCK TABLES `Listings` WRITE;
/*!40000 ALTER TABLE `Listings` DISABLE KEYS */;
INSERT INTO `Listings` VALUES (1,'user1','title1','city1','addres1',1,'A cottage',NULL,1000,'03.10.18 21:54','phone: 8 777 033 7676'),(2,'user1@mail.ru','title2','city1','adress2',2,'appartment with 2 rooms',NULL,2000,'04.10.18 13:18','phone: 8 702 544 6758'),(3,'Jony112','title3','city2','address3',3,'description3',NULL,3000,'05.10.18 13:23','phone 8 706 986 4356'),(4,'Jony114','EXAMPLE','city2','addres4',4,'desc',NULL,4000,'05.10.18','smt'),(6,'test1','t1','dity1','nah',2,'k',NULL,1000,'05','fdsvjnsdlvkjns'),(7,'test2','t2 ','fity','dkjfvnk',3,'g',NULL,500,'sdv','dsvfsdv');
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

-- Dump completed on 2018-10-25 15:58:27
