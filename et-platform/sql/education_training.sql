/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50610
Source Host           : 127.0.0.1:3306
Source Database       : education_training

Target Server Type    : MYSQL
Target Server Version : 50610
File Encoding         : 65001

Date: 2017-01-24 12:48:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for acc_account
-- ----------------------------
DROP TABLE IF EXISTS `acc_account`;
CREATE TABLE `acc_account` (
  `account_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '账号ID',
  `username` varchar(32) NOT NULL DEFAULT '' COMMENT '用户名',
  `mobile` varchar(16) NOT NULL DEFAULT '' COMMENT '手机号码',
  `password_strength` tinyint(4) NOT NULL COMMENT '密码强度',
  `salt` varchar(128) NOT NULL DEFAULT '' COMMENT '盐',
  `salt_password` varchar(64) NOT NULL DEFAULT '' COMMENT '加盐密码',
  `status` tinyint(4) NOT NULL COMMENT '状态',
  `is_deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '软删除，0是有效，1是删除',
  `ctime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `mtime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`account_id`),
  UNIQUE KEY `uk_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for auth_assignment
-- ----------------------------
DROP TABLE IF EXISTS `auth_assignment`;
CREATE TABLE `auth_assignment` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `item_id` int(11) NOT NULL COMMENT '权限或角色 auth_item ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `ctime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色（或权限）和用户的关联表';

-- ----------------------------
-- Table structure for auth_item
-- ----------------------------
DROP TABLE IF EXISTS `auth_item`;
CREATE TABLE `auth_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `name` varchar(16) NOT NULL COMMENT '权限/角色名称',
  `type` tinyint(4) NOT NULL COMMENT '区分权限和角色-0-角色 1-权限组 2-权限点',
  `data` varchar(32) NOT NULL DEFAULT '' COMMENT '内容',
  `description` varchar(32) NOT NULL DEFAULT '' COMMENT '描述',
  `is_deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '软删除，0是有效，1是删除',
  `ctime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `mtime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_auth_item` (`name`,`type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='储存权限和角色的表';

-- ----------------------------
-- Table structure for auth_item_child
-- ----------------------------
DROP TABLE IF EXISTS `auth_item_child`;
CREATE TABLE `auth_item_child` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `parent` int(11) NOT NULL COMMENT 'auth_item id',
  `child` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_parent_child` (`parent`,`child`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色和权限(或角色和角色)的关联表';

-- ----------------------------
-- Table structure for log_operation
-- ----------------------------
DROP TABLE IF EXISTS `log_operation`;
CREATE TABLE `log_operation` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `table_name` varchar(32) NOT NULL DEFAULT '' COMMENT '表名',
  `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 insert 1 delete 2 update 3 select',
  `operator_username` varchar(32) NOT NULL DEFAULT '' COMMENT '操作人id',
  `operator_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '操作人类型0广告主1运营',
  `value` text NOT NULL COMMENT '操作结果',
  `ctime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `mtime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '软删除，0是有效，1是删除',
  PRIMARY KEY (`id`),
  KEY `idx_ctime` (`ctime`) USING BTREE,
  KEY `idx_table_name_operator_id_ctime` (`table_name`(20),`operator_username`,`ctime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='操作日志表';
