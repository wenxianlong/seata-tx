/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : localhost:3306
 Source Schema         : seata

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 20/08/2023 21:58:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for branch_table
-- ----------------------------
DROP TABLE IF EXISTS `branch_table`;
CREATE TABLE `branch_table`  (
  `branch_id` bigint NOT NULL,
  `xid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `transaction_id` bigint NULL DEFAULT NULL,
  `resource_group_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `resource_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `branch_type` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` tinyint NULL DEFAULT NULL,
  `client_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `application_data` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `gmt_create` datetime(6) NULL DEFAULT NULL,
  `gmt_modified` datetime(6) NULL DEFAULT NULL,
  PRIMARY KEY (`branch_id`) USING BTREE,
  INDEX `idx_xid`(`xid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of branch_table
-- ----------------------------
INSERT INTO `branch_table` VALUES (1846902085603251796, '192.168.223.1:8091:1846902085603251792', 1846902085603251792, NULL, 'jdbc:mysql:///seata-stock', 'XA', 0, 'seata_tx:192.168.223.1:52749', NULL, '2023-08-20 10:29:59.221828', '2023-08-20 10:29:59.221828');
INSERT INTO `branch_table` VALUES (1846902085603251798, '192.168.223.1:8091:1846902085603251792', 1846902085603251792, NULL, 'jdbc:mysql:///seata-stock', 'XA', 0, 'seata_tx:192.168.223.1:52749', NULL, '2023-08-20 10:29:59.238044', '2023-08-20 10:29:59.238044');
INSERT INTO `branch_table` VALUES (1846902085603251853, '192.168.223.1:8091:1846902085603251851', 1846902085603251851, NULL, 'jdbc:mysql:///seata-stock', 'XA', 0, 'seata_tx:192.168.223.1:52749', NULL, '2023-08-20 10:30:25.623950', '2023-08-20 10:30:25.623950');
INSERT INTO `branch_table` VALUES (1846902085603252128, '192.168.223.1:8091:1846902085603252126', 1846902085603252126, NULL, 'jdbc:mysql:///seata-stock', 'XA', 0, 'seata_tx:192.168.223.1:53003', NULL, '2023-08-20 10:33:23.366474', '2023-08-20 10:33:23.366474');

-- ----------------------------
-- Table structure for distributed_lock
-- ----------------------------
DROP TABLE IF EXISTS `distributed_lock`;
CREATE TABLE `distributed_lock`  (
  `lock_key` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `lock_value` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `expire` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`lock_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of distributed_lock
-- ----------------------------
INSERT INTO `distributed_lock` VALUES ('AsyncCommitting', ' ', 0);
INSERT INTO `distributed_lock` VALUES ('RetryCommitting', ' ', 0);
INSERT INTO `distributed_lock` VALUES ('RetryRollbacking', ' ', 0);
INSERT INTO `distributed_lock` VALUES ('TxTimeoutCheck', ' ', 0);
INSERT INTO `distributed_lock` VALUES ('UndologDelete', ' ', 0);

-- ----------------------------
-- Table structure for global_table
-- ----------------------------
DROP TABLE IF EXISTS `global_table`;
CREATE TABLE `global_table`  (
  `xid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `transaction_id` bigint NULL DEFAULT NULL,
  `status` tinyint NOT NULL,
  `application_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `transaction_service_group` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `transaction_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `timeout` int NULL DEFAULT NULL,
  `begin_time` bigint NULL DEFAULT NULL,
  `application_data` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `gmt_create` datetime NULL DEFAULT NULL,
  `gmt_modified` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`xid`) USING BTREE,
  INDEX `idx_status_gmt_modified`(`status` ASC, `gmt_modified` ASC) USING BTREE,
  INDEX `idx_transaction_id`(`transaction_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of global_table
-- ----------------------------
INSERT INTO `global_table` VALUES ('192.168.223.1:8091:1846902085603251792', 1846902085603251792, 12, 'seata_tx', 'seata_tx_group', 'purchase(java.lang.String, java.lang.String, int, boolean)', 60000, 1692498599057, NULL, '2023-08-20 10:29:59', '2023-08-20 10:30:53');
INSERT INTO `global_table` VALUES ('192.168.223.1:8091:1846902085603251851', 1846902085603251851, 12, 'seata_tx', 'seata_tx_group', 'purchase(java.lang.String, java.lang.String, int, boolean)', 60000, 1692498625612, NULL, '2023-08-20 10:30:25', '2023-08-20 10:30:39');
INSERT INTO `global_table` VALUES ('192.168.223.1:8091:1846902085603252126', 1846902085603252126, 12, 'seata_tx', 'seata_tx_group', 'purchase(java.lang.String, java.lang.String, int, boolean)', 60000, 1692498803211, NULL, '2023-08-20 10:33:23', '2023-08-20 10:33:58');

-- ----------------------------
-- Table structure for lock_table
-- ----------------------------
DROP TABLE IF EXISTS `lock_table`;
CREATE TABLE `lock_table`  (
  `row_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `xid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `transaction_id` bigint NULL DEFAULT NULL,
  `branch_id` bigint NOT NULL,
  `resource_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `table_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `pk` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '0:locked ,1:rollbacking',
  `gmt_create` datetime NULL DEFAULT NULL,
  `gmt_modified` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`row_key`) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_branch_id`(`branch_id` ASC) USING BTREE,
  INDEX `idx_xid`(`xid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lock_table
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
