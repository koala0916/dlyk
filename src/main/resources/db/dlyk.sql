/*
 Navicat Premium Dump SQL

 Source Server         : koala
 Source Server Type    : MySQL
 Source Server Version : 80041 (8.0.41)
 Source Host           : localhost:3306
 Source Schema         : dlyk

 Target Server Type    : MySQL
 Target Server Version : 80041 (8.0.41)
 File Encoding         : 65001

 Date: 08/09/2026 17:40:42
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_activity
-- ----------------------------
DROP TABLE IF EXISTS `t_activity`;
CREATE TABLE `t_activity`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键，自动增长，活动ID',
  `owner_id` int NULL DEFAULT NULL COMMENT '活动所属人ID',
  `name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '活动名称',
  `start_time` datetime NULL DEFAULT NULL COMMENT '活动开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '活动结束时间',
  `cost` decimal(11, 2) NULL DEFAULT NULL COMMENT '活动预算',
  `description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '活动描述',
  `create_time` datetime NULL DEFAULT NULL COMMENT '活动创建时间',
  `create_by` int NULL DEFAULT NULL COMMENT '活动创建人',
  `edit_time` datetime NULL DEFAULT NULL COMMENT '活动编辑时间',
  `edit_by` int NULL DEFAULT NULL COMMENT '活动编辑人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `owner`(`owner_id` ASC) USING BTREE,
  INDEX `create_by`(`create_by` ASC) USING BTREE,
  INDEX `edit_by`(`edit_by` ASC) USING BTREE,
  CONSTRAINT `t_activity_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `t_user` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `t_activity_ibfk_2` FOREIGN KEY (`create_by`) REFERENCES `t_user` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `t_activity_ibfk_3` FOREIGN KEY (`edit_by`) REFERENCES `t_user` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 56 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '市场活动表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_activity
-- ----------------------------
INSERT INTO `t_activity` VALUES (1, 4, '百度推广', '2023-08-05 19:18:20', '2023-08-25 00:00:00', 1200000.00, '百度排名推广,活动非常非常贵,大家好好的做推广.由于推广活动的效果非常好,决定延期一个月.山东科技付货款打扫房间凯撒的补给卡时间跨度风寒咳嗽大部分就开吧打撒个发过火发给你厉害u7如果发生大叔大婶i我饿火绒额特容易骗人空调已汇入退役军人天涯茫然了啊浴室柜打不打卡是i速单号覅收到反馈上课的话费尽口舌大富科技是', '2024-06-25 10:33:33', 8, '2023-05-15 00:10:49', 12);
INSERT INTO `t_activity` VALUES (2, 2, '充话费-送手机', '2023-10-28 17:48:49', '2023-10-30 17:48:54', 9000.00, '充话费,送手机,充满送Iphone14', '2023-03-28 17:49:28', 1, '2023-05-15 00:10:52', 1);
INSERT INTO `t_activity` VALUES (7, 1, '抖音推广', '2023-12-01 12:03:09', '2023-12-30 12:03:09', 15800.00, '抖音直播推广活动1', NULL, 6, NULL, NULL);
INSERT INTO `t_activity` VALUES (8, 1, '抖音推广', '2023-01-01 12:03:09', '2023-04-30 12:03:09', 15800.00, '抖音直播推广活动2', NULL, 7, '2023-04-28 14:28:53', NULL);
INSERT INTO `t_activity` VALUES (10, 2, '11', '2023-01-11 00:30:58', '2023-04-30 00:31:00', 131.00, '12312312', NULL, NULL, '2023-04-28 13:33:56', NULL);
INSERT INTO `t_activity` VALUES (11, 3, '1213', '2023-02-01 00:00:00', '2023-04-30 00:31:13', 123123.00, '23123123', NULL, NULL, NULL, NULL);
INSERT INTO `t_activity` VALUES (12, 1, '抖音推广', '2023-12-01 12:03:09', '2023-12-11 12:03:09', 15800.00, '抖音直播推广活动1', NULL, NULL, NULL, NULL);
INSERT INTO `t_activity` VALUES (13, 1, '抖音推广', '2023-01-01 12:03:09', '2023-04-01 12:03:09', 15800.00, '抖音直播推广活动2', NULL, NULL, '2023-04-28 13:27:43', NULL);
INSERT INTO `t_activity` VALUES (14, 1, '抖音推广', '2023-11-01 12:03:09', '2023-12-01 12:03:09', 15800.00, '抖音直播推广活动1', NULL, NULL, NULL, NULL);
INSERT INTO `t_activity` VALUES (15, 1, '抖音推广', '2023-03-01 12:03:09', '2023-04-01 12:03:09', 15800.00, '抖音直播推广活动11111', NULL, NULL, '2023-04-26 17:09:49', NULL);
INSERT INTO `t_activity` VALUES (16, 1, '抖音推广', '2023-03-01 12:03:09', '2023-04-01 12:03:09', 15800.00, '抖音直播推广活动1', NULL, NULL, NULL, NULL);
INSERT INTO `t_activity` VALUES (17, 1, '抖音推广', '2023-03-01 12:03:09', '2023-04-01 12:03:09', 15800.00, '抖音直播推广活动2', NULL, NULL, NULL, NULL);
INSERT INTO `t_activity` VALUES (18, 1, '抖音推广', '2023-04-01 12:03:09', '2023-04-01 12:03:09', 15800.00, '抖音直播推广活动1', NULL, NULL, NULL, NULL);
INSERT INTO `t_activity` VALUES (19, 1, '抖音推广', '2023-09-01 12:03:09', '2023-09-01 12:03:09', 15800.00, '抖音直播推广活动2', NULL, NULL, NULL, NULL);
INSERT INTO `t_activity` VALUES (20, 1, '抖音推广', '2023-09-01 12:03:09', '2023-09-01 12:03:09', 15800.00, '抖音直播推广活动1', NULL, NULL, NULL, NULL);
INSERT INTO `t_activity` VALUES (21, 1, '抖音推广', '2023-04-01 12:03:09', '2023-04-01 12:03:09', 15800.00, '抖音直播推广活动2', NULL, NULL, NULL, NULL);
INSERT INTO `t_activity` VALUES (22, 1, '抖音推广', '2024-02-01 10:03:09', '2024-09-09 12:03:09', 15800.00, '抖音直播推广活动1', NULL, NULL, NULL, NULL);
INSERT INTO `t_activity` VALUES (23, 1, '抖音推广', '2024-02-01 10:03:09', '2024-09-11 12:03:09', 15800.00, '抖音直播推广活动2', NULL, NULL, NULL, NULL);
INSERT INTO `t_activity` VALUES (24, 1, '抖音推广', '2024-01-07 10:03:09', '2024-09-27 12:03:09', 15800.00, '抖音直播推广活动1', NULL, NULL, NULL, NULL);
INSERT INTO `t_activity` VALUES (25, 1, '抖音推广', '2024-02-01 12:03:09', '2024-09-01 12:03:09', 15800.00, '抖音直播推广活动2', NULL, NULL, NULL, NULL);
INSERT INTO `t_activity` VALUES (26, 1, '抖音推广', '2024-02-01 12:03:09', '2024-09-01 12:03:09', 15800.00, '抖音直播推广活动1', NULL, NULL, NULL, NULL);
INSERT INTO `t_activity` VALUES (27, 1, '抖音推广', '2024-02-01 12:03:09', '2024-09-01 12:03:09', 15800.00, '抖音直播推广活动2', NULL, NULL, NULL, NULL);
INSERT INTO `t_activity` VALUES (28, 1, '抖音推广', '2024-02-01 12:03:09', '2024-09-01 12:03:09', 15800.00, '抖音直播推广活动1', NULL, NULL, NULL, NULL);
INSERT INTO `t_activity` VALUES (29, 1, '抖音推广', '2024-02-01 12:03:09', '2024-09-01 12:03:09', 15800.00, '抖音直播推广活动2', NULL, NULL, NULL, NULL);
INSERT INTO `t_activity` VALUES (40, 1, '抖音推广11', '2024-05-01 12:03:09', '2024-09-01 12:03:09', 15800.00, '抖音直播推广活动1', NULL, NULL, NULL, NULL);
INSERT INTO `t_activity` VALUES (41, 1, '抖音推广11', '2024-05-01 12:03:09', '2024-09-01 12:03:09', 15800.00, '抖音直播推广活动2', NULL, NULL, NULL, NULL);
INSERT INTO `t_activity` VALUES (42, 1, '抖音推广11', '2024-05-01 12:03:09', '2024-09-01 12:03:09', 15800.00, '抖音直播推广活动1', NULL, NULL, NULL, NULL);
INSERT INTO `t_activity` VALUES (43, 1, '抖音推广11', '2024-05-01 12:03:09', '2024-09-01 12:03:09', 15800.00, '抖音直播推广活动2', NULL, NULL, NULL, NULL);
INSERT INTO `t_activity` VALUES (44, 1, '抖音推广11', '2024-06-01 12:03:09', '2024-09-01 12:03:09', 15800.00, '抖音直播推广活动1', NULL, NULL, NULL, NULL);
INSERT INTO `t_activity` VALUES (45, 1, '抖音推广11', '2024-06-01 12:03:09', '2024-09-15 12:03:09', 15800.00, '抖音直播推广活动2', NULL, NULL, NULL, NULL);
INSERT INTO `t_activity` VALUES (46, 3, '抖音短视频广告', '2024-07-05 00:00:00', '2024-11-30 00:00:00', 5000.00, '抖音短视频广告，宣传产品', NULL, NULL, NULL, NULL);
INSERT INTO `t_activity` VALUES (47, 3, '我去恶趣味', '2024-07-15 16:51:40', '2024-11-30 16:51:42', 231231.00, '色达所大所大所多', '2024-06-25 09:19:14', NULL, '2024-06-25 16:59:31', 1);
INSERT INTO `t_activity` VALUES (48, 7, '可视角度焚枯食淡粉红色', '2024-11-01 00:44:19', '2024-12-30 00:44:19', 185323.00, '第三方水电费水电费水电费是非得失', '2024-06-25 22:27:15', NULL, '2024-06-25 17:00:02', 1);
INSERT INTO `t_activity` VALUES (49, 1, '抖音推广11', '2024-12-01 12:03:09', '2024-12-12 12:03:09', 15800.00, '抖音直播推广活动1', '2024-06-25 09:19:23', NULL, NULL, NULL);
INSERT INTO `t_activity` VALUES (50, 2, '手动滑稽腹背受敌', '2024-06-25 16:58:52', '2024-06-29 00:00:00', 1280000.00, '第三方水电费水电费', '2024-06-25 16:12:00', 1, '2024-06-25 16:58:55', 1);

-- ----------------------------
-- Table structure for t_activity_remark
-- ----------------------------
DROP TABLE IF EXISTS `t_activity_remark`;
CREATE TABLE `t_activity_remark`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键，自动增长，活动备注ID',
  `activity_id` int NULL DEFAULT NULL COMMENT '活动ID',
  `note_content` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注内容',
  `create_time` datetime NULL DEFAULT NULL COMMENT '备注创建时间',
  `create_by` int NULL DEFAULT NULL COMMENT '备注创建人',
  `edit_time` datetime NULL DEFAULT NULL COMMENT '备注编辑时间',
  `edit_by` int NULL DEFAULT NULL COMMENT '备注编辑人',
  `deleted` int NULL DEFAULT NULL COMMENT '删除状态（0或者null正常，1删除）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `activity_id`(`activity_id` ASC) USING BTREE,
  INDEX `t_activity_remark_ibfk_2`(`create_by` ASC) USING BTREE,
  INDEX `t_activity_remark_ibfk_3`(`edit_by` ASC) USING BTREE,
  CONSTRAINT `t_activity_remark_ibfk_1` FOREIGN KEY (`activity_id`) REFERENCES `t_activity` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `t_activity_remark_ibfk_2` FOREIGN KEY (`create_by`) REFERENCES `t_user` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `t_activity_remark_ibfk_3` FOREIGN KEY (`edit_by`) REFERENCES `t_user` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '市场活动备注表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_activity_remark
-- ----------------------------
INSERT INTO `t_activity_remark` VALUES (1, 46, '1111111111111', '2023-05-17 14:07:48', 1, NULL, NULL, NULL);
INSERT INTO `t_activity_remark` VALUES (2, 1, '市场活动效果需要进一步改进。', '2023-05-17 14:25:30', 1, '2024-06-25 14:44:01', 1, 1);
INSERT INTO `t_activity_remark` VALUES (3, 2, '1231231', '2023-06-07 23:08:32', 1, NULL, NULL, NULL);
INSERT INTO `t_activity_remark` VALUES (4, 2, '23123', '2023-06-07 23:08:57', 1, NULL, NULL, NULL);
INSERT INTO `t_activity_remark` VALUES (5, 10, '恶趣味群二无群二群无', '2023-06-07 23:10:20', 1, NULL, NULL, NULL);
INSERT INTO `t_activity_remark` VALUES (6, 1, '上帝粉红色的匡扶汉室登记卡放哪的桑拿房科技大收纳发卡机打撒', '2023-08-04 22:30:47', 1, '2024-06-25 14:43:51', 1, NULL);
INSERT INTO `t_activity_remark` VALUES (7, 1, '啥地方跟你说快递费牛会计三大步伐年卡大撒把饭卡大撒把开发受打击开发你受打击考拉放哪受打击可燃放哪凯撒', '2024-06-25 11:11:00', 1, '2024-06-25 14:44:53', 1, NULL);
INSERT INTO `t_activity_remark` VALUES (8, 1, '首付款是的呢防溺水迪菲娜手打 ', '2024-06-25 11:11:30', 1, '2024-06-25 14:44:49', 1, NULL);
INSERT INTO `t_activity_remark` VALUES (9, 1, '活动效果还可以，每天可以拿到2000个真实的意向客户的手机号，但是后续电话联系的时候，大部分电话不接听，联系不上。', '2024-06-25 11:13:30', 1, NULL, NULL, NULL);
INSERT INTO `t_activity_remark` VALUES (10, 1, '附件看过的发给你了地方你噶漏打卡个', '2024-06-25 11:40:11', 1, NULL, NULL, NULL);
INSERT INTO `t_activity_remark` VALUES (11, 1, '隧道粉红色款到发货', '2024-06-25 12:01:09', 1, NULL, NULL, NULL);
INSERT INTO `t_activity_remark` VALUES (12, 1, '冻死佛山分你收到了菲尼萨打卡漏发了双打卡发撒。', '2024-06-25 12:01:21', 1, '2024-06-25 14:44:14', 1, NULL);
INSERT INTO `t_activity_remark` VALUES (13, 1, '咨询费重振雄风产生的', '2024-06-25 12:01:39', 1, NULL, NULL, NULL);
INSERT INTO `t_activity_remark` VALUES (14, 1, '水电费水电费是的', '2024-06-25 12:01:41', 1, NULL, NULL, NULL);
INSERT INTO `t_activity_remark` VALUES (15, 1, '水电费水电费是的', '2024-06-25 12:01:43', 1, NULL, NULL, NULL);
INSERT INTO `t_activity_remark` VALUES (16, 1, '水电费水电费', '2024-06-25 12:01:44', 1, '2024-06-25 14:44:25', 1, 1);
INSERT INTO `t_activity_remark` VALUES (17, 1, '水电费水电费是的', '2024-06-25 12:01:46', 1, NULL, NULL, NULL);
INSERT INTO `t_activity_remark` VALUES (18, 1, '收到放虎归山东方红郡双打卡饭卡打撒', '2024-06-25 12:12:07', 1, NULL, NULL, NULL);
INSERT INTO `t_activity_remark` VALUES (19, 1, 'USD会阿迪斯', '2024-06-25 14:45:37', 1, NULL, NULL, 1);
INSERT INTO `t_activity_remark` VALUES (20, 1, '瓦尔特瑞特热二位大范甘迪公对公', '2024-06-25 15:01:27', 1, '2024-06-25 15:01:35', 1, 1);
INSERT INTO `t_activity_remark` VALUES (21, 48, '二哥让他人', '2024-06-25 16:59:50', 1, NULL, NULL, NULL);
INSERT INTO `t_activity_remark` VALUES (22, 48, '如图和液体与东风股份大概', '2024-06-25 16:59:54', 1, '2024-06-25 17:00:08', 1, 1);

-- ----------------------------
-- Table structure for t_clue
-- ----------------------------
DROP TABLE IF EXISTS `t_clue`;
CREATE TABLE `t_clue`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `age` int NULL DEFAULT NULL COMMENT '年龄',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '电话',
  `intention_course` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '意向课程',
  `intention_strength` int NULL DEFAULT NULL COMMENT '意向强度1-10',
  `source` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '来源',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '备注',
  `trial_class_time` datetime NULL DEFAULT NULL COMMENT '体验课时间',
  `clue_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '未联系' COMMENT '未联系/已联系/已转客户',
  `create_by` int NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 116 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '线索表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_clue
-- ----------------------------
INSERT INTO `t_clue` VALUES (7, 'Emma', 22, '13910001001', '少儿武术基础班', 8, '抖音团购', NULL, '2026-05-22 22:04:25', '未联系', 2, '2026-03-19 22:04:25');
INSERT INTO `t_clue` VALUES (8, 'Olivia', 16, '13910001002', '散打实战班', 6, '转介绍', NULL, '2026-05-24 22:04:25', '已联系', 3, '2026-03-30 22:04:25');
INSERT INTO `t_clue` VALUES (9, 'Ava', 12, '13910001003', '武术套路提高班', 9, '门店咨询', NULL, '2026-05-09 22:04:25', '已转客户', 4, '2026-04-09 22:04:25');
INSERT INTO `t_clue` VALUES (10, 'Sophia', 18, '13910001004', '武术器械班', 7, '线上活动', NULL, '2026-05-14 22:04:25', '已转客户', 5, '2026-02-18 22:04:25');
INSERT INTO `t_clue` VALUES (11, 'Isabella', 14, '13910001005', '少儿武术基础班', 5, '地推', NULL, '2026-05-26 22:04:25', '未联系', 6, '2026-04-29 22:04:25');
INSERT INTO `t_clue` VALUES (12, 'Mia', 20, '13910001006', '散打实战班', 8, '抖音团购', NULL, '2026-05-04 22:04:25', '已联系', 7, '2026-03-20 22:04:25');
INSERT INTO `t_clue` VALUES (15, 'Han', 21, '13910001009', '散打实战班', 7, '抖音团购', NULL, '2026-05-07 22:04:25', '已转客户', 14, '2026-04-04 22:04:25');
INSERT INTO `t_clue` VALUES (18, 'Elsa', 13, '13910001012', '武术套路提高班', 8, '线上活动', NULL, '2026-04-29 22:04:25', '已联系', 4, '2026-04-24 22:04:25');
INSERT INTO `t_clue` VALUES (19, '平儿', 33, '13545556789', '成人剑术', 5, '抖音团购', '想上私教，昨天来咨询过', NULL, '已转客户', 1, '2026-05-21 19:27:25');
INSERT INTO `t_clue` VALUES (20, '入画', 8, '1390202000', '少儿武术基础班', 7, '抖音团购', '通灵宝玉引来咨询，意向少儿武术健体班', NULL, '未联系', 2, '2026-05-25 17:02:49');
INSERT INTO `t_clue` VALUES (21, '抱琴', 9, '1390202001', '散打实战班', 8, '转介绍', '葬花节后到馆体验，关注形体与气质课程', NULL, '未联系', 2, '2026-05-25 17:02:49');
INSERT INTO `t_clue` VALUES (22, '莺儿', 10, '1390202002', '武术套路提高班', 9, '门店咨询', '海棠诗社成员转介绍，想学武术套路', NULL, '未联系', 2, '2026-05-25 17:02:50');
INSERT INTO `t_clue` VALUES (23, '麝月', 11, '1390202003', '武术器械班', 10, '线上活动', '协理宁国府时路过武馆，问企业团课', NULL, '未联系', 2, '2026-05-25 17:02:50');
INSERT INTO `t_clue` VALUES (24, '秋纹', 12, '1390202004', '少儿武术基础班', 5, '地推', '史太君寿宴后想给孙辈报班', NULL, '未联系', 2, '2026-05-25 17:02:50');
INSERT INTO `t_clue` VALUES (25, '碧痕', 13, '1390202005', '散打实战班', 6, '抖音团购', '严父望子成龙，咨询散打实战班', NULL, '未联系', 2, '2026-05-25 17:02:50');
INSERT INTO `t_clue` VALUES (26, '小红', 14, '1390202006', '武术套路提高班', 7, '转介绍', '管家派来打听，府里丫鬟可组班', NULL, '未联系', 2, '2026-05-25 17:02:50');
INSERT INTO `t_clue` VALUES (27, '坠儿', 15, '1390202007', '武术器械班', 8, '门店咨询', '大观园诗会结识教练，问器械班', NULL, '未联系', 2, '2026-05-25 17:02:50');
INSERT INTO `t_clue` VALUES (28, '娇杏', 16, '1390202008', '少儿武术基础班', 9, '线上活动', '才自精明志自高，想报提高班', NULL, '未联系', 2, '2026-05-25 17:02:50');
INSERT INTO `t_clue` VALUES (29, '贾宝玉', 17, '1390202009', '散打实战班', 10, '地推', '勘破三春景不长，试课散打入门', NULL, '未联系', 2, '2026-05-25 17:02:50');
INSERT INTO `t_clue` VALUES (30, '林黛玉', 18, '1390202010', '武术套路提高班', 5, '抖音团购', '通灵宝玉引来咨询，意向少儿武术健体班', NULL, '已联系', 2, '2026-05-25 17:02:50');
INSERT INTO `t_clue` VALUES (31, '薛宝钗', 19, '1390202011', '武术器械班', 6, '转介绍', '葬花节后到馆体验，关注形体与气质课程', NULL, '已联系', 2, '2026-05-25 17:02:51');
INSERT INTO `t_clue` VALUES (32, '王熙凤', 20, '1390202012', '少儿武术基础班', 7, '门店咨询', '海棠诗社成员转介绍，想学武术套路', NULL, '已联系', 2, '2026-05-25 17:02:51');
INSERT INTO `t_clue` VALUES (33, '贾母', 21, '1390202013', '散打实战班', 8, '线上活动', '协理宁国府时路过武馆，问企业团课', NULL, '已联系', 2, '2026-05-25 17:02:51');
INSERT INTO `t_clue` VALUES (34, '贾政', 22, '1390202014', '武术套路提高班', 9, '地推', '史太君寿宴后想给孙辈报班', NULL, '已联系', 2, '2026-05-25 17:02:51');
INSERT INTO `t_clue` VALUES (35, '王夫人', 23, '1390202015', '武术器械班', 10, '抖音团购', '严父望子成龙，咨询散打实战班', NULL, '已联系', 2, '2026-05-25 17:02:51');
INSERT INTO `t_clue` VALUES (36, '邢夫人', 24, '1390202016', '少儿武术基础班', 5, '转介绍', '管家派来打听，府里丫鬟可组班', NULL, '已联系', 2, '2026-05-25 17:02:51');
INSERT INTO `t_clue` VALUES (37, '探春', 25, '1390202017', '散打实战班', 6, '门店咨询', '大观园诗会结识教练，问器械班', NULL, '已联系', 2, '2026-05-25 17:02:52');
INSERT INTO `t_clue` VALUES (38, '惜春', 26, '1390202018', '武术套路提高班', 7, '线上活动', '才自精明志自高，想报提高班', NULL, '已转客户', 2, '2026-05-25 17:02:52');
INSERT INTO `t_clue` VALUES (39, '迎春', 27, '1390202019', '武术器械班', 8, '地推', '勘破三春景不长，试课散打入门', NULL, '已转客户', 2, '2026-05-25 17:02:54');
INSERT INTO `t_clue` VALUES (40, '元春', 28, '1390202020', '少儿武术基础班', 9, '抖音团购', '通灵宝玉引来咨询，意向少儿武术健体班', NULL, '已转客户', 2, '2026-05-25 17:02:54');
INSERT INTO `t_clue` VALUES (41, '史湘云', 29, '1390202021', '散打实战班', 10, '转介绍', '葬花节后到馆体验，关注形体与气质课程', NULL, '已转客户', 2, '2026-05-25 17:02:55');
INSERT INTO `t_clue` VALUES (42, '妙玉', 30, '1390202022', '武术套路提高班', 5, '门店咨询', '海棠诗社成员转介绍，想学武术套路', NULL, '已转客户', 2, '2026-05-25 17:02:55');
INSERT INTO `t_clue` VALUES (43, '邢夫人', 8, '1390303000', '少儿武术基础班', 5, '抖音团购', '通灵宝玉引来咨询，意向少儿武术健体班', NULL, '未联系', 3, '2026-05-25 17:02:57');
INSERT INTO `t_clue` VALUES (44, '探春', 9, '1390303001', '散打实战班', 6, '转介绍', '葬花节后到馆体验，关注形体与气质课程', NULL, '未联系', 3, '2026-05-25 17:02:57');
INSERT INTO `t_clue` VALUES (45, '惜春', 10, '1390303002', '武术套路提高班', 7, '门店咨询', '海棠诗社成员转介绍，想学武术套路', NULL, '未联系', 3, '2026-05-25 17:02:57');
INSERT INTO `t_clue` VALUES (46, '迎春', 11, '1390303003', '武术器械班', 8, '线上活动', '协理宁国府时路过武馆，问企业团课', NULL, '未联系', 3, '2026-05-25 17:02:57');
INSERT INTO `t_clue` VALUES (47, '元春', 12, '1390303004', '少儿武术基础班', 9, '地推', '史太君寿宴后想给孙辈报班', NULL, '未联系', 3, '2026-05-25 17:02:57');
INSERT INTO `t_clue` VALUES (48, '史湘云', 13, '1390303005', '散打实战班', 10, '抖音团购', '严父望子成龙，咨询散打实战班', NULL, '未联系', 3, '2026-05-25 17:02:57');
INSERT INTO `t_clue` VALUES (49, '妙玉', 14, '1390303006', '武术套路提高班', 5, '转介绍', '管家派来打听，府里丫鬟可组班', NULL, '未联系', 3, '2026-05-25 17:02:57');
INSERT INTO `t_clue` VALUES (50, '秦可卿', 15, '1390303007', '武术器械班', 6, '门店咨询', '大观园诗会结识教练，问器械班', NULL, '未联系', 3, '2026-05-25 17:02:57');
INSERT INTO `t_clue` VALUES (51, '袭人', 16, '1390303008', '少儿武术基础班', 7, '线上活动', '才自精明志自高，想报提高班', NULL, '未联系', 3, '2026-05-25 17:02:57');
INSERT INTO `t_clue` VALUES (52, '晴雯', 17, '1390303009', '散打实战班', 8, '地推', '勘破三春景不长，试课散打入门', NULL, '未联系', 3, '2026-05-25 17:02:58');
INSERT INTO `t_clue` VALUES (53, '平儿', 18, '1390303010', '武术套路提高班', 9, '抖音团购', '通灵宝玉引来咨询，意向少儿武术健体班', NULL, '已联系', 3, '2026-05-25 17:02:58');
INSERT INTO `t_clue` VALUES (54, '香菱', 19, '1390303011', '武术器械班', 10, '转介绍', '葬花节后到馆体验，关注形体与气质课程', NULL, '已联系', 3, '2026-05-25 17:02:58');
INSERT INTO `t_clue` VALUES (55, '紫鹃', 20, '1390303012', '少儿武术基础班', 5, '门店咨询', '海棠诗社成员转介绍，想学武术套路', NULL, '已联系', 3, '2026-05-25 17:02:58');
INSERT INTO `t_clue` VALUES (56, '鸳鸯', 21, '1390303013', '散打实战班', 6, '线上活动', '协理宁国府时路过武馆，问企业团课', NULL, '已联系', 3, '2026-05-25 17:02:58');
INSERT INTO `t_clue` VALUES (57, '贾琏', 22, '1390303014', '武术套路提高班', 7, '地推', '史太君寿宴后想给孙辈报班', NULL, '已联系', 3, '2026-05-25 17:02:58');
INSERT INTO `t_clue` VALUES (58, '尤二姐', 23, '1390303015', '武术器械班', 8, '抖音团购', '严父望子成龙，咨询散打实战班', NULL, '已联系', 3, '2026-05-25 17:02:58');
INSERT INTO `t_clue` VALUES (59, '尤三姐', 24, '1390303016', '少儿武术基础班', 9, '转介绍', '管家派来打听，府里丫鬟可组班', NULL, '已联系', 3, '2026-05-25 17:02:58');
INSERT INTO `t_clue` VALUES (60, '薛蟠', 25, '1390303017', '散打实战班', 10, '门店咨询', '大观园诗会结识教练，问器械班', NULL, '已联系', 3, '2026-05-25 17:02:59');
INSERT INTO `t_clue` VALUES (61, '柳湘莲', 26, '1390303018', '武术套路提高班', 5, '线上活动', '才自精明志自高，想报提高班', NULL, '已转客户', 3, '2026-05-25 17:02:59');
INSERT INTO `t_clue` VALUES (62, '甄士隐', 27, '1390303019', '武术器械班', 6, '地推', '勘破三春景不长，试课散打入门', NULL, '已转客户', 3, '2026-05-25 17:02:59');
INSERT INTO `t_clue` VALUES (63, '刘姥姥', 28, '1390303020', '少儿武术基础班', 7, '抖音团购', '通灵宝玉引来咨询，意向少儿武术健体班', NULL, '已转客户', 3, '2026-05-25 17:02:59');
INSERT INTO `t_clue` VALUES (64, '马道婆', 29, '1390303021', '散打实战班', 8, '转介绍', '葬花节后到馆体验，关注形体与气质课程', NULL, '已转客户', 3, '2026-05-25 17:03:00');
INSERT INTO `t_clue` VALUES (65, '蒋玉菡', 30, '1390303022', '武术套路提高班', 9, '门店咨询', '海棠诗社成员转介绍，想学武术套路', NULL, '已转客户', 3, '2026-05-25 17:03:00');
INSERT INTO `t_clue` VALUES (66, '坠儿', 8, '1390505000', '少儿武术基础班', 7, '抖音团购', '通灵宝玉引来咨询，意向少儿武术健体班', NULL, '未联系', 5, '2026-05-25 17:03:01');
INSERT INTO `t_clue` VALUES (67, '娇杏', 9, '1390505001', '散打实战班', 8, '转介绍', '葬花节后到馆体验，关注形体与气质课程', NULL, '未联系', 5, '2026-05-25 17:03:01');
INSERT INTO `t_clue` VALUES (68, '贾宝玉', 10, '1390505002', '武术套路提高班', 9, '门店咨询', '海棠诗社成员转介绍，想学武术套路', NULL, '未联系', 5, '2026-05-25 17:03:01');
INSERT INTO `t_clue` VALUES (69, '林黛玉', 11, '1390505003', '武术器械班', 10, '线上活动', '协理宁国府时路过武馆，问企业团课', NULL, '未联系', 5, '2026-05-25 17:03:02');
INSERT INTO `t_clue` VALUES (70, '薛宝钗', 12, '1390505004', '少儿武术基础班', 5, '地推', '史太君寿宴后想给孙辈报班', NULL, '未联系', 5, '2026-05-25 17:03:02');
INSERT INTO `t_clue` VALUES (71, '王熙凤', 13, '1390505005', '散打实战班', 6, '抖音团购', '严父望子成龙，咨询散打实战班', NULL, '未联系', 5, '2026-05-25 17:03:02');
INSERT INTO `t_clue` VALUES (72, '贾母', 14, '1390505006', '武术套路提高班', 7, '转介绍', '管家派来打听，府里丫鬟可组班', NULL, '未联系', 5, '2026-05-25 17:03:02');
INSERT INTO `t_clue` VALUES (73, '贾政', 15, '1390505007', '武术器械班', 8, '门店咨询', '大观园诗会结识教练，问器械班', NULL, '未联系', 5, '2026-05-25 17:03:02');
INSERT INTO `t_clue` VALUES (74, '王夫人', 16, '1390505008', '少儿武术基础班', 9, '线上活动', '才自精明志自高，想报提高班', NULL, '未联系', 5, '2026-05-25 17:03:02');
INSERT INTO `t_clue` VALUES (75, '邢夫人', 17, '1390505009', '散打实战班', 10, '地推', '勘破三春景不长，试课散打入门', NULL, '已转客户', 5, '2026-05-25 17:03:02');
INSERT INTO `t_clue` VALUES (76, '探春', 18, '1390505010', '武术套路提高班', 5, '抖音团购', '通灵宝玉引来咨询，意向少儿武术健体班', NULL, '已联系', 5, '2026-05-25 17:03:02');
INSERT INTO `t_clue` VALUES (77, '惜春', 19, '1390505011', '武术器械班', 6, '转介绍', '葬花节后到馆体验，关注形体与气质课程', NULL, '已联系', 5, '2026-05-25 17:03:02');
INSERT INTO `t_clue` VALUES (78, '迎春', 20, '1390505012', '少儿武术基础班', 7, '门店咨询', '海棠诗社成员转介绍，想学武术套路', NULL, '已联系', 5, '2026-05-25 17:03:02');
INSERT INTO `t_clue` VALUES (79, '元春', 21, '1390505013', '散打实战班', 8, '线上活动', '协理宁国府时路过武馆，问企业团课', NULL, '已联系', 5, '2026-05-25 17:03:03');
INSERT INTO `t_clue` VALUES (80, '史湘云', 22, '1390505014', '武术套路提高班', 9, '地推', '史太君寿宴后想给孙辈报班', NULL, '已联系', 5, '2026-05-25 17:03:03');
INSERT INTO `t_clue` VALUES (81, '妙玉', 23, '1390505015', '武术器械班', 10, '抖音团购', '严父望子成龙，咨询散打实战班', NULL, '已联系', 5, '2026-05-25 17:03:03');
INSERT INTO `t_clue` VALUES (82, '秦可卿', 24, '1390505016', '少儿武术基础班', 5, '转介绍', '管家派来打听，府里丫鬟可组班', NULL, '已联系', 5, '2026-05-25 17:03:03');
INSERT INTO `t_clue` VALUES (83, '袭人', 25, '1390505017', '散打实战班', 6, '门店咨询', '大观园诗会结识教练，问器械班', NULL, '已联系', 5, '2026-05-25 17:03:03');
INSERT INTO `t_clue` VALUES (84, '晴雯', 26, '1390505018', '武术套路提高班', 7, '线上活动', '才自精明志自高，想报提高班', NULL, '已转客户', 5, '2026-05-25 17:03:03');
INSERT INTO `t_clue` VALUES (85, '平儿', 27, '1390505019', '武术器械班', 8, '地推', '勘破三春景不长，试课散打入门', NULL, '已转客户', 5, '2026-05-25 17:03:03');
INSERT INTO `t_clue` VALUES (86, '香菱', 28, '1390505020', '少儿武术基础班', 9, '抖音团购', '通灵宝玉引来咨询，意向少儿武术健体班', NULL, '已转客户', 5, '2026-05-25 17:03:04');
INSERT INTO `t_clue` VALUES (87, '紫鹃', 29, '1390505021', '散打实战班', 10, '转介绍', '葬花节后到馆体验，关注形体与气质课程', NULL, '已转客户', 5, '2026-05-25 17:03:04');
INSERT INTO `t_clue` VALUES (88, '鸳鸯', 30, '1390505022', '武术套路提高班', 5, '门店咨询', '海棠诗社成员转介绍，想学武术套路', NULL, '已转客户', 5, '2026-05-25 17:03:04');
INSERT INTO `t_clue` VALUES (89, '秦可卿', 8, '1390606000', '少儿武术基础班', 5, '抖音团购', '通灵宝玉引来咨询，意向少儿武术健体班', NULL, '未联系', 6, '2026-05-25 17:03:06');
INSERT INTO `t_clue` VALUES (90, '袭人', 9, '1390606001', '散打实战班', 6, '转介绍', '葬花节后到馆体验，关注形体与气质课程', NULL, '未联系', 6, '2026-05-25 17:03:06');
INSERT INTO `t_clue` VALUES (91, '晴雯', 10, '1390606002', '武术套路提高班', 7, '门店咨询', '海棠诗社成员转介绍，想学武术套路', NULL, '未联系', 6, '2026-05-25 17:03:06');
INSERT INTO `t_clue` VALUES (92, '平儿', 11, '1390606003', '武术器械班', 8, '线上活动', '协理宁国府时路过武馆，问企业团课', NULL, '未联系', 6, '2026-05-25 17:03:06');
INSERT INTO `t_clue` VALUES (93, '香菱', 12, '1390606004', '少儿武术基础班', 9, '地推', '史太君寿宴后想给孙辈报班', NULL, '未联系', 6, '2026-05-25 17:03:06');
INSERT INTO `t_clue` VALUES (94, '紫鹃', 13, '1390606005', '散打实战班', 10, '抖音团购', '严父望子成龙，咨询散打实战班', NULL, '未联系', 6, '2026-05-25 17:03:06');
INSERT INTO `t_clue` VALUES (95, '鸳鸯', 14, '1390606006', '武术套路提高班', 5, '转介绍', '管家派来打听，府里丫鬟可组班', NULL, '未联系', 6, '2026-05-25 17:03:06');
INSERT INTO `t_clue` VALUES (96, '贾琏', 15, '1390606007', '武术器械班', 6, '门店咨询', '大观园诗会结识教练，问器械班', NULL, '未联系', 6, '2026-05-25 17:03:06');
INSERT INTO `t_clue` VALUES (97, '尤二姐', 16, '1390606008', '少儿武术基础班', 7, '线上活动', '才自精明志自高，想报提高班', NULL, '未联系', 6, '2026-05-25 17:03:06');
INSERT INTO `t_clue` VALUES (98, '尤三姐', 17, '1390606009', '散打实战班', 8, '地推', '勘破三春景不长，试课散打入门', NULL, '未联系', 6, '2026-05-25 17:03:06');
INSERT INTO `t_clue` VALUES (99, '薛蟠', 18, '1390606010', '武术套路提高班', 9, '抖音团购', '通灵宝玉引来咨询，意向少儿武术健体班', NULL, '已联系', 6, '2026-05-25 17:03:07');
INSERT INTO `t_clue` VALUES (100, '柳湘莲', 19, '1390606011', '武术器械班', 10, '转介绍', '葬花节后到馆体验，关注形体与气质课程', NULL, '已联系', 6, '2026-05-25 17:03:07');
INSERT INTO `t_clue` VALUES (101, '甄士隐', 20, '1390606012', '少儿武术基础班', 5, '门店咨询', '海棠诗社成员转介绍，想学武术套路', NULL, '已联系', 6, '2026-05-25 17:03:07');
INSERT INTO `t_clue` VALUES (102, '刘姥姥', 21, '1390606013', '散打实战班', 6, '线上活动', '协理宁国府时路过武馆，问企业团课', NULL, '已联系', 6, '2026-05-25 17:03:07');
INSERT INTO `t_clue` VALUES (103, '马道婆', 22, '1390606014', '武术套路提高班', 7, '地推', '史太君寿宴后想给孙辈报班', NULL, '已联系', 6, '2026-05-25 17:03:07');
INSERT INTO `t_clue` VALUES (104, '蒋玉菡', 23, '1390606015', '武术器械班', 8, '抖音团购', '严父望子成龙，咨询散打实战班', NULL, '已联系', 6, '2026-05-25 17:03:07');
INSERT INTO `t_clue` VALUES (105, '金钏', 24, '1390606016', '少儿武术基础班', 9, '转介绍', '管家派来打听，府里丫鬟可组班', NULL, '已联系', 6, '2026-05-25 17:03:07');
INSERT INTO `t_clue` VALUES (106, '司棋', 25, '13906060177', '散打实战班', 10, '门店咨询', '大观园诗会结识教练，问器械班', '2026-05-28 16:00:00', '已联系', 6, '2026-05-25 17:03:07');
INSERT INTO `t_clue` VALUES (107, '入画', 26, '1390606018', '武术套路提高班', 5, '线上活动', '才自精明志自高，想报提高班', NULL, '已转客户', 6, '2026-05-25 17:03:07');
INSERT INTO `t_clue` VALUES (108, '抱琴', 27, '1390606019', '武术器械班', 6, '地推', '勘破三春景不长，试课散打入门', NULL, '已转客户', 6, '2026-05-25 17:03:08');
INSERT INTO `t_clue` VALUES (109, '莺儿', 28, '1390606020', '少儿武术基础班', 7, '抖音团购', '通灵宝玉引来咨询，意向少儿武术健体班', NULL, '已转客户', 6, '2026-05-25 17:03:08');
INSERT INTO `t_clue` VALUES (110, '麝月', 29, '1390606021', '散打实战班', 8, '转介绍', '葬花节后到馆体验，关注形体与气质课程', NULL, '已转客户', 6, '2026-05-25 17:03:08');
INSERT INTO `t_clue` VALUES (111, '秋纹', 30, '1390606022', '武术套路提高班', 9, '门店咨询', '海棠诗社成员转介绍，想学武术套路', NULL, '已转客户', 6, '2026-05-25 17:03:09');
INSERT INTO `t_clue` VALUES (112, '金钏', 8, '1390707000', '少儿武术基础班', 9, '抖音团购', '通灵宝玉引来咨询，意向少儿武术健体班', NULL, '未联系', 7, '2026-05-25 17:03:10');
INSERT INTO `t_clue` VALUES (113, '小明', 9, '13907070011', '散打实战班', 10, '转介绍', '葬花节后到馆体验，关注形体与气质课程。\r\n妈妈打电话说希望女老师上课', '2026-05-29 18:00:00', '已联系', 7, '2026-05-25 17:03:10');
INSERT INTO `t_clue` VALUES (114, '李华', 8, '12345655456', '启蒙班', 8, '地推', NULL, NULL, '未联系', 50, '2026-05-27 17:00:07');
INSERT INTO `t_clue` VALUES (115, '张三', 5, '12344455555', '体适能武术启蒙', 5, '转介绍', NULL, NULL, '已联系', 50, '2026-05-27 17:01:19');

-- ----------------------------
-- Table structure for t_clue_edit_log
-- ----------------------------
DROP TABLE IF EXISTS `t_clue_edit_log`;
CREATE TABLE `t_clue_edit_log`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `clue_id` int NOT NULL,
  `edit_by` int NULL DEFAULT NULL,
  `edit_time` datetime NOT NULL,
  `change_content` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_clue_id`(`clue_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '线索编辑日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_clue_edit_log
-- ----------------------------
INSERT INTO `t_clue_edit_log` VALUES (1, 19, 1, '2026-05-21 19:28:52', '备注：由「想上私教」改为「想上私教，昨天来咨询过」');
INSERT INTO `t_clue_edit_log` VALUES (2, 113, 1, '2026-05-27 14:27:24', '电话：由「1390707001」改为「13907070011」；线索状态：由「未联系」改为「已联系」');
INSERT INTO `t_clue_edit_log` VALUES (3, 113, 1, '2026-05-27 14:28:01', '备注：由「葬花节后到馆体验，关注形体与气质课程」改为「葬花节后到馆体验，关注形体与气质课程。\r\n妈妈打电话说希望女老师上课」');
INSERT INTO `t_clue_edit_log` VALUES (4, 113, 1, '2026-05-27 14:38:01', '体验课时间：由「」改为「2026-05-29 00:00:00」');
INSERT INTO `t_clue_edit_log` VALUES (5, 106, 1, '2026-05-27 14:38:14', '电话：由「1390606017」改为「13906060177」；体验课时间：由「」改为「2026-05-28 00:00:00」');
INSERT INTO `t_clue_edit_log` VALUES (6, 106, 1, '2026-05-27 14:38:41', '体验课时间：由「2026-05-28 00:00:00」改为「2026-05-28 16:00:00」');
INSERT INTO `t_clue_edit_log` VALUES (7, 113, 1, '2026-05-27 14:38:52', '体验课时间：由「2026-05-29 00:00:00」改为「2026-05-29 18:00:00」');
INSERT INTO `t_clue_edit_log` VALUES (8, 113, 1, '2026-05-27 14:39:09', '姓名：由「司棋」改为「小明」');

-- ----------------------------
-- Table structure for t_customer
-- ----------------------------
DROP TABLE IF EXISTS `t_customer`;
CREATE TABLE `t_customer`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '电话',
  `age` int NULL DEFAULT NULL COMMENT '年龄',
  `course_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '课程类型',
  `remaining_lessons` int NULL DEFAULT NULL COMMENT '剩余课时',
  `course_expire_time` datetime NULL DEFAULT NULL COMMENT '课程到期时间',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '备注',
  `create_by` int NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `source` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '来源',
  `studying` tinyint NOT NULL DEFAULT 1 COMMENT '是否正在学习 0否 1是',
  `clue_id` int NULL DEFAULT NULL COMMENT '预留：线索转客户',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 42 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '客户(学员)表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_customer
-- ----------------------------
INSERT INTO `t_customer` VALUES (7, 'Sophia', '13910001004', 18, '武术器械班', 20, '2026-09-16 22:04:25', NULL, 5, '2026-02-23 22:04:25', '线上活动', 1, 4);
INSERT INTO `t_customer` VALUES (8, '王熙凤', '13910001007', 19, '武术套路提高班', 16, '2026-08-17 22:04:25', NULL, 8, '2026-02-13 22:04:25', '转介绍', 1, 7);
INSERT INTO `t_customer` VALUES (9, 'Mickey', '13910001011', 10, '少儿武术基础班', 24, '2026-11-15 22:04:25', NULL, 3, '2026-03-05 22:04:25', '转介绍', 1, 11);
INSERT INTO `t_customer` VALUES (10, 'Anna', '13910002001', 11, '少儿武术基础班', 12, '2026-07-18 22:04:25', NULL, 2, '2025-10-31 22:04:25', '抖音团购', 1, NULL);
INSERT INTO `t_customer` VALUES (12, 'Cinderella', '13910002003', 22, '武术器械班', 0, '2026-05-09 22:04:25', '已结课', 4, '2025-07-23 22:04:25', '门店咨询', 0, NULL);
INSERT INTO `t_customer` VALUES (13, 'Ariel', '13910002004', 14, '武术套路提高班', 2, '2026-06-18 22:04:25', NULL, 5, '2025-12-20 22:04:25', '线上活动', 1, NULL);
INSERT INTO `t_customer` VALUES (14, 'Mulan', '13910002005', 15, '少儿武术基础班', 18, '2026-08-27 22:04:25', NULL, 6, '2026-01-19 22:04:25', '地推', 1, NULL);
INSERT INTO `t_customer` VALUES (15, '贾宝玉', '13910002006', 20, '散打实战班', 10, '2026-08-07 22:04:25', NULL, 8, '2026-02-08 22:04:25', '抖音团购', 1, NULL);
INSERT INTO `t_customer` VALUES (16, '林黛玉', '13910002007', 17, '武术套路提高班', 14, '2026-07-28 22:04:25', NULL, 12, '2026-02-18 22:04:25', '转介绍', 1, NULL);
INSERT INTO `t_customer` VALUES (17, 'Ava', '13910001003', 12, '武术套路提高班', 100, NULL, NULL, 4, '2026-05-21 18:55:48', '门店咨询', 1, 9);
INSERT INTO `t_customer` VALUES (18, 'Han', '13910001009', 21, '散打实战班', NULL, '2027-05-13 00:00:00', NULL, 14, '2026-05-23 17:08:19', '抖音团购', 1, 15);
INSERT INTO `t_customer` VALUES (19, '惜春', '1390202018', 26, '武术套路提高班', 24, '2026-11-21 17:02:52', '转客户-才自精明志自高，想报提高班', 2, '2026-05-25 17:02:53', '线上活动', 1, 38);
INSERT INTO `t_customer` VALUES (20, '迎春', '1390202019', 27, '武术器械班', 24, '2026-11-21 17:02:53', '转客户-勘破三春景不长，试课散打入门', 2, '2026-05-25 17:02:54', '地推', 1, 39);
INSERT INTO `t_customer` VALUES (21, '元春', '1390202020', 28, '少儿武术基础班', 24, '2026-11-21 17:02:54', '转客户-通灵宝玉引来咨询，意向少儿武术健体班', 2, '2026-05-25 17:02:54', '抖音团购', 1, 40);
INSERT INTO `t_customer` VALUES (22, '史湘云', '1390202021', 29, '散打实战班', 24, '2026-11-21 17:02:54', '转客户-葬花节后到馆体验，关注形体与气质课程', 2, '2026-05-25 17:02:55', '转介绍', 1, 41);
INSERT INTO `t_customer` VALUES (23, '妙玉', '1390202022', 30, '武术套路提高班', 24, '2026-11-21 17:02:55', '转客户-海棠诗社成员转介绍，想学武术套路', 2, '2026-05-25 17:02:55', '门店咨询', 1, 42);
INSERT INTO `t_customer` VALUES (24, '柳湘莲', '1390303018', 26, '武术套路提高班', 24, '2026-11-21 17:02:58', '转客户-才自精明志自高，想报提高班', 3, '2026-05-25 17:02:59', '线上活动', 1, 61);
INSERT INTO `t_customer` VALUES (25, '甄士隐', '1390303019', 27, '武术器械班', 24, '2026-11-21 17:02:59', '转客户-勘破三春景不长，试课散打入门', 3, '2026-05-25 17:02:59', '地推', 1, 62);
INSERT INTO `t_customer` VALUES (26, '刘姥姥', '1390303020', 28, '少儿武术基础班', 24, '2026-11-21 17:02:59', '转客户-通灵宝玉引来咨询，意向少儿武术健体班', 3, '2026-05-25 17:02:59', '抖音团购', 1, 63);
INSERT INTO `t_customer` VALUES (27, '马道婆', '1390303021', 29, '散打实战班', 24, '2026-11-21 17:02:59', '转客户-葬花节后到馆体验，关注形体与气质课程', 3, '2026-05-25 17:03:00', '转介绍', 1, 64);
INSERT INTO `t_customer` VALUES (28, '蒋玉菡', '1390303022', 30, '武术套路提高班', 24, '2026-11-21 17:03:00', '转客户-海棠诗社成员转介绍，想学武术套路', 3, '2026-05-25 17:03:00', '门店咨询', 1, 65);
INSERT INTO `t_customer` VALUES (29, '晴雯', '1390505018', 26, '武术套路提高班', 24, '2026-11-21 17:03:03', '转客户-才自精明志自高，想报提高班', 5, '2026-05-25 17:03:03', '线上活动', 1, 84);
INSERT INTO `t_customer` VALUES (30, '平儿', '1390505019', 27, '武术器械班', 24, '2026-11-21 17:03:03', '转客户-勘破三春景不长，试课散打入门', 5, '2026-05-25 17:03:04', '地推', 1, 85);
INSERT INTO `t_customer` VALUES (31, '香菱', '1390505020', 28, '少儿武术基础班', 24, '2026-11-21 17:03:03', '转客户-通灵宝玉引来咨询，意向少儿武术健体班', 5, '2026-05-25 17:03:04', '抖音团购', 1, 86);
INSERT INTO `t_customer` VALUES (32, '紫鹃', '1390505021', 29, '散打实战班', 24, '2026-11-21 17:03:04', '转客户-葬花节后到馆体验，关注形体与气质课程', 5, '2026-05-25 17:03:04', '转介绍', 1, 87);
INSERT INTO `t_customer` VALUES (33, '鸳鸯', '1390505022', 30, '武术套路提高班', 24, '2026-11-21 17:03:04', '转客户-海棠诗社成员转介绍，想学武术套路', 5, '2026-05-25 17:03:05', '门店咨询', 1, 88);
INSERT INTO `t_customer` VALUES (34, '入画', '1390606018', 26, '武术套路提高班', 24, '2026-11-21 17:03:07', '转客户-才自精明志自高，想报提高班', 6, '2026-05-25 17:03:08', '线上活动', 1, 107);
INSERT INTO `t_customer` VALUES (35, '抱琴', '1390606019', 27, '武术器械班', 24, '2026-11-21 17:03:07', '转客户-勘破三春景不长，试课散打入门', 6, '2026-05-25 17:03:08', '地推', 1, 108);
INSERT INTO `t_customer` VALUES (36, '莺儿', '1390606020', 28, '少儿武术基础班', 24, '2026-11-21 17:03:08', '转客户-通灵宝玉引来咨询，意向少儿武术健体班', 6, '2026-05-25 17:03:08', '抖音团购', 1, 109);
INSERT INTO `t_customer` VALUES (37, '麝月', '1390606021', 29, '散打实战班', 24, '2026-11-21 17:03:08', '转客户-葬花节后到馆体验，关注形体与气质课程', 6, '2026-05-25 17:03:09', '转介绍', 1, 110);
INSERT INTO `t_customer` VALUES (38, '秋纹', '1390606022', 30, '武术套路提高班', 24, '2026-11-21 17:03:08', '转客户-海棠诗社成员转介绍，想学武术套路', 6, '2026-05-25 17:03:09', '门店咨询', 1, 111);
INSERT INTO `t_customer` VALUES (39, '邢夫人', '1390505009', 17, '散打实战班', 100, NULL, '勘破三春景不长，试课散打入门', 5, '2026-06-17 21:22:04', '地推', 1, 75);
INSERT INTO `t_customer` VALUES (40, 'Oleg', '13910001010', 25, '武术器械班', 100, NULL, NULL, 15, '2026-06-17 21:22:46', '地推', 1, 16);
INSERT INTO `t_customer` VALUES (41, '平儿', '13545556789', 33, '成人剑术', 20, NULL, '想上私教，昨天来咨询过', 1, '2026-06-17 21:23:04', '抖音团购', 1, 19);

-- ----------------------------
-- Table structure for t_customer_edit_log
-- ----------------------------
DROP TABLE IF EXISTS `t_customer_edit_log`;
CREATE TABLE `t_customer_edit_log`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int NOT NULL COMMENT '客户ID',
  `edit_by` int NULL DEFAULT NULL COMMENT '编辑人ID',
  `edit_time` datetime NOT NULL COMMENT '编辑时间',
  `change_content` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '修改内容说明',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_customer_id`(`customer_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '客户编辑日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_customer_edit_log
-- ----------------------------
INSERT INTO `t_customer_edit_log` VALUES (1, 13, 1, '2026-05-22 00:29:14', '剩余课时：由「6」改为「2」');

-- ----------------------------
-- Table structure for t_dic_type
-- ----------------------------
DROP TABLE IF EXISTS `t_dic_type`;
CREATE TABLE `t_dic_type`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键，自动增长，字典类型ID',
  `type_code` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '字典类型代码',
  `type_name` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '字典类型名称',
  `remark` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `code`(`type_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '字典类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_dic_type
-- ----------------------------
INSERT INTO `t_dic_type` VALUES (1, 'sex', '性别', NULL);
INSERT INTO `t_dic_type` VALUES (2, 'appellation', '称呼', NULL);
INSERT INTO `t_dic_type` VALUES (3, 'clueState', '线索状态', NULL);
INSERT INTO `t_dic_type` VALUES (4, 'returnPriority', '回访优先级', NULL);
INSERT INTO `t_dic_type` VALUES (5, 'returnState', '回访状态', NULL);
INSERT INTO `t_dic_type` VALUES (6, 'source', '来源', NULL);
INSERT INTO `t_dic_type` VALUES (7, 'stage', '阶段', NULL);
INSERT INTO `t_dic_type` VALUES (8, 'transactionType', '交易类型', NULL);
INSERT INTO `t_dic_type` VALUES (9, 'intentionState', '意向状态', NULL);
INSERT INTO `t_dic_type` VALUES (10, 'needLoan', '是否贷款', NULL);
INSERT INTO `t_dic_type` VALUES (11, 'educational', '学历', NULL);
INSERT INTO `t_dic_type` VALUES (12, 'userState', '用户状态', NULL);
INSERT INTO `t_dic_type` VALUES (13, 'noteWay', '跟踪方式', NULL);

-- ----------------------------
-- Table structure for t_dic_value
-- ----------------------------
DROP TABLE IF EXISTS `t_dic_value`;
CREATE TABLE `t_dic_value`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键，自动增长，字典值ID',
  `type_code` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '字典类型代码',
  `type_value` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '字典值',
  `order` int NULL DEFAULT NULL COMMENT '字典值排序',
  `remark` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `t_dic_value_ibfk_1`(`type_code` ASC) USING BTREE,
  CONSTRAINT `t_dic_value_ibfk_1` FOREIGN KEY (`type_code`) REFERENCES `t_dic_type` (`type_code`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 66 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '字典值表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_dic_value
-- ----------------------------
INSERT INTO `t_dic_value` VALUES (-1, 'clueState', '已转客户', 0, NULL);
INSERT INTO `t_dic_value` VALUES (1, 'clueState', '虚假线索', 4, NULL);
INSERT INTO `t_dic_value` VALUES (2, 'source', '知乎', 8, NULL);
INSERT INTO `t_dic_value` VALUES (3, 'source', '车展会', 11, NULL);
INSERT INTO `t_dic_value` VALUES (4, 'returnPriority', '最高', 2, NULL);
INSERT INTO `t_dic_value` VALUES (5, 'appellation', '教授', 5, NULL);
INSERT INTO `t_dic_value` VALUES (6, 'clueState', '将来联系', 2, NULL);
INSERT INTO `t_dic_value` VALUES (7, 'clueState', '丢失线索', 5, NULL);
INSERT INTO `t_dic_value` VALUES (8, 'returnState', '未启动', 1, NULL);
INSERT INTO `t_dic_value` VALUES (10, 'clueState', '试图联系', 1, NULL);
INSERT INTO `t_dic_value` VALUES (11, 'appellation', '博士', 4, NULL);
INSERT INTO `t_dic_value` VALUES (12, 'stage', '01创建交易', 1, NULL);
INSERT INTO `t_dic_value` VALUES (14, 'source', '汽车之家', 14, NULL);
INSERT INTO `t_dic_value` VALUES (15, 'returnPriority', '低', 3, NULL);
INSERT INTO `t_dic_value` VALUES (16, 'source', '网络广告', 1, NULL);
INSERT INTO `t_dic_value` VALUES (17, 'source', '视频直播', 9, NULL);
INSERT INTO `t_dic_value` VALUES (18, 'appellation', '先生', 1, NULL);
INSERT INTO `t_dic_value` VALUES (19, 'returnPriority', '高', 1, NULL);
INSERT INTO `t_dic_value` VALUES (20, 'appellation', '夫人', 2, NULL);
INSERT INTO `t_dic_value` VALUES (21, 'stage', '06丢失关闭', 7, NULL);
INSERT INTO `t_dic_value` VALUES (22, 'source', '地图', 13, NULL);
INSERT INTO `t_dic_value` VALUES (23, 'source', '合作伙伴', 6, NULL);
INSERT INTO `t_dic_value` VALUES (24, 'clueState', '未联系', 6, NULL);
INSERT INTO `t_dic_value` VALUES (25, 'source', '朋友圈', 10, NULL);
INSERT INTO `t_dic_value` VALUES (26, 'returnState', '进行中', 3, NULL);
INSERT INTO `t_dic_value` VALUES (27, 'clueState', '已联系', 3, NULL);
INSERT INTO `t_dic_value` VALUES (28, 'returnState', '推迟', 2, NULL);
INSERT INTO `t_dic_value` VALUES (29, 'returnState', '完成', 4, NULL);
INSERT INTO `t_dic_value` VALUES (30, 'clueState', '需要条件', 7, NULL);
INSERT INTO `t_dic_value` VALUES (32, 'returnState', '等待某人', 5, NULL);
INSERT INTO `t_dic_value` VALUES (33, 'source', '懂车帝', 2, NULL);
INSERT INTO `t_dic_value` VALUES (34, 'returnPriority', '常规', 5, NULL);
INSERT INTO `t_dic_value` VALUES (35, 'stage', '04产品检验', 5, NULL);
INSERT INTO `t_dic_value` VALUES (36, 'source', '易车网', 12, NULL);
INSERT INTO `t_dic_value` VALUES (37, 'stage', '02确认清单', 3, NULL);
INSERT INTO `t_dic_value` VALUES (38, 'returnPriority', '最低', 4, NULL);
INSERT INTO `t_dic_value` VALUES (39, 'source', '员工介绍', 3, NULL);
INSERT INTO `t_dic_value` VALUES (40, 'stage', '03交付定金', 4, NULL);
INSERT INTO `t_dic_value` VALUES (41, 'appellation', '女士', 3, NULL);
INSERT INTO `t_dic_value` VALUES (42, 'stage', '05付款成交', 6, NULL);
INSERT INTO `t_dic_value` VALUES (43, 'source', '官方网站', 5, NULL);
INSERT INTO `t_dic_value` VALUES (44, 'source', '公众号', 7, NULL);
INSERT INTO `t_dic_value` VALUES (45, 'source', '门店参观', 4, NULL);
INSERT INTO `t_dic_value` VALUES (46, 'intentionState', '有意向', 1, NULL);
INSERT INTO `t_dic_value` VALUES (47, 'intentionState', '无意向', 2, NULL);
INSERT INTO `t_dic_value` VALUES (48, 'intentionState', '意向不明', 3, NULL);
INSERT INTO `t_dic_value` VALUES (49, 'needLoan', '需要', 1, NULL);
INSERT INTO `t_dic_value` VALUES (50, 'needLoan', '不需要', 2, NULL);
INSERT INTO `t_dic_value` VALUES (51, 'sex', '男', 1, NULL);
INSERT INTO `t_dic_value` VALUES (52, 'sex', '女', 2, NULL);
INSERT INTO `t_dic_value` VALUES (53, 'educational', '小学', 1, NULL);
INSERT INTO `t_dic_value` VALUES (54, 'educational', '初中', 2, NULL);
INSERT INTO `t_dic_value` VALUES (55, 'educational', '高中', 3, NULL);
INSERT INTO `t_dic_value` VALUES (56, 'educational', '大学', 4, NULL);
INSERT INTO `t_dic_value` VALUES (57, 'educational', '研究生', 5, NULL);
INSERT INTO `t_dic_value` VALUES (58, 'userState', '正常', 1, NULL);
INSERT INTO `t_dic_value` VALUES (59, 'userState', '锁定', 2, NULL);
INSERT INTO `t_dic_value` VALUES (60, 'userState', '禁用', 3, NULL);
INSERT INTO `t_dic_value` VALUES (61, 'noteWay', '电话', 1, NULL);
INSERT INTO `t_dic_value` VALUES (62, 'noteWay', '微信', 2, NULL);
INSERT INTO `t_dic_value` VALUES (63, 'noteWay', 'QQ', 3, NULL);
INSERT INTO `t_dic_value` VALUES (64, 'noteWay', '面聊', 4, NULL);
INSERT INTO `t_dic_value` VALUES (65, 'noteWay', '其他', 5, NULL);

-- ----------------------------
-- Table structure for t_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE `t_permission`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `url` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `parent_id` int NULL DEFAULT NULL,
  `order_no` int NULL DEFAULT NULL,
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `component` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单对应要渲染的Vue组件名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1116 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_permission
-- ----------------------------
INSERT INTO `t_permission` VALUES (1, '市场活动', NULL, NULL, 'menu', 0, 41, 'OfficeBuilding', NULL);
INSERT INTO `t_permission` VALUES (2, '市场活动', NULL, '/dashboard/activity', 'menu', 1, 42, 'CreditCard', 'ActivityView');
INSERT INTO `t_permission` VALUES (3, '市场活动-列表', 'activity:list', NULL, 'button', 2, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (4, '市场活动-录入', 'activity:add', NULL, 'button', 2, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (5, '市场活动-编辑', 'activity:edit', NULL, 'button', 2, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (6, '市场活动-查看', 'activity:view', NULL, 'button', 2, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (7, '市场活动-删除', 'activity:delete', NULL, 'button', 2, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (9, '市场活动-搜索', 'activity:search', NULL, 'button', 2, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (10, '线索管理', NULL, NULL, 'menu', 0, 21, 'Magnet', NULL);
INSERT INTO `t_permission` VALUES (12, '线索管理', NULL, '/dashboard/clue', 'menu', 10, 22, 'Paperclip', 'ClueView');
INSERT INTO `t_permission` VALUES (13, '线索管理-列表', 'clue:list', NULL, 'button', 12, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (14, '线索管理-录入', 'clue:add', NULL, 'button', 12, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (15, '线索管理-编辑', 'clue:edit', NULL, 'button', 12, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (16, '线索管理-查看', 'clue:view', NULL, 'button', 12, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (17, '线索管理-删除', 'clue:delete', NULL, 'button', 12, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (18, '线索管理-导入', 'clue:import', NULL, 'button', 12, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (19, '客户管理', NULL, NULL, 'menu', 0, 11, 'User', NULL);
INSERT INTO `t_permission` VALUES (20, '客户管理', NULL, '/dashboard/customer', 'menu', 19, 12, 'UserFilled', 'CustomerView');
INSERT INTO `t_permission` VALUES (21, '客户管理-列表', 'customer:list', NULL, 'button', 20, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (22, '客户管理-查看', 'customer:view', NULL, 'button', 20, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (23, '客户管理-导出', 'customer:export', NULL, 'button', 20, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (24, '交易管理', NULL, NULL, 'menu', 0, 31, 'Wallet', NULL);
INSERT INTO `t_permission` VALUES (25, '交易管理', NULL, '/dashboard/tran', 'menu', 24, 32, 'Coin', 'TranView');
INSERT INTO `t_permission` VALUES (26, '交易管理-列表', 'tran:list', NULL, 'button', 25, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (27, '交易管理-查看', 'tran:view', NULL, 'button', 25, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (28, '产品管理', NULL, NULL, 'menu', 0, NULL, 'Memo', NULL);
INSERT INTO `t_permission` VALUES (29, '产品管理', NULL, '/dashboard/product', 'menu', 28, NULL, 'SetUp', 'ProductView');
INSERT INTO `t_permission` VALUES (30, '产品管理-列表', 'product:list', NULL, 'button', 29, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (31, '产品管理-录入', 'product:add', NULL, 'button', 29, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (32, '产品管理-编辑', 'product:edit', NULL, 'button', 29, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (33, '产品管理-查看', 'product:view', NULL, 'button', 29, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (34, '产品管理-删除', 'product:delete', NULL, 'button', 29, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (35, '字典管理', NULL, NULL, 'menu', 0, 100, 'Grid', NULL);
INSERT INTO `t_permission` VALUES (36, '字典类型', NULL, '/dashboard/dictype', 'menu', 35, 100, 'Postcard', 'DictypeView');
INSERT INTO `t_permission` VALUES (37, '字典类型-列表', 'dictype:list', NULL, 'button', 36, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (38, '字典类型-录入', 'dictype:add', NULL, 'button', 36, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (39, '字典类型-编辑', 'dictype:edit', NULL, 'button', 36, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (40, '字典类型-查看', 'dictype:view', NULL, 'button', 36, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (41, '字典类型-删除', 'dictype:delete', NULL, 'button', 36, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (42, '字典数据', '', '/dashboard/dicvalue', 'menu', 35, 100, 'DataAnalysis', 'DicvalueView');
INSERT INTO `t_permission` VALUES (43, '字典数据-列表', 'dicvalue:list', NULL, 'button', 42, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (44, '字典数据-录入', 'dicvalue:add', NULL, 'button', 42, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (45, '字典数据-编辑', 'dicvalue:edit', NULL, 'button', 42, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (46, '字典数据-查看', 'dicvalue:view', NULL, 'button', 42, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (47, '字典数据-删除', 'dicvalue:delete', NULL, 'button', 42, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (49, '用户管理', NULL, '/dashboard/user', 'menu', 55, 53, 'User', 'UserView');
INSERT INTO `t_permission` VALUES (50, '用户管理-列表', 'user:list', NULL, 'button', 49, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (51, '用户管理-录入', 'user:add', NULL, 'button', 49, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (52, '用户管理-编辑', 'user:edit', NULL, 'button', 49, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (53, '用户管理-查看', 'user:view', NULL, 'button', 49, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (54, '用户管理-删除', 'user:delete', NULL, 'button', 49, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (55, '系统管理', NULL, NULL, 'menu', 0, 51, 'Setting', NULL);
INSERT INTO `t_permission` VALUES (56, '权限管理', 'permission:manage', '/dashboard/permission', 'menu', 55, 55, 'Key', '');
INSERT INTO `t_permission` VALUES (57, '系统管理-列表', 'system:list', NULL, 'button', 56, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (58, '系统管理-录入', 'system:add', NULL, 'button', 56, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (59, '系统管理-编辑', 'system:edit', NULL, 'button', 56, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (60, '系统管理-查看', 'system:view', NULL, 'button', 56, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (61, '系统管理-删除', 'system:delete', NULL, 'button', 56, NULL, NULL, NULL);
INSERT INTO `t_permission` VALUES (1112, '角色管理', NULL, '/dashboard/role', 'menu', 55, 54, 'UserFilled', 'RoleView');
INSERT INTO `t_permission` VALUES (1113, '信息统计', NULL, '', 'menu', 0, 61, 'Histogram', NULL);
INSERT INTO `t_permission` VALUES (1114, '业绩分析', 'chart:performance', '/dashboard/chart/performance', 'menu', 1113, 62, 'GoldMedal', NULL);
INSERT INTO `t_permission` VALUES (1115, '数据分析', 'chart:analysis', '/dashboard/chart/analysis', 'menu', 1113, 63, 'DataLine', NULL);

-- ----------------------------
-- Table structure for t_product
-- ----------------------------
DROP TABLE IF EXISTS `t_product`;
CREATE TABLE `t_product`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键，自动增长，线索ID',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '产品名称',
  `guide_price_s` decimal(10, 2) NULL DEFAULT NULL COMMENT '官方指导起始价',
  `guide_price_e` decimal(10, 2) NULL DEFAULT NULL COMMENT '官方指导最高价',
  `quotation` decimal(10, 2) NULL DEFAULT NULL COMMENT '经销商报价',
  `state` int NULL DEFAULT NULL COMMENT '状态 0在售 1售罄',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` int NULL DEFAULT NULL COMMENT '创建人',
  `edit_time` datetime NULL DEFAULT NULL COMMENT '编辑时间',
  `edit_by` int NULL DEFAULT NULL COMMENT '编辑人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `t_product_ibfk_1`(`create_by` ASC) USING BTREE,
  INDEX `t_product_ibfk_2`(`edit_by` ASC) USING BTREE,
  CONSTRAINT `t_product_ibfk_1` FOREIGN KEY (`create_by`) REFERENCES `t_user` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `t_product_ibfk_2` FOREIGN KEY (`edit_by`) REFERENCES `t_user` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '产品表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_product
-- ----------------------------
INSERT INTO `t_product` VALUES (1, '海鸥', 10.18, 10.58, 9.28, 0, '2023-04-06 18:25:00', 1, NULL, NULL);
INSERT INTO `t_product` VALUES (2, '比亚迪e2', 10.28, 10.98, 9.78, 0, '2023-04-03 15:26:12', 1, NULL, NULL);
INSERT INTO `t_product` VALUES (3, '比亚迪e3', 15.48, 15.98, 14.38, 0, '2023-04-03 11:29:08', 1, NULL, NULL);
INSERT INTO `t_product` VALUES (4, '海豚', 11.68, 13.68, 10.86, 0, '2023-04-09 10:27:47', 1, NULL, NULL);
INSERT INTO `t_product` VALUES (5, '秦EV', 12.99, 16.98, 11.98, 0, '2023-04-08 15:28:23', 1, NULL, NULL);
INSERT INTO `t_product` VALUES (6, '秦PLUS DM-i', 9.98, 16.58, 9.06, 0, '2023-04-10 19:29:53', 1, NULL, NULL);
INSERT INTO `t_product` VALUES (7, '秦PLUS EV', 12.98, 18.08, 12.38, 0, '2023-04-05 09:30:31', 1, NULL, NULL);
INSERT INTO `t_product` VALUES (8, '海豹', 21.28, 28.98, 20.18, 0, '2023-04-02 10:31:08', 1, NULL, NULL);
INSERT INTO `t_product` VALUES (9, '汉DM', 21.78, 32.18, 19.88, 0, '2023-04-07 16:31:45', 1, NULL, NULL);
INSERT INTO `t_product` VALUES (10, '宋PLUS EV', 18.68, 20.38, 17.86, 0, '2023-03-18 21:33:08', 1, NULL, NULL);

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `role` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `role_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES (1, 'admin', '管理员');
INSERT INTO `t_role` VALUES (2, 'coach', '教练');
INSERT INTO `t_role` VALUES (5, 'accountant', '会计');
INSERT INTO `t_role` VALUES (6, 'caiwu', '财务');
INSERT INTO `t_role` VALUES (7, 'guanzhang', '馆长');

-- ----------------------------
-- Table structure for t_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_role_permission`;
CREATE TABLE `t_role_permission`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_id` int NULL DEFAULT NULL,
  `permission_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `t_role_permission_ibfk_1`(`role_id` ASC) USING BTREE,
  INDEX `t_role_permission_ibfk_2`(`permission_id` ASC) USING BTREE,
  CONSTRAINT `t_role_permission_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `t_role` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `t_role_permission_ibfk_2` FOREIGN KEY (`permission_id`) REFERENCES `t_permission` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 92 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色权限关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_role_permission
-- ----------------------------
INSERT INTO `t_role_permission` VALUES (1, 1, 10);
INSERT INTO `t_role_permission` VALUES (3, 1, 1112);
INSERT INTO `t_role_permission` VALUES (10, 1, 12);
INSERT INTO `t_role_permission` VALUES (17, 1, 19);
INSERT INTO `t_role_permission` VALUES (18, 1, 20);
INSERT INTO `t_role_permission` VALUES (22, 1, 24);
INSERT INTO `t_role_permission` VALUES (23, 1, 25);
INSERT INTO `t_role_permission` VALUES (47, 1, 49);
INSERT INTO `t_role_permission` VALUES (53, 1, 55);
INSERT INTO `t_role_permission` VALUES (54, 1, 56);
INSERT INTO `t_role_permission` VALUES (60, 2, 10);
INSERT INTO `t_role_permission` VALUES (61, 2, 12);
INSERT INTO `t_role_permission` VALUES (77, 1, 1113);
INSERT INTO `t_role_permission` VALUES (78, 1, 1114);
INSERT INTO `t_role_permission` VALUES (79, 1, 1115);
INSERT INTO `t_role_permission` VALUES (80, 1, 56);
INSERT INTO `t_role_permission` VALUES (82, 5, 24);
INSERT INTO `t_role_permission` VALUES (83, 5, 1);
INSERT INTO `t_role_permission` VALUES (85, 5, 1113);
INSERT INTO `t_role_permission` VALUES (86, 6, 10);
INSERT INTO `t_role_permission` VALUES (87, 6, 1);
INSERT INTO `t_role_permission` VALUES (88, 6, 19);
INSERT INTO `t_role_permission` VALUES (89, 6, 12);
INSERT INTO `t_role_permission` VALUES (90, 6, 20);
INSERT INTO `t_role_permission` VALUES (91, 6, 1114);

-- ----------------------------
-- Table structure for t_system_info
-- ----------------------------
DROP TABLE IF EXISTS `t_system_info`;
CREATE TABLE `t_system_info`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `system_code` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `site` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `logo` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `title` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `description` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `keywords` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `shortcuticon` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `tel` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `weixin` varchar(25) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `email` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `address` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `version` varchar(145) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `closeMsg` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `isopen` varchar(8) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT 'y',
  `create_time` datetime NULL DEFAULT NULL,
  `create_by` int NULL DEFAULT NULL,
  `edit_time` datetime NULL DEFAULT NULL,
  `edit_by` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `t_system_info_ibfk_1`(`create_by` ASC) USING BTREE,
  INDEX `t_system_info_ibfk_2`(`edit_by` ASC) USING BTREE,
  CONSTRAINT `t_system_info_ibfk_1` FOREIGN KEY (`create_by`) REFERENCES `t_user` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `t_system_info_ibfk_2` FOREIGN KEY (`edit_by`) REFERENCES `t_user` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '系统信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_system_info
-- ----------------------------
INSERT INTO `t_system_info` VALUES (1, 'crm', '动力云客系统', 'http://www.bjpowernode.com', 'http://localhost:8080/image/logo.png', '动力云客系统', '动力CRM 企业客户智慧云管理', 'crm, 客户, 客户关系, 客户关系管理', 'http://www.bjpowernode.com/favicon.ico', '010-84846003', '123456789', '123456789@qq.com', '北京市大兴区大族企业湾10栋3层', '系统版本:1.1.0.bate', '网站维护中 动力云客系统 http://www.bjpowernode.com', 'true', '2023-11-08 13:28:18', 1, NULL, NULL);
INSERT INTO `t_system_info` VALUES (2, 'call', '动力呼叫系统', 'http://www.bjpowernode.com', 'http://localhost:8080/image/logo.png', '动力呼叫系统', '动力CRM 企业客户智慧云管理', 'crm, 客户, 客户关系, 客户关系管理', 'http://www.bjpowernode.com/favicon.ico', '010-84846003', '123456789', '123456789@qq.com', '北京市大兴区大族企业湾10栋3层', '系统版本:1.1.0.bate', '网站维护中 动力呼叫系统 http://www.bjpowernode.com', 'true', '2023-11-08 13:28:21', 1, NULL, NULL);

-- ----------------------------
-- Table structure for t_tran
-- ----------------------------
DROP TABLE IF EXISTS `t_tran`;
CREATE TABLE `t_tran`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `tran_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流水号',
  `customer_id` int NULL DEFAULT NULL COMMENT '客户ID',
  `student_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学员姓名',
  `money` decimal(12, 2) NULL DEFAULT NULL COMMENT '交易金额',
  `create_by` int NULL DEFAULT NULL COMMENT '创建人',
  `deal_time` datetime NULL DEFAULT NULL COMMENT '成交时间',
  `course_type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '课程类型',
  `tran_remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '交易备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '记录创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_customer_id`(`customer_id` ASC) USING BTREE,
  CONSTRAINT `t_tran_customer_fk` FOREIGN KEY (`customer_id`) REFERENCES `t_customer` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 175 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '交易表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_tran
-- ----------------------------
INSERT INTO `t_tran` VALUES (3, 'T16-M0', 16, '林黛玉', 6980.00, 6, '2026-05-19 22:04:25', '武术套路提高班', '演示-首单', '2026-05-19 22:04:25');
INSERT INTO `t_tran` VALUES (4, 'T15-M0', 15, '贾宝玉', 6700.00, 5, '2026-05-19 22:04:25', '散打实战班', '演示-首单', '2026-05-19 22:04:25');
INSERT INTO `t_tran` VALUES (5, 'T14-M0', 14, 'Mulan', 6420.00, 4, '2026-05-19 22:04:25', '少儿武术基础班', '演示-首单', '2026-05-19 22:04:25');
INSERT INTO `t_tran` VALUES (7, 'T12-M0', 12, 'Cinderella', 5860.00, 2, '2026-05-19 22:04:25', '武术器械班', '演示-首单', '2026-05-19 22:04:25');
INSERT INTO `t_tran` VALUES (9, 'T10-M0', 10, 'Anna', 5300.00, 6, '2026-05-19 22:04:25', '少儿武术基础班', '演示-首单', '2026-05-19 22:04:25');
INSERT INTO `t_tran` VALUES (10, 'T9-M0', 9, 'Mickey', 5020.00, 5, '2026-05-19 22:04:25', '少儿武术基础班', '演示-首单', '2026-05-19 22:04:25');
INSERT INTO `t_tran` VALUES (11, 'T8-M0', 8, '王熙凤', 4740.00, 4, '2026-05-19 22:04:25', '武术套路提高班', '演示-首单', '2026-05-19 22:04:25');
INSERT INTO `t_tran` VALUES (13, 'T16-M1', 16, '林黛玉', 7100.00, 7, '2026-04-19 22:04:25', '武术套路提高班', '演示-首单', '2026-04-19 22:04:25');
INSERT INTO `t_tran` VALUES (14, 'T15-M1', 15, '贾宝玉', 6820.00, 6, '2026-04-19 22:04:25', '散打实战班', '演示-首单', '2026-04-19 22:04:25');
INSERT INTO `t_tran` VALUES (15, 'T14-M1', 14, 'Mulan', 6540.00, 5, '2026-04-19 22:04:25', '少儿武术基础班', '演示-首单', '2026-04-19 22:04:25');
INSERT INTO `t_tran` VALUES (16, 'T13-M1', 13, 'Ariel', 6260.00, 4, '2026-04-19 22:04:25', '武术套路提高班', '演示-首单', '2026-04-19 22:04:25');
INSERT INTO `t_tran` VALUES (19, 'T10-M1', 10, 'Anna', 5420.00, 7, '2026-04-19 22:04:25', '少儿武术基础班', '演示-首单', '2026-04-19 22:04:25');
INSERT INTO `t_tran` VALUES (20, 'T9-M1', 9, 'Mickey', 5140.00, 6, '2026-04-19 22:04:25', '少儿武术基础班', '演示-首单', '2026-04-19 22:04:25');
INSERT INTO `t_tran` VALUES (21, 'T8-M1', 8, '王熙凤', 4860.00, 5, '2026-04-19 22:04:25', '武术套路提高班', '演示-首单', '2026-04-19 22:04:25');
INSERT INTO `t_tran` VALUES (22, 'T7-M1', 7, 'Sophia', 4580.00, 4, '2026-04-19 22:04:25', '武术器械班', '演示-首单', '2026-04-19 22:04:25');
INSERT INTO `t_tran` VALUES (23, 'T16-M2', 16, '林黛玉', 7220.00, 2, '2026-03-19 22:04:25', '武术套路提高班', '演示-首单', '2026-03-19 22:04:25');
INSERT INTO `t_tran` VALUES (24, 'T15-M2', 15, '贾宝玉', 6940.00, 7, '2026-03-19 22:04:25', '散打实战班', '演示-首单', '2026-03-19 22:04:25');
INSERT INTO `t_tran` VALUES (25, 'T14-M2', 14, 'Mulan', 6660.00, 6, '2026-03-19 22:04:25', '少儿武术基础班', '演示-首单', '2026-03-19 22:04:25');
INSERT INTO `t_tran` VALUES (26, 'T13-M2', 13, 'Ariel', 6380.00, 5, '2026-03-19 22:04:25', '武术套路提高班', '演示-首单', '2026-03-19 22:04:25');
INSERT INTO `t_tran` VALUES (27, 'T12-M2', 12, 'Cinderella', 6100.00, 4, '2026-03-19 22:04:25', '武术器械班', '演示-首单', '2026-03-19 22:04:25');
INSERT INTO `t_tran` VALUES (29, 'T10-M2', 10, 'Anna', 5540.00, 2, '2026-03-19 22:04:25', '少儿武术基础班', '演示-首单', '2026-03-19 22:04:25');
INSERT INTO `t_tran` VALUES (30, 'T9-M2', 9, 'Mickey', 5260.00, 7, '2026-03-19 22:04:25', '少儿武术基础班', '演示-首单', '2026-03-19 22:04:25');
INSERT INTO `t_tran` VALUES (31, 'T8-M2', 8, '王熙凤', 4980.00, 6, '2026-03-19 22:04:25', '武术套路提高班', '演示-首单', '2026-03-19 22:04:25');
INSERT INTO `t_tran` VALUES (32, 'T7-M2', 7, 'Sophia', 4700.00, 5, '2026-03-19 22:04:25', '武术器械班', '演示-首单', '2026-03-19 22:04:25');
INSERT INTO `t_tran` VALUES (34, 'T15-M3', 15, '贾宝玉', 7060.00, 2, '2026-02-19 22:04:25', '散打实战班', '演示-首单', '2026-02-19 22:04:25');
INSERT INTO `t_tran` VALUES (35, 'T14-M3', 14, 'Mulan', 6780.00, 7, '2026-02-19 22:04:25', '少儿武术基础班', '演示-首单', '2026-02-19 22:04:25');
INSERT INTO `t_tran` VALUES (36, 'T13-M3', 13, 'Ariel', 6500.00, 6, '2026-02-19 22:04:25', '武术套路提高班', '演示-首单', '2026-02-19 22:04:25');
INSERT INTO `t_tran` VALUES (37, 'T12-M3', 12, 'Cinderella', 6220.00, 5, '2026-02-19 22:04:25', '武术器械班', '演示-首单', '2026-02-19 22:04:25');
INSERT INTO `t_tran` VALUES (40, 'T9-M3', 9, 'Mickey', 5380.00, 2, '2026-02-19 22:04:25', '少儿武术基础班', '演示-首单', '2026-02-19 22:04:25');
INSERT INTO `t_tran` VALUES (41, 'T8-M3', 8, '王熙凤', 5100.00, 7, '2026-02-19 22:04:25', '武术套路提高班', '演示-首单', '2026-02-19 22:04:25');
INSERT INTO `t_tran` VALUES (42, 'T7-M3', 7, 'Sophia', 4820.00, 6, '2026-02-19 22:04:25', '武术器械班', '演示-首单', '2026-02-19 22:04:25');
INSERT INTO `t_tran` VALUES (43, 'T16-M4', 16, '林黛玉', 7460.00, 4, '2026-01-19 22:04:25', '武术套路提高班', '演示-首单', '2026-01-19 22:04:25');
INSERT INTO `t_tran` VALUES (44, 'T15-M4', 15, '贾宝玉', 7180.00, 3, '2026-01-19 22:04:25', '散打实战班', '演示-首单', '2026-01-19 22:04:25');
INSERT INTO `t_tran` VALUES (45, 'T14-M4', 14, 'Mulan', 6900.00, 2, '2026-01-19 22:04:25', '少儿武术基础班', '演示-首单', '2026-01-19 22:04:25');
INSERT INTO `t_tran` VALUES (46, 'T13-M4', 13, 'Ariel', 6620.00, 7, '2026-01-19 22:04:25', '武术套路提高班', '演示-首单', '2026-01-19 22:04:25');
INSERT INTO `t_tran` VALUES (47, 'T12-M4', 12, 'Cinderella', 6340.00, 6, '2026-01-19 22:04:25', '武术器械班', '演示-首单', '2026-01-19 22:04:25');
INSERT INTO `t_tran` VALUES (49, 'T10-M4', 10, 'Anna', 5780.00, 4, '2026-01-19 22:04:25', '少儿武术基础班', '演示-首单', '2026-01-19 22:04:25');
INSERT INTO `t_tran` VALUES (51, 'T8-M4', 8, '王熙凤', 5220.00, 2, '2026-01-19 22:04:25', '武术套路提高班', '演示-首单', '2026-01-19 22:04:25');
INSERT INTO `t_tran` VALUES (52, 'T7-M4', 7, 'Sophia', 4940.00, 7, '2026-01-19 22:04:25', '武术器械班', '演示-首单', '2026-01-19 22:04:25');
INSERT INTO `t_tran` VALUES (53, 'T16-M5', 16, '林黛玉', 7580.00, 5, '2025-12-19 22:04:25', '武术套路提高班', '演示-首单', '2025-12-19 22:04:25');
INSERT INTO `t_tran` VALUES (54, 'T15-M5', 15, '贾宝玉', 7300.00, 4, '2025-12-19 22:04:25', '散打实战班', '演示-首单', '2025-12-19 22:04:25');
INSERT INTO `t_tran` VALUES (56, 'T13-M5', 13, 'Ariel', 6740.00, 2, '2025-12-19 22:04:25', '武术套路提高班', '演示-首单', '2025-12-19 22:04:25');
INSERT INTO `t_tran` VALUES (57, 'T12-M5', 12, 'Cinderella', 6460.00, 7, '2025-12-19 22:04:25', '武术器械班', '演示-首单', '2025-12-19 22:04:25');
INSERT INTO `t_tran` VALUES (59, 'T10-M5', 10, 'Anna', 5900.00, 5, '2025-12-19 22:04:25', '少儿武术基础班', '演示-首单', '2025-12-19 22:04:25');
INSERT INTO `t_tran` VALUES (60, 'T9-M5', 9, 'Mickey', 5620.00, 4, '2025-12-19 22:04:25', '少儿武术基础班', '演示-首单', '2025-12-19 22:04:25');
INSERT INTO `t_tran` VALUES (61, 'T8-M5', 8, '王熙凤', 5340.00, 3, '2025-12-19 22:04:25', '武术套路提高班', '演示-首单', '2025-12-19 22:04:25');
INSERT INTO `t_tran` VALUES (62, 'T7-M5', 7, 'Sophia', 5060.00, 2, '2025-12-19 22:04:25', '武术器械班', '演示-首单', '2025-12-19 22:04:25');
INSERT INTO `t_tran` VALUES (63, 'T16-M6', 16, '林黛玉', 7700.00, 6, '2025-11-19 22:04:25', '武术套路提高班', '演示-首单', '2025-11-19 22:04:25');
INSERT INTO `t_tran` VALUES (64, 'T15-M6', 15, '贾宝玉', 7420.00, 5, '2025-11-19 22:04:25', '散打实战班', '演示-首单', '2025-11-19 22:04:25');
INSERT INTO `t_tran` VALUES (65, 'T14-M6', 14, 'Mulan', 7140.00, 4, '2025-11-19 22:04:25', '少儿武术基础班', '演示-首单', '2025-11-19 22:04:25');
INSERT INTO `t_tran` VALUES (67, 'T12-M6', 12, 'Cinderella', 6580.00, 2, '2025-11-19 22:04:25', '武术器械班', '演示-首单', '2025-11-19 22:04:25');
INSERT INTO `t_tran` VALUES (69, 'T10-M6', 10, 'Anna', 6020.00, 6, '2025-11-19 22:04:25', '少儿武术基础班', '演示-首单', '2025-11-19 22:04:25');
INSERT INTO `t_tran` VALUES (70, 'T9-M6', 9, 'Mickey', 5740.00, 5, '2025-11-19 22:04:25', '少儿武术基础班', '演示-首单', '2025-11-19 22:04:25');
INSERT INTO `t_tran` VALUES (71, 'T8-M6', 8, '王熙凤', 5460.00, 4, '2025-11-19 22:04:25', '武术套路提高班', '演示-首单', '2025-11-19 22:04:25');
INSERT INTO `t_tran` VALUES (72, 'T7-M6', 7, 'Sophia', 5180.00, 3, '2025-11-19 22:04:25', '武术器械班', '演示-首单', '2025-11-19 22:04:25');
INSERT INTO `t_tran` VALUES (73, 'T16-M7', 16, '林黛玉', 7820.00, 7, '2025-10-19 22:04:25', '武术套路提高班', '演示-首单', '2025-10-19 22:04:25');
INSERT INTO `t_tran` VALUES (74, 'T15-M7', 15, '贾宝玉', 7540.00, 6, '2025-10-19 22:04:25', '散打实战班', '演示-首单', '2025-10-19 22:04:25');
INSERT INTO `t_tran` VALUES (75, 'T14-M7', 14, 'Mulan', 7260.00, 5, '2025-10-19 22:04:25', '少儿武术基础班', '演示-首单', '2025-10-19 22:04:25');
INSERT INTO `t_tran` VALUES (76, 'T13-M7', 13, 'Ariel', 6980.00, 4, '2025-10-19 22:04:25', '武术套路提高班', '演示-首单', '2025-10-19 22:04:25');
INSERT INTO `t_tran` VALUES (79, 'T10-M7', 10, 'Anna', 6140.00, 7, '2025-10-19 22:04:25', '少儿武术基础班', '演示-首单', '2025-10-19 22:04:25');
INSERT INTO `t_tran` VALUES (80, 'T9-M7', 9, 'Mickey', 5860.00, 6, '2025-10-19 22:04:25', '少儿武术基础班', '演示-首单', '2025-10-19 22:04:25');
INSERT INTO `t_tran` VALUES (81, 'T8-M7', 8, '王熙凤', 5580.00, 5, '2025-10-19 22:04:25', '武术套路提高班', '演示-首单', '2025-10-19 22:04:25');
INSERT INTO `t_tran` VALUES (82, 'T7-M7', 7, 'Sophia', 5300.00, 4, '2025-10-19 22:04:25', '武术器械班', '演示-首单', '2025-10-19 22:04:25');
INSERT INTO `t_tran` VALUES (83, 'T16-M8', 16, '林黛玉', 7940.00, 2, '2025-09-19 22:04:25', '武术套路提高班', '演示-首单', '2025-09-19 22:04:25');
INSERT INTO `t_tran` VALUES (84, 'T15-M8', 15, '贾宝玉', 7660.00, 7, '2025-09-19 22:04:25', '散打实战班', '演示-首单', '2025-09-19 22:04:25');
INSERT INTO `t_tran` VALUES (85, 'T14-M8', 14, 'Mulan', 7380.00, 6, '2025-09-19 22:04:25', '少儿武术基础班', '演示-首单', '2025-09-19 22:04:25');
INSERT INTO `t_tran` VALUES (86, 'T13-M8', 13, 'Ariel', 7100.00, 5, '2025-09-19 22:04:25', '武术套路提高班', '演示-首单', '2025-09-19 22:04:25');
INSERT INTO `t_tran` VALUES (87, 'T12-M8', 12, 'Cinderella', 6820.00, 4, '2025-09-19 22:04:25', '武术器械班', '演示-首单', '2025-09-19 22:04:25');
INSERT INTO `t_tran` VALUES (89, 'T10-M8', 10, 'Anna', 6260.00, 2, '2025-09-19 22:04:25', '少儿武术基础班', '演示-首单', '2025-09-19 22:04:25');
INSERT INTO `t_tran` VALUES (90, 'T9-M8', 9, 'Mickey', 5980.00, 7, '2025-09-19 22:04:25', '少儿武术基础班', '演示-首单', '2025-09-19 22:04:25');
INSERT INTO `t_tran` VALUES (91, 'T8-M8', 8, '王熙凤', 5700.00, 6, '2025-09-19 22:04:25', '武术套路提高班', '演示-首单', '2025-09-19 22:04:25');
INSERT INTO `t_tran` VALUES (92, 'T7-M8', 7, 'Sophia', 5420.00, 5, '2025-09-19 22:04:25', '武术器械班', '演示-首单', '2025-09-19 22:04:25');
INSERT INTO `t_tran` VALUES (94, 'T15-M9', 15, '贾宝玉', 7780.00, 2, '2025-08-19 22:04:25', '散打实战班', '演示-首单', '2025-08-19 22:04:25');
INSERT INTO `t_tran` VALUES (95, 'T14-M9', 14, 'Mulan', 7500.00, 7, '2025-08-19 22:04:25', '少儿武术基础班', '演示-首单', '2025-08-19 22:04:25');
INSERT INTO `t_tran` VALUES (96, 'T13-M9', 13, 'Ariel', 7220.00, 6, '2025-08-19 22:04:25', '武术套路提高班', '演示-首单', '2025-08-19 22:04:25');
INSERT INTO `t_tran` VALUES (97, 'T12-M9', 12, 'Cinderella', 6940.00, 5, '2025-08-19 22:04:25', '武术器械班', '演示-首单', '2025-08-19 22:04:25');
INSERT INTO `t_tran` VALUES (100, 'T9-M9', 9, 'Mickey', 6100.00, 2, '2025-08-19 22:04:25', '少儿武术基础班', '演示-首单', '2025-08-19 22:04:25');
INSERT INTO `t_tran` VALUES (101, 'T8-M9', 8, '王熙凤', 5820.00, 7, '2025-08-19 22:04:25', '武术套路提高班', '演示-首单', '2025-08-19 22:04:25');
INSERT INTO `t_tran` VALUES (102, 'T7-M9', 7, 'Sophia', 5540.00, 6, '2025-08-19 22:04:25', '武术器械班', '演示-首单', '2025-08-19 22:04:25');
INSERT INTO `t_tran` VALUES (103, 'T16-M10', 16, '林黛玉', 8180.00, 4, '2025-07-19 22:04:25', '武术套路提高班', '演示-首单', '2025-07-19 22:04:25');
INSERT INTO `t_tran` VALUES (105, 'T14-M10', 14, 'Mulan', 7620.00, 2, '2025-07-19 22:04:25', '少儿武术基础班', '演示-首单', '2025-07-19 22:04:25');
INSERT INTO `t_tran` VALUES (106, 'T13-M10', 13, 'Ariel', 7340.00, 7, '2025-07-19 22:04:25', '武术套路提高班', '演示-首单', '2025-07-19 22:04:25');
INSERT INTO `t_tran` VALUES (107, 'T12-M10', 12, 'Cinderella', 7060.00, 6, '2025-07-19 22:04:25', '武术器械班', '演示-首单', '2025-07-19 22:04:25');
INSERT INTO `t_tran` VALUES (109, 'T10-M10', 10, 'Anna', 6500.00, 4, '2025-07-19 22:04:25', '少儿武术基础班', '演示-首单', '2025-07-19 22:04:25');
INSERT INTO `t_tran` VALUES (110, 'T9-M10', 9, 'Mickey', 6220.00, 3, '2025-07-19 22:04:25', '少儿武术基础班', '演示-首单', '2025-07-19 22:04:25');
INSERT INTO `t_tran` VALUES (111, 'T8-M10', 8, '王熙凤', 5940.00, 2, '2025-07-19 22:04:25', '武术套路提高班', '演示-首单', '2025-07-19 22:04:25');
INSERT INTO `t_tran` VALUES (112, 'T7-M10', 7, 'Sophia', 5660.00, 7, '2025-07-19 22:04:25', '武术器械班', '演示-首单', '2025-07-19 22:04:25');
INSERT INTO `t_tran` VALUES (113, 'T16-M11', 16, '林黛玉', 8300.00, 5, '2025-06-19 22:04:25', '武术套路提高班', '演示-首单', '2025-06-19 22:04:25');
INSERT INTO `t_tran` VALUES (114, 'T15-M11', 15, '贾宝玉', 8020.00, 4, '2025-06-19 22:04:25', '散打实战班', '演示-首单', '2025-06-19 22:04:25');
INSERT INTO `t_tran` VALUES (115, 'T14-M11', 14, 'Mulan', 7740.00, 3, '2025-06-19 22:04:25', '少儿武术基础班', '演示-首单', '2025-06-19 22:04:25');
INSERT INTO `t_tran` VALUES (116, 'T13-M11', 13, 'Ariel', 7460.00, 2, '2025-06-19 22:04:25', '武术套路提高班', '演示-首单', '2025-06-19 22:04:25');
INSERT INTO `t_tran` VALUES (117, 'T12-M11', 12, 'Cinderella', 7180.00, 7, '2025-06-19 22:04:25', '武术器械班', '演示-首单', '2025-06-19 22:04:25');
INSERT INTO `t_tran` VALUES (119, 'T10-M11', 10, 'Anna', 6620.00, 5, '2025-06-19 22:04:25', '少儿武术基础班', '演示-首单', '2025-06-19 22:04:25');
INSERT INTO `t_tran` VALUES (120, 'T9-M11', 9, 'Mickey', 6340.00, 4, '2025-06-19 22:04:25', '少儿武术基础班', '演示-首单', '2025-06-19 22:04:25');
INSERT INTO `t_tran` VALUES (121, 'T8-M11', 8, '王熙凤', 6060.00, 3, '2025-06-19 22:04:25', '武术套路提高班', '演示-首单', '2025-06-19 22:04:25');
INSERT INTO `t_tran` VALUES (122, 'T7-M11', 7, 'Sophia', 5780.00, 2, '2025-06-19 22:04:25', '武术器械班', '演示-首单', '2025-06-19 22:04:25');
INSERT INTO `t_tran` VALUES (130, 'TR8-1', 8, '王熙凤', 3080.00, 8, '2026-01-19 22:04:25', '武术套路提高班', '演示-续费', '2026-01-19 22:04:25');
INSERT INTO `t_tran` VALUES (131, 'TR7-1', 7, 'Sophia', 2930.00, 5, '2026-01-19 22:04:25', '武术器械班', '演示-续费', '2026-01-19 22:04:25');
INSERT INTO `t_tran` VALUES (132, 'TR8-2', 8, '王熙凤', 3160.00, 8, '2025-10-19 22:04:25', '武术套路提高班', '演示-续费', '2025-10-19 22:04:25');
INSERT INTO `t_tran` VALUES (133, 'TR7-2', 7, 'Sophia', 3010.00, 5, '2025-10-19 22:04:25', '武术器械班', '演示-续费', '2025-10-19 22:04:25');
INSERT INTO `t_tran` VALUES (134, 'TR8-3', 8, '王熙凤', 3240.00, 8, '2025-07-19 22:04:25', '武术套路提高班', '演示-续费', '2025-07-19 22:04:25');
INSERT INTO `t_tran` VALUES (135, 'TR7-3', 7, 'Sophia', 3090.00, 5, '2025-07-19 22:04:25', '武术器械班', '演示-续费', '2025-07-19 22:04:25');
INSERT INTO `t_tran` VALUES (137, '2057415051069435904', 17, 'Ava', 9000.00, 1, '2026-05-21 18:55:48', '武术套路提高班', NULL, '2026-05-21 18:55:48');
INSERT INTO `t_tran` VALUES (138, '2058083097945624576', 15, '贾宝玉', 233.00, 1, '2026-05-06 00:00:00', '散打实战班', NULL, '2026-05-23 15:10:23');
INSERT INTO `t_tran` VALUES (139, '2058112777658617856', 18, 'Han', 2345.00, 1, '2026-05-23 17:08:19', '散打实战班', NULL, '2026-05-23 17:08:19');
INSERT INTO `t_tran` VALUES (140, '2058836188160659456', 19, '惜春', 3700.00, 2, '2026-05-25 17:02:53', '武术套路提高班', '首单报名-惜春（红楼梦演示数据）', '2026-05-25 17:02:53');
INSERT INTO `t_tran` VALUES (141, '2058836190631104512', 20, '迎春', 4000.00, 2, '2026-05-25 17:02:54', '武术器械班', '首单报名-迎春（红楼梦演示数据）', '2026-05-25 17:02:54');
INSERT INTO `t_tran` VALUES (142, '2058836193026052096', 21, '元春', 2800.00, 2, '2026-05-25 17:02:54', '少儿武术基础班', '首单报名-元春（红楼梦演示数据）', '2026-05-25 17:02:54');
INSERT INTO `t_tran` VALUES (143, '2058836194758299648', 22, '史湘云', 3100.00, 2, '2026-05-25 17:02:55', '散打实战班', '首单报名-史湘云（红楼梦演示数据）', '2026-05-25 17:02:55');
INSERT INTO `t_tran` VALUES (144, '2058836196419244032', 23, '妙玉', 3400.00, 2, '2026-05-25 17:02:55', '武术套路提高班', '首单报名-妙玉（红楼梦演示数据）', '2026-05-25 17:02:55');
INSERT INTO `t_tran` VALUES (145, '2058836198365401088', 19, '惜春', 1500.00, 2, '2026-06-24 17:02:55', '武术套路提高班', '续费-惜春（刘姥姥进大观园后复训）', '2026-05-25 17:02:56');
INSERT INTO `t_tran` VALUES (146, '2058836199292342272', 20, '迎春', 1700.00, 2, '2026-07-01 17:02:55', '武术器械班', '续费-迎春（刘姥姥进大观园后复训）', '2026-05-25 17:02:56');
INSERT INTO `t_tran` VALUES (149, '2058836212928024576', 25, '甄士隐', 4000.00, 3, '2026-05-25 17:02:59', '武术器械班', '首单报名-甄士隐（红楼梦演示数据）', '2026-05-25 17:02:59');
INSERT INTO `t_tran` VALUES (151, '2058836215784345600', 27, '马道婆', 3100.00, 3, '2026-05-25 17:03:00', '散打实战班', '首单报名-马道婆（红楼梦演示数据）', '2026-05-25 17:03:00');
INSERT INTO `t_tran` VALUES (152, '2058836217378181120', 28, '蒋玉菡', 3400.00, 3, '2026-05-25 17:03:00', '武术套路提高班', '首单报名-蒋玉菡（红楼梦演示数据）', '2026-05-25 17:03:00');
INSERT INTO `t_tran` VALUES (153, '2058836218435145728', 24, '柳湘莲', 1500.00, 3, '2026-06-24 17:03:00', '武术套路提高班', '续费-柳湘莲（刘姥姥进大观园后复训）', '2026-05-25 17:03:00');
INSERT INTO `t_tran` VALUES (156, '2058836230175002624', 29, '晴雯', 3700.00, 5, '2026-05-25 17:03:03', '武术套路提高班', '首单报名-晴雯（红楼梦演示数据）', '2026-05-25 17:03:03');
INSERT INTO `t_tran` VALUES (157, '2058836231819169792', 30, '平儿', 4000.00, 5, '2026-05-25 17:03:04', '武术器械班', '首单报名-平儿（红楼梦演示数据）', '2026-05-25 17:03:04');
INSERT INTO `t_tran` VALUES (158, '2058836233111015424', 31, '香菱', 2800.00, 5, '2026-05-25 17:03:04', '少儿武术基础班', '首单报名-香菱（红楼梦演示数据）', '2026-05-25 17:03:04');
INSERT INTO `t_tran` VALUES (159, '2058836234482552832', 32, '紫鹃', 3100.00, 5, '2026-05-25 17:03:04', '散打实战班', '首单报名-紫鹃（红楼梦演示数据）', '2026-05-25 17:03:04');
INSERT INTO `t_tran` VALUES (160, '2058836235870867456', 33, '鸳鸯', 3400.00, 5, '2026-05-25 17:03:05', '武术套路提高班', '首单报名-鸳鸯（红楼梦演示数据）', '2026-05-25 17:03:05');
INSERT INTO `t_tran` VALUES (161, '2058836237011718144', 29, '晴雯', 1500.00, 5, '2026-06-24 17:03:04', '武术套路提高班', '续费-晴雯（刘姥姥进大观园后复训）', '2026-05-25 17:03:05');
INSERT INTO `t_tran` VALUES (162, '2058836237699584000', 30, '平儿', 1700.00, 5, '2026-07-01 17:03:05', '武术器械班', '续费-平儿（刘姥姥进大观园后复训）', '2026-05-25 17:03:05');
INSERT INTO `t_tran` VALUES (163, '2058836238492307456', 31, '香菱', 1900.00, 5, '2026-07-08 17:03:05', '少儿武术基础班', '续费-香菱（刘姥姥进大观园后复训）', '2026-05-25 17:03:05');
INSERT INTO `t_tran` VALUES (164, '2058836248202121216', 34, '入画', 3700.00, 6, '2026-05-25 17:03:08', '武术套路提高班', '首单报名-入画（红楼梦演示数据）', '2026-05-25 17:03:08');
INSERT INTO `t_tran` VALUES (165, '2058836249582047232', 35, '抱琴', 4000.00, 6, '2026-05-25 17:03:08', '武术器械班', '首单报名-抱琴（红楼梦演示数据）', '2026-05-25 17:03:08');
INSERT INTO `t_tran` VALUES (166, '2058836251096190976', 36, '莺儿', 2800.00, 6, '2026-05-25 17:03:08', '少儿武术基础班', '首单报名-莺儿（红楼梦演示数据）', '2026-05-25 17:03:08');
INSERT INTO `t_tran` VALUES (167, '2058836252375453696', 37, '麝月', 3100.00, 6, '2026-05-25 17:03:09', '散打实战班', '首单报名-麝月（红楼梦演示数据）', '2026-05-25 17:03:09');
INSERT INTO `t_tran` VALUES (168, '2058836253507915776', 38, '秋纹', 3400.00, 6, '2026-05-25 17:03:09', '武术套路提高班', '首单报名-秋纹（红楼梦演示数据）', '2026-05-25 17:03:09');
INSERT INTO `t_tran` VALUES (169, '2058836254195781632', 34, '入画', 1500.00, 6, '2026-06-24 17:03:08', '武术套路提高班', '续费-入画（刘姥姥进大观园后复训）', '2026-05-25 17:03:09');
INSERT INTO `t_tran` VALUES (171, '2058836255525376000', 36, '莺儿', 1900.00, 6, '2026-07-08 17:03:09', '少儿武术基础班', '续费-莺儿（刘姥姥进大观园后复训）', '2026-05-25 17:03:09');
INSERT INTO `t_tran` VALUES (172, '2067236334535733248', 39, '邢夫人', 5000.00, 1, '2026-06-17 21:22:04', '散打实战班', NULL, '2026-06-17 21:22:04');
INSERT INTO `t_tran` VALUES (173, '2067236510021218304', 40, 'Oleg', 5000.00, 1, '2026-06-17 21:22:46', '武术器械班', NULL, '2026-06-17 21:22:46');
INSERT INTO `t_tran` VALUES (174, '2067236585921343488', 41, '平儿', 5000.00, 1, '2026-06-17 21:23:04', '成人剑术', NULL, '2026-06-17 21:23:04');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键，自动增长，用户ID',
  `login_act` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '登录账号',
  `login_pwd` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '登录密码',
  `name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `phone` varchar(18) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户手机',
  `email` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `account_no_expired` int NULL DEFAULT NULL COMMENT '账户是否没有过期，0已过期 1正常',
  `credentials_no_expired` int NULL DEFAULT NULL COMMENT '密码是否没有过期，0已过期 1正常',
  `account_no_locked` int NULL DEFAULT NULL COMMENT '账号是否没有锁定，0已锁定 1正常',
  `account_enabled` int NULL DEFAULT NULL COMMENT '账号是否启用，0禁用 1启用',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` int NULL DEFAULT NULL COMMENT '创建人',
  `edit_time` datetime NULL DEFAULT NULL COMMENT '编辑时间',
  `edit_by` int NULL DEFAULT NULL COMMENT '编辑人',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最近登录时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `login_act`(`login_act` ASC) USING BTREE,
  UNIQUE INDEX `phone`(`phone` ASC) USING BTREE,
  UNIQUE INDEX `email`(`email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 61 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, 'admin', '$2a$10$M/Cj/AMCXXHbf3nlRhivoO81o5n9ETel0Mo8v5QAjYMKrzJP6w1Ei', '管理员', '13700000000', 'admin@qq.com', 1, 1, 1, 1, '2023-02-22 09:37:12', NULL, '2024-06-24 10:48:48', 1, '2024-01-11 11:20:17');
INSERT INTO `t_user` VALUES (2, 'yuyan', '$2a$10$WEkWUvuJOV/q7lRXzmdvgulskx8EhLPU8N.hdnfNyk.AYqYSPwYHa', 'Monica', '13809090908', 'yuyan@163.com', 1, 1, 1, 1, '2023-02-28 12:11:40', 1, '2024-06-22 16:31:32', 2, '2023-12-11 21:07:07');
INSERT INTO `t_user` VALUES (3, 'zhangqi', '$2a$10$Q0qTW6QqkabTzFyoilViw..YdrVzZkSKe5RvLmjgPgW/IrcPkBoF.', 'Chandler', '1362362323', 'zhangqi@qq.com', 1, 1, 1, 1, '2023-03-02 11:37:34', NULL, '2023-05-23 00:21:02', NULL, '2023-12-11 21:07:28');
INSERT INTO `t_user` VALUES (4, 'suwanting', '$2a$10$3bambNLTCAKtQn2OXPiHb.f0SzH.MucTiLi6GPT6nQrYpsxsdxaFi', 'Ross', NULL, 'suwanting@qq.com', 1, 1, 1, 1, '2023-04-03 15:04:54', NULL, NULL, NULL, NULL);
INSERT INTO `t_user` VALUES (6, 'mengyan', '$2a$10$6zGT7CfeuJ/6jZPk1pAqcuiMYDnCJstrceThGD5DVVOA5XvOP/sQq', 'Phoebe', NULL, 'mengyan@163.com', 1, 1, 1, 1, '2023-03-19 10:17:28', NULL, NULL, NULL, NULL);
INSERT INTO `t_user` VALUES (7, 'yuanhuimin', '$2a$10$mbsloGtPV7cDwfAVYxuvLemQRWumZKrDxVZxg4fnbfaocnfZFlYuu', 'Joey', NULL, 'yuanhuimin@11.com', 1, 1, 1, 1, '2023-04-11 20:18:50', NULL, NULL, NULL, NULL);
INSERT INTO `t_user` VALUES (8, 'qinxuwen', '$2a$10$ir8uLlBrPMHRtGiu5Ajkv.UKcRacXWRen7zxelp9iUaco3WhGkJ36', '贾宝玉', '13820000000', 'qinxuwen@163.com', 1, 1, 1, 1, '2023-03-19 21:11:37', NULL, NULL, NULL, NULL);
INSERT INTO `t_user` VALUES (12, 'dengping', '$2a$10$hpN8orfqUFXb.WWbIoZBkOZrr6D8rdSbl/SWXsMQ0zEuqkldlkpW2', '林黛玉', NULL, 'dengping@qq.com', 1, 1, 1, 1, '2023-02-19 20:10:58', NULL, NULL, NULL, NULL);
INSERT INTO `t_user` VALUES (14, 'zhangmeng', '$2a$10$MMHG2cQh4H4YFbdf48SnyO9IZ78F110x3.7IWGNExrgk2rFmhrd/u', 'Max', NULL, 'zhangmeng@qq.com', 1, 1, 1, 1, '2023-01-13 08:16:02', NULL, NULL, NULL, NULL);
INSERT INTO `t_user` VALUES (15, 'shixixiang', '$2a$10$zYwq/QfevFPAZxw4b2DkCeQvjVQ52AUU9c4aC0uS0wTJaRr75G74y', 'Caroline', NULL, 'shixixiang@qq.com', 1, 1, 1, 1, '2023-03-10 15:19:49', NULL, NULL, NULL, NULL);
INSERT INTO `t_user` VALUES (16, 'chengjiuming', '$2a$10$yNN5TcFkM4OqRsKGNM8CNeqAJhRYKQgXVFqbre5lQPicnIXT7THTu', '陈久明', NULL, 'chengjiuming@163.com', 1, 1, 1, 1, '2023-04-09 23:17:37', NULL, NULL, NULL, NULL);
INSERT INTO `t_user` VALUES (17, 'genghao', '$2a$10$rWHo.vUpJCbqWLGMkPj95O5FlhaQLzro.LY7pVQ/UnVVAdvjEAy0K', '耿浩', NULL, 'genghao@qq.com', 1, 1, 1, 1, '2023-03-19 12:10:22', NULL, '2023-04-10 21:42:21', NULL, NULL);
INSERT INTO `t_user` VALUES (18, 'hanmingyang', '$2a$10$PRMdG7a8nFIN1A3TD584Xe2BZI7Y0mktDL7Wp5lF88E1D1iPijFc6', '韩明洋', NULL, 'hanmingyang@163.com', 1, 1, 1, 1, '2023-02-12 18:13:01', NULL, '2023-04-13 23:43:25', NULL, NULL);
INSERT INTO `t_user` VALUES (19, 'xuyan', '$2a$10$S7MF2dOqFcoOJPqpEH2nu.Muhn2XC0BlBTZ5gAoL3axrQxdJEJNnK', '徐燕', NULL, 'xuyan@qq.com', 1, 1, 1, 1, '2023-03-29 13:16:15', NULL, NULL, NULL, NULL);
INSERT INTO `t_user` VALUES (20, 'chengjuan', '$2a$10$m1g5cxikApV05pR7Cx4cy.d4sT3efOl6UvDLvH27WzMjtpymQ5ANi', '程娟', NULL, 'chengjuan@qq.com', 1, 1, 1, 1, '2023-02-19 15:12:22', NULL, NULL, NULL, NULL);
INSERT INTO `t_user` VALUES (21, 'huangxiao', '$2a$10$R/RwQd5.3OxYpSZBLIn8DeeYYNF0vgWCrCR4tcyL.c/HtnuIfBRIK', '黄潇', NULL, 'huangxiao@qq.com', 1, 1, 1, 1, '2023-03-26 22:11:37', NULL, NULL, NULL, NULL);
INSERT INTO `t_user` VALUES (50, 'duli', '$2a$10$k.9XN1WE6XhsIIA/b4vExuo3wM8zxs1ofG26EXNHiamyPeawErx.m', '杜力', '17625280790', '88866666@qq.com', 1, 1, 1, 1, '2025-06-18 21:33:52', 1, '2026-05-21 18:17:47', 1, NULL);
INSERT INTO `t_user` VALUES (52, 'huarui', NULL, '花睿', '1234323412', NULL, 1, 1, 1, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_user` VALUES (54, 'xiaoyang', NULL, '小羊', '1231243', NULL, 1, 1, 1, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_user` VALUES (56, 'chandler', '$2a$10$08BDZifUUQcxWWxm4UZt5eRy7JDSRtSqjxXoRMlgx/kPIFlDR1kBG', 'chandler', '15511111111', NULL, 1, 1, 1, 1, '2026-05-14 18:52:21', 1, NULL, NULL, NULL);
INSERT INTO `t_user` VALUES (58, 'jiaxuan', '$2a$10$ISQLd1zDlajRP53QxWmZq.bxAjHD882C/3hG2GFBd.psglTpPndE2', '佳轩', '15533333333', 'null', 1, 1, 1, 1, '2026-05-14 19:03:44', 1, '2026-05-14 19:04:46', 1, NULL);
INSERT INTO `t_user` VALUES (59, 'wangjiaolian', '$2a$10$jgXkGqdKadlpxAtuvetMUeT3NvKKOxBNDB/QR5O89TLkwy7Q/2tAy', '王承辉', '17727727777', NULL, 0, 0, 0, 1, '2026-05-19 19:04:11', 1, NULL, NULL, NULL);
INSERT INTO `t_user` VALUES (60, 'ceshi', '$2a$10$QYbY40R/54Lawi4JLdyRieu7kw6VoY4Kh8WxJYVSVT2JdMxdLAR5.', 'test1', '17655589098', NULL, 0, 0, 0, 1, '2026-05-21 17:55:01', 1, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL,
  `role_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `t_user_role_ibfk_1`(`user_id` ASC) USING BTREE,
  INDEX `t_user_role_ibfk_2`(`role_id` ASC) USING BTREE,
  CONSTRAINT `t_user_role_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `t_user_role_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `t_role` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES (1, 1, 1);
INSERT INTO `t_user_role` VALUES (3, 50, 2);
INSERT INTO `t_user_role` VALUES (4, 52, 2);
INSERT INTO `t_user_role` VALUES (5, 54, 2);
INSERT INTO `t_user_role` VALUES (6, 56, 2);
INSERT INTO `t_user_role` VALUES (7, 58, 2);
INSERT INTO `t_user_role` VALUES (10, 59, 2);
INSERT INTO `t_user_role` VALUES (11, 59, 5);
INSERT INTO `t_user_role` VALUES (12, 14, 2);
INSERT INTO `t_user_role` VALUES (13, 15, 2);

SET FOREIGN_KEY_CHECKS = 1;
