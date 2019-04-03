-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: database_name
-- ------------------------------------------------------
-- Server version	5.0.27-community

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
-- Not dumping tablespaces as no INFORMATION_SCHEMA.FILES table on this server
--

--
-- Table structure for table `tbl_member`
--

DROP TABLE IF EXISTS `tbl_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_member` (
  `MEMBER_ID` varchar(20) NOT NULL,
  `PASSWD` varchar(50) NOT NULL,
  `NAME` varchar(30) NOT NULL,
  `JUMIN` varchar(13) NOT NULL,
  `ZIPCODE` varchar(6) NOT NULL,
  `ADDR1` varchar(100) NOT NULL,
  `ADDR2` varchar(100) NOT NULL,
  `TELNO` varchar(20) default NULL,
  `EMAIL` varchar(50) NOT NULL,
  `LEVEL_F` varchar(1) NOT NULL,
  `DELETE_F` varchar(1) NOT NULL,
  `REG_DATE` datetime NOT NULL,
  PRIMARY KEY  (`MEMBER_ID`),
  UNIQUE KEY `PK_TBL_MEMBER` (`MEMBER_ID`),
  UNIQUE KEY `UK_TBL_MEMBER` (`JUMIN`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_member`
--

LOCK TABLES `tbl_member` WRITE;
/*!40000 ALTER TABLE `tbl_member` DISABLE KEYS */;
INSERT INTO `tbl_member` (`MEMBER_ID`, `PASSWD`, `NAME`, `JUMIN`, `ZIPCODE`, `ADDR1`, `ADDR2`, `TELNO`, `EMAIL`, `LEVEL_F`, `DELETE_F`, `REG_DATE`) VALUES ('administrator','750E6F005452','이름','1234561234567','12345','주소1','주소2','010-1234-1234','abc@abc.com','A','N',NOW());
/*!40000 ALTER TABLE `tbl_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_group`
--

DROP TABLE IF EXISTS `tbl_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_group` (
  `GROUP_CODE` varchar(11) NOT NULL,
  `PARENT_CODE` varchar(11) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `DESCRIPTION` varchar(200) default NULL,
  `REG_DATE` datetime NOT NULL,
  `GROUP_SEQ` int(11) NOT NULL,
  `USE_F` varchar(1) NOT NULL,
  PRIMARY KEY  (`GROUP_CODE`),
  UNIQUE KEY `PK_TBL_GROUP` (`GROUP_CODE`),
  KEY `PARENT_CODE` (`PARENT_CODE`),
  CONSTRAINT `fk_tbl_group` FOREIGN KEY (`PARENT_CODE`) REFERENCES `tbl_group` (`GROUP_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_group`
--

LOCK TABLES `tbl_group` WRITE;
/*!40000 ALTER TABLE `tbl_group` DISABLE KEYS */;
INSERT INTO `tbl_group` (`GROUP_CODE`, `PARENT_CODE`, `NAME`, `DESCRIPTION`, `REG_DATE`, `GROUP_SEQ`, `USE_F`) VALUES ('0','0','Root',		NULL,NOW(),1,'');
INSERT INTO `tbl_group` (`GROUP_CODE`, `PARENT_CODE`, `NAME`, `DESCRIPTION`, `REG_DATE`, `GROUP_SEQ`, `USE_F`) VALUES ('1','0','관리자',	NULL,NOW(),1,'');
/*!40000 ALTER TABLE `tbl_group` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-01-01 00:00:00
