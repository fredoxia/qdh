/*
Navicat MySQL Data Transfer

Source Server         : MySQL
Source Server Version : 50544
Source Host           : localhost:3306
Source Database       : qdh

Target Server Type    : MYSQL
Target Server Version : 50544
File Encoding         : 65001

Date: 2016-04-27 20:06:35
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for order_export_log
-- ----------------------------
DROP TABLE IF EXISTS `order_export_log`;
CREATE TABLE `order_export_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_identity` varchar(15) NOT NULL,
  `import_time` datetime NOT NULL,
  `num_of_orders` mediumint(9) NOT NULL,
  `operator` varchar(15) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_export_log
-- ----------------------------
INSERT INTO `order_export_log` VALUES ('1', '201503', '2015-03-30 13:47:17', '123', '夏林');
INSERT INTO `order_export_log` VALUES ('2', '201604', '2016-04-29 13:47:32', '345', '周届');
INSERT INTO `order_export_log` VALUES ('3', '201405', '2014-05-06 13:47:53', '456', '夏林');
INSERT INTO `order_export_log` VALUES ('4', '201701', '2017-01-01 13:48:22', '233', '夏林');
