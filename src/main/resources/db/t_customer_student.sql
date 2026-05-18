-- 客户表改造为武术学员信息（可清空后执行）
-- 执行前请备份；本脚本会删除原 t_customer 数据

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS t_customer_edit_log;

DROP TABLE IF EXISTS t_customer_remark;

DROP TABLE IF EXISTS t_customer;

CREATE TABLE t_customer (
  id INT NOT NULL AUTO_INCREMENT COMMENT '主键',
  name VARCHAR(64) NOT NULL COMMENT '姓名',
  phone VARCHAR(20) NOT NULL COMMENT '电话',
  age INT NULL COMMENT '年龄',
  course_type VARCHAR(64) NULL COMMENT '课程类型',
  remaining_lessons INT NULL COMMENT '剩余课时',
  course_expire_time DATETIME NULL COMMENT '课程到期时间',
  remark TEXT NULL COMMENT '备注',
  create_by INT NULL COMMENT '创建人ID',
  create_time DATETIME NULL COMMENT '创建时间',
  source VARCHAR(128) NULL COMMENT '来源',
  studying TINYINT NOT NULL DEFAULT 1 COMMENT '是否正在学习 0否 1是',
  clue_id INT NULL COMMENT '预留：线索转客户',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户(学员)表';

CREATE TABLE t_customer_edit_log (
  id INT NOT NULL AUTO_INCREMENT,
  customer_id INT NOT NULL COMMENT '客户ID',
  edit_by INT NULL COMMENT '编辑人ID',
  edit_time DATETIME NOT NULL COMMENT '编辑时间',
  change_content VARCHAR(2000) NOT NULL COMMENT '修改内容说明',
  PRIMARY KEY (id),
  KEY idx_customer_id (customer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户编辑日志';

INSERT INTO t_customer (name, phone, age, course_type, remaining_lessons, course_expire_time, remark, create_by, create_time, source, studying) VALUES
('张小明', '13800001001', 12, '少儿武术基础班', 24, DATE_ADD(NOW(), INTERVAL 90 DAY), '家长要求周末班', 1, NOW(), '门店咨询', 1),
('李雨萱', '13800001002', 15, '武术套路提高班', 2, DATE_ADD(NOW(), INTERVAL 10 DAY), '比赛集训', 1, DATE_SUB(NOW(), INTERVAL 30 DAY), '老学员续费', 1),
('王浩然', '13800001003', 10, '少儿武术基础班', 8, DATE_ADD(NOW(), INTERVAL 60 DAY), NULL, 1, DATE_SUB(NOW(), INTERVAL 15 DAY), '转介绍', 1),
('陈思琪', '13800001004', 18, '散打实战班', 0, DATE_SUB(NOW(), INTERVAL 5 DAY), '已结课待续费', 1, DATE_SUB(NOW(), INTERVAL 120 DAY), '线上活动', 0),
('刘子轩', '13800001005', 14, '武术器械班', 16, DATE_ADD(NOW(), INTERVAL 120 DAY), '对刀术感兴趣', 1, NOW(), '地推', 1);

SET FOREIGN_KEY_CHECKS = 1;
