/*
 Navicat Premium Data Transfer

 Source Server         : mysql8.0.35
 Source Server Type    : MySQL
 Source Server Version : 80035 (8.0.35)
 Source Host           : localhost:5206
 Source Schema         : orientation

 Target Server Type    : MySQL
 Target Server Version : 80035 (8.0.35)
 File Encoding         : 65001

 Date: 24/01/2024 18:22:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for academy
-- ----------------------------
DROP TABLE IF EXISTS `academy`;
CREATE TABLE `academy`  (
  `id` bigint NOT NULL COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学院名称',
  `illustration` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学院描述',
  `student_count` int UNSIGNED NULL DEFAULT NULL COMMENT '学院学生数量',
  `priority` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '优先级(数字越小优先级越小)',
  `gmt_create` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime(6) NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除(0：未删除 1：已删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`priority` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of academy
-- ----------------------------
INSERT INTO `academy` VALUES (1, '计算机科学学院', '计算机科学学院', 10000, 10, '2023-12-31 13:40:28.000000', '2023-12-31 13:40:31.000000', '1', '1', 0);
INSERT INTO `academy` VALUES (69023664439297, '艺术学院', '一个充满艺术的学院', 300, 1, '2024-01-16 22:30:48.193000', '2024-01-16 22:30:48.193000', '1', '1', 0);

-- ----------------------------
-- Table structure for activity
-- ----------------------------
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity`  (
  `id` bigint NOT NULL COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '活动名称',
  `content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '活动内容',
  `start` datetime(6) NULL DEFAULT NULL COMMENT '活动开始时间',
  `end` datetime(6) NULL DEFAULT NULL COMMENT '活动结束时间',
  `points` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '积分数',
  `type` tinyint NULL DEFAULT NULL COMMENT '活动类型(0：长期活动 1：临时活动)',
  `score_standard` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计分标准',
  `state` tinyint NULL DEFAULT NULL COMMENT '活动状态(0：未开始 1：进行中 2：已结束)',
  `max_number` int UNSIGNED NULL DEFAULT NULL COMMENT '活动最大人数限制',
  `count` int UNSIGNED NULL DEFAULT 0 COMMENT '已参与活动人数',
  `gmt_create` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime(6) NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除(0：未删除 1：已删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of activity
-- ----------------------------
INSERT INTO `activity` VALUES (70425746866177, '这是一个活动', '这是一个活动', '2024-01-24 13:05:00.000000', '2024-01-29 00:00:00.000000', 5, 1, '一次活动加五分', 1, 100, 1, '2024-01-24 16:13:33.819000', '2024-01-24 16:40:00.878000', '1', '1', 0);

-- ----------------------------
-- Table structure for activity_log
-- ----------------------------
DROP TABLE IF EXISTS `activity_log`;
CREATE TABLE `activity_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `activity_id` bigint NULL DEFAULT NULL COMMENT '活动id',
  `uid` bigint NULL DEFAULT NULL COMMENT '参与活动用户id',
  `material` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提交材料',
  `finish_time` datetime(6) NULL DEFAULT NULL COMMENT '活动完成时间',
  `state` tinyint NULL DEFAULT NULL COMMENT '活动状态(0：未完成 1：已完成 2：进行中)',
  `gmt_create` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime(6) NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除(0：未删除 1：已删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of activity_log
-- ----------------------------
INSERT INTO `activity_log` VALUES (1, 70425746866177, 1, NULL, '2024-01-24 16:51:57.383000', 1, '2024-01-24 16:27:24.585000', '2024-01-24 16:51:57.384000', '1', '1', 0);
INSERT INTO `activity_log` VALUES (2, 70425746866177, 2, NULL, NULL, 0, '2024-01-24 16:38:15.105000', '2024-01-24 16:40:00.891000', '1', '1', 0);

-- ----------------------------
-- Table structure for advertisement
-- ----------------------------
DROP TABLE IF EXISTS `advertisement`;
CREATE TABLE `advertisement`  (
  `id` bigint NOT NULL COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '赞助商名称',
  `description` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '赞助商描述',
  `logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '赞助商logo',
  `link` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '赞助商友链',
  `priority` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '优先级(数字越小优先级越小)',
  `gmt_create` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime(6) NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除(0：未删除 1：已删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of advertisement
-- ----------------------------
INSERT INTO `advertisement` VALUES (68973043384321, '赞助商一', '赞助商一', '赞助商一', '赞助商一', 1, '2024-01-16 15:48:31.635000', '2024-01-16 15:48:31.635000', '1', '1', 0);
INSERT INTO `advertisement` VALUES (68973204865025, '赞助商二', '赞助商二', '赞助商二', '赞助商二', 1, '2024-01-16 15:49:47.764000', '2024-01-16 15:49:47.764000', '1', '1', 0);

-- ----------------------------
-- Table structure for exchange_log
-- ----------------------------
DROP TABLE IF EXISTS `exchange_log`;
CREATE TABLE `exchange_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `prize_id` bigint NULL DEFAULT NULL COMMENT '奖品id',
  `uid` bigint NULL DEFAULT NULL COMMENT '用户id',
  `gmt_create` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime(6) NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 68783490203650 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of exchange_log
-- ----------------------------
INSERT INTO `exchange_log` VALUES (68783490203649, NULL, 11, '2024-01-15 16:58:43.139000', '2024-01-15 16:58:43.139000', '1', '1', 0);

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `permission_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限名称',
  `key_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限关键词(权限认证使用此字段)',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由路径',
  `perms` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由组件',
  `gmt_create` datetime(6) NULL DEFAULT NULL,
  `gmt_modified` datetime(6) NULL DEFAULT NULL,
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `is_deleted` tinyint(1) NULL DEFAULT 0,
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父级权限id',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `key_name`(`key_name` ASC) USING BTREE,
  INDEX `father`(`parent_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 363 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES (1, 'root', '*', NULL, NULL, NULL, '2024-01-16 17:25:45.114000', '2024-01-16 17:25:45.114000', '1', '1', 0, 0, NULL);
INSERT INTO `permission` VALUES (362, 'admin', 'admin.*', NULL, NULL, NULL, '2024-01-17 09:33:27.501000', '2024-01-17 09:33:27.501000', '1', '1', 0, 1, NULL);

-- ----------------------------
-- Table structure for points_log
-- ----------------------------
DROP TABLE IF EXISTS `points_log`;
CREATE TABLE `points_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `uid` bigint NULL DEFAULT NULL COMMENT '用户id',
  `points` int UNSIGNED NULL DEFAULT NULL COMMENT '本次所加积分数',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '加分说明',
  `type` tinyint NULL DEFAULT NULL COMMENT '积分类型(0：任务积分 1：活动积分)',
  `gmt_create` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime(6) NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `is_deleted` tinyint(1) NULL DEFAULT NULL COMMENT '逻辑删除(0：未删除 1：已删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of points_log
-- ----------------------------
INSERT INTO `points_log` VALUES (1, 1, 5, '一次活动加五分', 1, '2024-01-24 16:38:30.289000', '2024-01-24 16:38:30.289000', '1', '1', NULL);
INSERT INTO `points_log` VALUES (2, 1, 5, '一次活动加五分', 1, '2024-01-24 16:51:57.405000', '2024-01-24 16:51:57.405000', '1', '1', NULL);
INSERT INTO `points_log` VALUES (3, 1, 10, '点击就送', 0, '2024-01-24 17:08:22.520000', '2024-01-24 17:08:22.520000', '1', '1', NULL);

-- ----------------------------
-- Table structure for prize
-- ----------------------------
DROP TABLE IF EXISTS `prize`;
CREATE TABLE `prize`  (
  `id` bigint NOT NULL COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '奖品名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '奖品描述',
  `price` int UNSIGNED NULL DEFAULT NULL COMMENT '奖品价格(所需积分数)',
  `inventory` int UNSIGNED NULL DEFAULT NULL COMMENT '奖品库存',
  `limited` int UNSIGNED NULL DEFAULT NULL COMMENT '限制兑换数量',
  `state` tinyint NULL DEFAULT 1 COMMENT '奖品状态(0：已售空 1：兑换中 2：已下架)',
  `gmt_create` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime(6) NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `is_deleted` tinyint(1) NULL DEFAULT NULL COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of prize
-- ----------------------------
INSERT INTO `prize` VALUES (68783404220417, '奖牌', '一个奖牌', 20, 100, 1, 1, '2024-01-15 14:41:23.865000', '2024-01-15 14:41:23.865000', '1', '1', 0);
INSERT INTO `prize` VALUES (68783490203649, '记事本', '一本全新的记事本', 25, 29, 1, 1, '2024-01-15 14:42:04.137000', '2024-01-15 16:58:43.154000', '1', '1', 0);
INSERT INTO `prize` VALUES (68800795901953, '记事本', '一本记事本', 15, 50, 1, 1, '2024-01-15 16:59:36.944000', '2024-01-15 16:59:36.945000', '1', '1', 0);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `role_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色关键词(权限认证字段)',
  `gmt_create` datetime(6) NULL DEFAULT NULL,
  `gmt_modified` datetime(6) NULL DEFAULT NULL,
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `is_deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, 'root', 'root', '2023-12-30 15:20:16.982000', '2023-12-30 15:20:16.982000', '1', '1', 0);
INSERT INTO `role` VALUES (2, 'admin', 'admin', '2024-01-17 09:32:43.205000', '2024-01-17 09:32:43.205000', '1', '1', 0);

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NOT NULL,
  `permission_id` bigint NOT NULL,
  `gmt_create` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime(6) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改者',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除',
  `deleted_id` bigint NULL DEFAULT NULL COMMENT '删除id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `per_role`(`permission_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 499 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES (497, 1, 1, '2024-01-16 21:54:33.284000', '2024-01-16 21:54:33.284000', '1', '1', 0, NULL);
INSERT INTO `role_permission` VALUES (498, 2, 362, '2024-01-17 09:48:12.652000', '2024-01-17 09:48:12.652000', '1', '1', 0, NULL);

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `id` bigint NOT NULL COMMENT 'id',
  `name` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '真实名字',
  `sex` tinyint(1) NULL DEFAULT NULL COMMENT '0女,1男',
  `identity_card` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `admission_letter` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '录取通知书',
  `contact` json NULL COMMENT '联系方式',
  `department_id` bigint NULL DEFAULT 1 COMMENT '学院',
  `points` int UNSIGNED NULL DEFAULT 0 COMMENT '学生积分数',
  `state` tinyint NULL DEFAULT NULL COMMENT '状态(0：未报道 1：已报道)',
  `uid` bigint NULL DEFAULT NULL COMMENT '用户id',
  `gmt_create` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime(6) NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_std`(`uid` ASC) USING BTREE COMMENT '根据uid快速获取到学生信息',
  CONSTRAINT `user_student` FOREIGN KEY (`uid`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES (1, 'admin', 1, '2141224232', '32352533', '{}', 1, 46, 1, 1, '2023-12-31 13:38:41.000000', '2024-01-24 17:08:22.533000', '1', '1', 0);
INSERT INTO `student` VALUES (68789884420097, 'xiaoming', 1, NULL, 'qwrqraDqrwffsfw', '{}', 1, 75, 1, 11, '2024-01-15 15:32:54.053000', '2024-01-15 16:58:43.110000', '1', '1', 0);

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task`  (
  `id` bigint NOT NULL COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务内容',
  `start` datetime(6) NULL DEFAULT NULL COMMENT '开始时间',
  `end` datetime(6) NULL DEFAULT NULL COMMENT '结束时间',
  `points` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '积分数',
  `score_standard` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计分标准(计分规则)',
  `state` tinyint NULL DEFAULT 0 COMMENT '任务状态(0：未开始 1：进行中 2：已结束)',
  `type` tinyint NULL DEFAULT NULL COMMENT '任务类型(0：主线任务 1：支线任务)',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父任务id(禁止为本任务id)',
  `gmt_create` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime(6) NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除(0：未删除 1：已删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of task
-- ----------------------------
INSERT INTO `task` VALUES (68933788893185, '任务一', '任务一', NULL, NULL, 10, '点击就送', 0, 0, NULL, '2024-01-16 10:36:33.025000', '2024-01-16 10:36:33.025000', '1', '1', 0);
INSERT INTO `task` VALUES (68933818253313, '任务二', '任务二', NULL, NULL, 5, '点击就送', 0, 0, NULL, '2024-01-16 10:36:46.683000', '2024-01-16 10:36:46.683000', '1', '1', 0);
INSERT INTO `task` VALUES (68933897945089, '任务三', '任务三', NULL, NULL, 5, '点击就送', 0, 0, 68933788893185, '2024-01-16 10:37:24.457000', '2024-01-16 10:37:24.457000', '1', '1', 0);

-- ----------------------------
-- Table structure for task_log
-- ----------------------------
DROP TABLE IF EXISTS `task_log`;
CREATE TABLE `task_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `task_id` bigint NULL DEFAULT NULL COMMENT '任务id',
  `uid` bigint NULL DEFAULT NULL COMMENT '用户id',
  `material` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提交的任务材料',
  `finish_time` datetime(6) NULL DEFAULT NULL COMMENT '任务完成时间',
  `state` tinyint NULL DEFAULT 0 COMMENT '任务状态(0：未完成 1：已完成 2：进行中)',
  `gmt_create` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime(6) NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除(0：未删除 1：已删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of task_log
-- ----------------------------
INSERT INTO `task_log` VALUES (1, 68933788893185, 1, NULL, '2024-01-24 17:08:22.056000', 1, '2024-01-24 17:06:04.151000', '2024-01-24 17:08:22.504000', '1', '1', 0);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱地址',
  `phone` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `type` tinyint NOT NULL COMMENT '用户类型',
  `state` tinyint NULL DEFAULT 1 COMMENT '用户状态(0：禁用 1：启用，默认为1)',
  `gmt_create` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime(6) NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除(0：未删除 1：已删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_name`(`user_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'root', '2e7aea220cf51ddd', '', 'https://fingerbed.oss-cn-chengdu.aliyuncs.com/CSDN/202311171434600.png', '', NULL, 0, 0, NULL, '2024-01-16 17:14:32.495000', NULL, '1', 0);
INSERT INTO `user` VALUES (4, '1111111', '1111111', '1111111', '1111111', '1111111', NULL, 1, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `user` VALUES (5, 'testest', '43c2499e41ae7a34', '', '', '', NULL, 1, 1, '2023-11-03 09:24:10.680000', '2023-12-24 15:17:28.623000', '1', '1', 0);
INSERT INTO `user` VALUES (6, 'teset', 'c55a7663a7cfc37f', 'teset', '', '', NULL, 0, 0, '2023-12-24 15:13:55.593000', '2023-12-24 15:15:01.456000', '1', '1', 1);
INSERT INTO `user` VALUES (7, 'test1', 'test1', NULL, NULL, NULL, NULL, 2, 0, '2023-12-28 14:40:21.889000', '2023-12-28 14:40:21.890000', '1', NULL, 0);
INSERT INTO `user` VALUES (10, 'test2211', 'test2211', NULL, NULL, NULL, NULL, 2, 0, '2023-12-29 20:41:39.948000', '2023-12-29 20:41:39.948000', '1', '1', 0);
INSERT INTO `user` VALUES (11, 'xiaoming', 'xiaoming', NULL, NULL, NULL, NULL, 2, 1, '2024-01-15 14:44:35.070000', '2024-01-15 14:44:35.070000', '1', '1', 0);
INSERT INTO `user` VALUES (12, 'admin', '2e7aea220cf51ddd', NULL, NULL, NULL, NULL, 1, 1, '2024-01-17 09:34:44.749000', '2024-01-17 09:34:44.749000', '1', '1', 0);
INSERT INTO `user` VALUES (13, 'xiaohong', '2e7aea220cf51ddd', NULL, NULL, NULL, NULL, 1, 1, '2024-01-17 09:45:17.645000', '2024-01-17 09:45:17.645000', '1', '1', 0);

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  `gmt_create` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime(6) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改者',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除',
  `deleted_id` bigint NULL DEFAULT NULL COMMENT '删除id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_user`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (1, 1, 1, '2024-01-16 21:42:38.476000', '2024-01-16 21:42:38.476000', '1', '1', 0, NULL);
INSERT INTO `user_role` VALUES (2, 11, 2, '2024-01-17 09:45:43.795000', '2024-01-17 09:45:43.795000', '1', '1', 0, NULL);

SET FOREIGN_KEY_CHECKS = 1;
