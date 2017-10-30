/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50719
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2017-09-07 10:39:43
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `sys_department`
-- ----------------------------
DROP TABLE IF EXISTS `sys_department`;
CREATE TABLE `sys_department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `is_delete` int(2) DEFAULT NULL,
  `insert_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_department
-- ----------------------------
INSERT INTO sys_department VALUES ('1', '采购部', '0', '2017-09-01 11:45:49');
INSERT INTO sys_department VALUES ('2', '市场部', '0', '2017-09-01 11:46:02');

-- ----------------------------
-- Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `department_id` int(11) DEFAULT NULL,
  `nickname` varchar(30) DEFAULT NULL,
  `username` varchar(16) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL,
  `insert_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO sys_user VALUES ('1', '1', '采购1', 'caigou1', '123qweqwe', '2017-09-01 11:46:34');
INSERT INTO sys_user VALUES ('2', '1', '采购2', 'caigou2', '123qweqwe', '2017-09-01 11:46:53');
INSERT INTO sys_user VALUES ('3', '2', '市场1', 'shichang1', '123qweqwe', '2017-09-01 11:47:15');
INSERT INTO sys_user VALUES ('4', '1', '哈哈0', 'test0', '123456', '2017-09-05 11:31:17');
INSERT INTO sys_user VALUES ('5', '1', '哈哈1', 'test1', '123456', '2017-09-05 11:31:17');
INSERT INTO sys_user VALUES ('6', '1', '哈哈2', 'test2', '123456', '2017-09-05 11:31:17');
INSERT INTO sys_user VALUES ('7', '1', '哈哈3', 'test3', '123456', '2017-09-05 11:31:17');
INSERT INTO sys_user VALUES ('8', '1', '哈哈4', 'test4', '123456', '2017-09-05 11:31:17');
INSERT INTO sys_user VALUES ('9', '1', '哈哈5', 'test5', '123456', '2017-09-05 11:31:17');
INSERT INTO sys_user VALUES ('10', '1', '哈哈6', 'test6', '123456', '2017-09-05 11:31:17');
INSERT INTO sys_user VALUES ('11', '1', '哈哈7', 'test7', '123456', '2017-09-05 11:31:17');
INSERT INTO sys_user VALUES ('12', '1', '哈哈8', 'test8', '123456', '2017-09-05 11:31:17');
INSERT INTO sys_user VALUES ('13', '1', '哈哈9', 'test9', '123456', '2017-09-05 11:31:17');
INSERT INTO sys_user VALUES ('14', '1', '哈哈10', 'test10', '123456', '2017-09-05 11:31:17');
INSERT INTO sys_user VALUES ('15', '1', '哈哈11', 'test11', '123456', '2017-09-05 11:31:17');
INSERT INTO sys_user VALUES ('16', '1', '哈哈12', 'test12', '123456', '2017-09-05 11:31:17');
INSERT INTO sys_user VALUES ('17', '1', '哈哈13', 'test13', '123456', '2017-09-05 11:31:17');
INSERT INTO sys_user VALUES ('18', '1', '哈哈14', 'test14', '123456', '2017-09-05 11:31:17');
INSERT INTO sys_user VALUES ('19', '1', '哈哈15', 'test15', '123456', '2017-09-05 11:31:17');
INSERT INTO sys_user VALUES ('20', '1', '哈哈16', 'test16', '123456', '2017-09-05 11:31:17');
INSERT INTO sys_user VALUES ('21', '1', '哈哈17', 'test17', '123456', '2017-09-05 11:31:17');
INSERT INTO sys_user VALUES ('22', '1', '哈哈18', 'test18', '123456', '2017-09-05 11:31:17');
INSERT INTO sys_user VALUES ('23', '1', '哈哈19', 'test19', '123456', '2017-09-05 11:31:17');
INSERT INTO sys_user VALUES ('24', '1', '哈哈20', 'test20', '123456', '2017-09-05 11:31:17');
INSERT INTO sys_user VALUES ('25', '2', '呵呵0', 'ceshi0', '123456', '2017-09-05 11:34:00');
INSERT INTO sys_user VALUES ('26', '2', '呵呵1', 'ceshi1', '123456', '2017-09-05 11:34:00');
INSERT INTO sys_user VALUES ('27', '2', '呵呵2', 'ceshi2', '123456', '2017-09-05 11:34:00');
INSERT INTO sys_user VALUES ('28', '2', '呵呵3', 'ceshi3', '123456', '2017-09-05 11:34:00');
INSERT INTO sys_user VALUES ('29', '2', '呵呵4', 'ceshi4', '123456', '2017-09-05 11:34:00');
INSERT INTO sys_user VALUES ('30', '2', '呵呵5', 'ceshi5', '123456', '2017-09-05 11:34:00');
INSERT INTO sys_user VALUES ('31', '2', '呵呵6', 'ceshi6', '123456', '2017-09-05 11:34:00');
INSERT INTO sys_user VALUES ('32', '2', '呵呵7', 'ceshi7', '123456', '2017-09-05 11:34:00');
INSERT INTO sys_user VALUES ('33', '2', '呵呵8', 'ceshi8', '123456', '2017-09-05 11:34:00');
INSERT INTO sys_user VALUES ('34', '2', '呵呵9', 'ceshi9', '123456', '2017-09-05 11:34:00');
INSERT INTO sys_user VALUES ('35', '2', '呵呵10', 'ceshi10', '123456', '2017-09-05 11:34:00');
INSERT INTO sys_user VALUES ('36', '2', '呵呵11', 'ceshi11', '123456', '2017-09-05 11:34:00');
INSERT INTO sys_user VALUES ('37', '2', '呵呵12', 'ceshi12', '123456', '2017-09-05 11:34:00');
INSERT INTO sys_user VALUES ('38', '2', '呵呵13', 'ceshi13', '123456', '2017-09-05 11:34:00');
INSERT INTO sys_user VALUES ('39', '2', '呵呵14', 'ceshi14', '123456', '2017-09-05 11:34:00');
INSERT INTO sys_user VALUES ('40', '2', '呵呵15', 'ceshi15', '123456', '2017-09-05 11:34:00');
INSERT INTO sys_user VALUES ('41', '2', '呵呵16', 'ceshi16', '123456', '2017-09-05 11:34:00');
INSERT INTO sys_user VALUES ('42', '2', '呵呵17', 'ceshi17', '123456', '2017-09-05 11:34:00');
INSERT INTO sys_user VALUES ('43', '2', '呵呵18', 'ceshi18', '123456', '2017-09-05 11:34:00');
INSERT INTO sys_user VALUES ('44', '2', '呵呵19', 'ceshi19', '123456', '2017-09-05 11:34:00');
INSERT INTO sys_user VALUES ('45', '2', '呵呵20', 'ceshi20', '123456', '2017-09-05 11:34:00');
