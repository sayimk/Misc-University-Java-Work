-- MySQL dump 10.13  Distrib 5.7.9, for osx10.9 (x86_64)
--
-- Host: localhost    Database: CO3098_CW3_2017
-- ------------------------------------------------------
-- Server version	5.7.15

--
-- Table structure for table `BOOKMARK_ENTITY`
--

DROP TABLE IF EXISTS `BOOKMARK_ENTITY`;

CREATE TABLE `BOOKMARK_ENTITY` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `readonly` int(11) DEFAULT NULL,
  `itemtype` varchar(45) DEFAULT NULL,
  `content` longtext,
  `title` varchar(45) DEFAULT NULL,
  `parentPath` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- Dumping data for table `BOOKMARK_ENTITY`
--

INSERT INTO `BOOKMARK_ENTITY` VALUES (1,'Documents',0,'FOLDER',NULL,NULL,NULL),(2,'Books',0,'FOLDER',NULL,NULL,'Documents'),(3,'Holidays',0,'FOLDER',NULL,NULL,'Documents'),(4,'Fiction',0,'FOLDER',NULL,NULL,'Documents|Books'),(5,'NonFiction',0,'FOLDER',NULL,NULL,'Documents|Books'),(6,'Romance',0,'FOLDER',NULL,NULL,'Documents|Books|Fiction'),(7,'Horror',0,'FOLDER',NULL,NULL,'Documents|Books|Fiction'),(9,'Asia',0,'FOLDER',NULL,NULL,'Documents|Holidays'),(10,'Europe',0,'FOLDER',NULL,NULL,'Documents|Holidays'),(11,'Lonely_Planet',0,'LINK','http://www.lonelyplanet.com','Lonely Planet','Documents|Books'),(12,'London',0,'LOCATION','(51.5074,0.1278)','London','Documents|Holidays|Europe'),(13,'MyTravelPlan.txt',0,'TEXTFILE','I will visit Victoria & Albert Museum','MyTravelPlan.txt','Documents|Holidays|Europe');

-- Dump completed on 2017-11-25 17:06:14
