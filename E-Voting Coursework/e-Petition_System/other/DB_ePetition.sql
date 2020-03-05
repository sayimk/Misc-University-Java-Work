-- MySQL dump 10.13  Distrib 5.7.9, for osx10.9 (x86_64)
--


--
-- Table structure for table `NIC_RECORDS`
--

DROP TABLE IF EXISTS `NIC_RECORDS`;
CREATE TABLE `NIC_RECORDS` (
  `NIC` varchar(30) NOT NULL,
  `MP` int(11) DEFAULT NULL,
  `USED` int(11) DEFAULT NULL,
  PRIMARY KEY (`NIC`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `NIC_RECORDS`
--


/*!40000 ALTER TABLE `NIC_RECORDS` DISABLE KEYS */;
INSERT INTO `NIC_RECORDS` VALUES ('34GC829B',0,0),('4F7YKH9G',1,0),('57UBS5J6',0,0),('8RL4ENTK',0,0),('CB8FBCCM',1,0),('CCZTQ8KW',0,0),('FEQQ6UUG',0,0),('MM2874Z6',1,0),('R9KZ2NXL',0,0),('YBQUVXHL',0,0);
/*!40000 ALTER TABLE `NIC_RECORDS` ENABLE KEYS */;


--
-- Table structure for table `PETITIONS`
--

DROP TABLE IF EXISTS `PETITIONS`;
CREATE TABLE `PETITIONS` (
  `ID` int(11) NOT NULL,
  `TITLE` varchar(80) DEFAULT NULL,
  `CONTENT` longtext,
  `DATE` date DEFAULT NULL,
  `CREATOR` varchar(45) DEFAULT NULL,
  `SIGN` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Dumping data for table `PETITIONS`
--

INSERT INTO `PETITIONS` VALUES (1,'Universal Basic Income ','Introducing Universal Basic Income for all citizens in Shangri-La ','2017-11-02',NULL,NULL);


--
-- Table structure for table `COMMENTS`
--

DROP TABLE IF EXISTS `COMMENTS`;
CREATE TABLE `COMMENTS` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `COMMENT` longtext,
  `BY_MP` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- Dump completed on 2017-11-02  2:13:01
