/*
 Navicat MySQL Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80020
 Source Host           : localhost:3306
 Source Schema         : hypad

 Target Server Type    : MySQL
 Target Server Version : 80020
 File Encoding         : 65001

 Date: 27/08/2020 09:19:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `is_delete` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除,1删除，0未删',
  `cst_create` datetime(0) NOT NULL COMMENT 'create时间',
  `cst_modified` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT 'modified时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for now_person
-- ----------------------------
DROP TABLE IF EXISTS `now_person`;
CREATE TABLE `now_person`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '人员编号',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '姓名',
  `gender` tinyint NOT NULL DEFAULT 0 COMMENT '性别',
  `cst_entry` datetime(0) NOT NULL COMMENT '入职时间(中国国家标准时间)',
  `ranknum` tinyint NOT NULL DEFAULT 0 COMMENT '历史职级数量',
  `cst_resign` datetime(0) NULL DEFAULT NULL COMMENT '离职时间',
  `resign_reason` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '离职原因',
  `department` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '部门',
  `post` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '岗位',
  `school` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学校',
  `major` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '专业',
  `qualification` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学历',
  `cst_graduation` datetime(0) NOT NULL COMMENT '毕业时间',
  `phone` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  `more` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_delete` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除,1删除，0未删',
  `cst_create` datetime(0) NOT NULL COMMENT 'create时间',
  `cst_modified` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT 'modified时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for now_person_rank
-- ----------------------------
DROP TABLE IF EXISTS `now_person_rank`;
CREATE TABLE `now_person_rank`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id-bigint',
  `personid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '人员编号',
  `rankid` bigint NOT NULL COMMENT 'id-bigint',
  `reason` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '升/降职原因',
  `shunxu` int NOT NULL COMMENT '职级位置',
  `cst_change` datetime(0) NULL DEFAULT NULL COMMENT '职级升降时间',
  `is_delete` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除,1删除，0未删',
  `cst_create` datetime(0) NOT NULL COMMENT 'create时间',
  `cst_modified` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT 'modified时间',
  PRIMARY KEY (`id`, `personid`, `rankid`) USING BTREE,
  INDEX `personid`(`personid`) USING BTREE,
  INDEX `rankid`(`rankid`) USING BTREE,
  CONSTRAINT `personid` FOREIGN KEY (`personid`) REFERENCES `now_person` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `rankid` FOREIGN KEY (`rankid`) REFERENCES `now_rank` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for now_rank
-- ----------------------------
DROP TABLE IF EXISTS `now_rank`;
CREATE TABLE `now_rank`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id-bigint',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '职级名称',
  `decription` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职级描述',
  `is_delete` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除,1删除，0未删',
  `cst_create` datetime(0) NOT NULL COMMENT 'create时间',
  `cst_modified` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT 'modified时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pad_key
-- ----------------------------
DROP TABLE IF EXISTS `pad_key`;
CREATE TABLE `pad_key`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '关键词id',
  `keyname` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '关键词',
  `keydes` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关键词描述',
  `is_delete` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除,1删除，0未删',
  `gmt_create` datetime(0) NOT NULL COMMENT 'create时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT 'modified时间',
  PRIMARY KEY (`id`, `keyname`) USING BTREE,
  INDEX `keyname`(`keyname`) USING BTREE,
  INDEX `id`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pad_person
-- ----------------------------
DROP TABLE IF EXISTS `pad_person`;
CREATE TABLE `pad_person`  (
  `id` bigint(20) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT COMMENT '关键词id',
  `name` varchar(32) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL COMMENT '人员姓名',
  `gender` tinyint NOT NULL DEFAULT 1 COMMENT '人员性别',
  `school` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL COMMENT '学校',
  `major` varchar(32) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL COMMENT '专业',
  `qualification` varchar(32) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL COMMENT '学历',
  `workexp` tinyint NULL DEFAULT 0 COMMENT '工作年限',
  `salarynow` varchar(32) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL COMMENT '目前薪资',
  `salaryexpect` varchar(32) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL COMMENT '期望薪资',
  `induction` datetime(0) NOT NULL COMMENT '简历日期',
  `employfrom` varchar(32) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL COMMENT '招聘渠道',
  `gmt_report` datetime(0) NULL DEFAULT NULL COMMENT '毕业时间',
  `lightcomp` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '亮点公司',
  `isinterview` tinyint NULL DEFAULT NULL COMMENT '是否面试,1是,0否',
  `interviewres` tinyint NULL DEFAULT NULL COMMENT '面试结果,1通过,0未通过',
  `workset` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL COMMENT '备注',
  `phone` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '88888888' COMMENT '手机',
  `email` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'someone@hydeze.com' COMMENT '邮箱',
  `is_delete` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除,1删除，0未删',
  `gmt_create` datetime(0) NOT NULL COMMENT 'create时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT 'modified时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 307 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for personkey
-- ----------------------------
DROP TABLE IF EXISTS `personkey`;
CREATE TABLE `personkey`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `personid` bigint UNSIGNED NOT NULL COMMENT '人员id',
  `keyid` bigint UNSIGNED NOT NULL COMMENT '关键词id',
  `is_delete` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除,1删除，0未删',
  `gmt_create` datetime(0) NOT NULL COMMENT 'create时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT 'modified时间',
  PRIMARY KEY (`id`, `personid`, `keyid`) USING BTREE,
  INDEX `person`(`personid`) USING BTREE,
  INDEX `key`(`keyid`) USING BTREE,
  CONSTRAINT `key` FOREIGN KEY (`keyid`) REFERENCES `pad_key` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `person` FOREIGN KEY (`personid`) REFERENCES `pad_person` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 599 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
