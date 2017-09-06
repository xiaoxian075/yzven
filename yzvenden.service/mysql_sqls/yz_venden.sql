/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50718
Source Host           : localhost:3306
Source Database       : yz_venden

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2017-09-06 14:51:58
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for ven_admin
-- ----------------------------
DROP TABLE IF EXISTS `ven_admin`;
CREATE TABLE `ven_admin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `login_name` varchar(64) NOT NULL COMMENT '登入名',
  `password` varchar(128) NOT NULL COMMENT '密码',
  `user_name` char(64) NOT NULL COMMENT '用户名',
  `state` int(11) NOT NULL COMMENT '状态 0:无效 1:有效',
  `role_id` int(11) NOT NULL COMMENT '角色',
  `create_time` bigint(20) NOT NULL COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ven_admin
-- ----------------------------
INSERT INTO `ven_admin` (`id`, `login_name`, `password`, `user_name`, `state`, `role_id`, `create_time`, `update_time`) VALUES ('1', 'admin', '123456', '小雪', '1', '0', '1504663930363', '1504663930363');

-- ----------------------------
-- Table structure for ven_menu
-- ----------------------------
DROP TABLE IF EXISTS `ven_menu`;
CREATE TABLE `ven_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `parent_id` int(11) NOT NULL COMMENT '父ID',
  `url` varchar(255) NOT NULL COMMENT '链接地址',
  `name` varchar(128) NOT NULL COMMENT '名字',
  `level` int(11) NOT NULL COMMENT '等级',
  `show` int(11) NOT NULL COMMENT '是否显示 0:不显示  1:显示',
  `power_list` blob NOT NULL COMMENT '权限集',
  `sort` int(11) NOT NULL COMMENT '排序',
  `create_time` bigint(20) NOT NULL COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `url` (`url`),
  UNIQUE KEY `name` (`name`),
  UNIQUE KEY `sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ven_menu
-- ----------------------------

-- ----------------------------
-- Table structure for ven_role
-- ----------------------------
DROP TABLE IF EXISTS `ven_role`;
CREATE TABLE `ven_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `name` varchar(64) NOT NULL COMMENT '名称',
  `menu_list` blob NOT NULL COMMENT '菜单集 JSON格式储存',
  `state` int(11) NOT NULL COMMENT '状态 0:无效 1:有效',
  `create_time` bigint(20) NOT NULL COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ven_role
-- ----------------------------
