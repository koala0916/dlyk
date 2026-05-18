-- 线索、交易表改造（武术培训）执行前请备份
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS t_clue_edit_log;
DROP TABLE IF EXISTS t_clue_remark;
DROP TABLE IF EXISTS t_tran_history;
DROP TABLE IF EXISTS t_tran_remark;
DROP TABLE IF EXISTS t_tran;
DROP TABLE IF EXISTS t_clue;

CREATE TABLE t_clue (
  id INT NOT NULL AUTO_INCREMENT COMMENT '主键',
  name VARCHAR(64) NOT NULL COMMENT '姓名',
  age INT NULL COMMENT '年龄',
  phone VARCHAR(20) NOT NULL COMMENT '电话',
  intention_course VARCHAR(128) NULL COMMENT '意向课程',
  intention_strength INT NULL COMMENT '意向强度1-10',
  source VARCHAR(128) NULL COMMENT '来源',
  remark TEXT NULL COMMENT '备注',
  trial_class_time DATETIME NULL COMMENT '体验课时间',
  clue_status VARCHAR(32) NOT NULL DEFAULT '未联系' COMMENT '未联系/已联系/已转客户',
  create_by INT NULL COMMENT '创建人ID',
  create_time DATETIME NULL COMMENT '创建时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='线索表';

CREATE TABLE t_clue_edit_log (
  id INT NOT NULL AUTO_INCREMENT,
  clue_id INT NOT NULL,
  edit_by INT NULL,
  edit_time DATETIME NOT NULL,
  change_content VARCHAR(2000) NOT NULL,
  PRIMARY KEY (id),
  KEY idx_clue_id (clue_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='线索编辑日志';

CREATE TABLE t_tran (
  id INT NOT NULL AUTO_INCREMENT,
  tran_no VARCHAR(64) NOT NULL COMMENT '流水号',
  customer_id INT NULL COMMENT '客户ID',
  student_name VARCHAR(64) NULL COMMENT '学员姓名',
  money DECIMAL(12,2) NULL COMMENT '交易金额',
  create_by INT NULL COMMENT '创建人',
  deal_time DATETIME NULL COMMENT '成交时间',
  course_type VARCHAR(128) NULL COMMENT '课程类型',
  tran_remark VARCHAR(500) NULL COMMENT '交易备注',
  create_time DATETIME NULL COMMENT '记录创建时间',
  PRIMARY KEY (id),
  KEY idx_customer_id (customer_id),
  CONSTRAINT t_tran_customer_fk FOREIGN KEY (customer_id) REFERENCES t_customer (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交易表';

INSERT INTO t_clue (name, age, phone, intention_course, intention_strength, source, remark, trial_class_time, clue_status, create_by, create_time) VALUES
('赵小虎', 11, '13900002001', '少儿武术基础班', 8, '地推', '家长很积极', DATE_ADD(NOW(), INTERVAL 3 DAY), '未联系', 1, NOW()),
('孙丽娜', 16, '13900002002', '散打实战班', 6, '转介绍', NULL, DATE_ADD(NOW(), INTERVAL 10 DAY), '已联系', 1, DATE_SUB(NOW(), INTERVAL 2 DAY)),
('周天宇', 13, '13900002003', '武术套路提高班', 9, '线上活动', '已预约体验课', DATE_ADD(NOW(), INTERVAL 5 DAY), '已联系', 1, DATE_SUB(NOW(), INTERVAL 1 DAY));

SET FOREIGN_KEY_CHECKS = 1;
