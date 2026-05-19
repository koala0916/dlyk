-- 图表统计演示数据 + 菜单（请在 MySQL dlyk 库手动执行）
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 销售/负责人使用影视剧角色名（保留 id 1 为 admin）
UPDATE t_user SET name = 'Monica' WHERE id = 2;
UPDATE t_user SET name = 'Chandler' WHERE id = 3;
UPDATE t_user SET name = 'Ross' WHERE id = 4;
UPDATE t_user SET name = 'Rachel' WHERE id = 5;
UPDATE t_user SET name = 'Phoebe' WHERE id = 6;
UPDATE t_user SET name = 'Joey' WHERE id = 7;
UPDATE t_user SET name = '贾宝玉' WHERE id = 8;
UPDATE t_user SET name = '林黛玉' WHERE id = 12;
UPDATE t_user SET name = 'Max' WHERE id = 14;
UPDATE t_user SET name = 'Caroline' WHERE id = 15;

-- 清空业务表后插入演示数据（若需保留原数据请注释本段）
DELETE FROM t_tran;
DELETE FROM t_clue_edit_log;
DELETE FROM t_customer_edit_log;
DELETE FROM t_clue;
DELETE FROM t_customer;

-- 线索
INSERT INTO t_clue (name, age, phone, intention_course, intention_strength, source, remark, trial_class_time, clue_status, create_by, create_time) VALUES
('Emma', 22, '13910001001', '少儿武术基础班', 8, '抖音团购', NULL, DATE_ADD(NOW(), INTERVAL 3 DAY), '未联系', 2, DATE_SUB(NOW(), INTERVAL 2 MONTH)),
('Olivia', 16, '13910001002', '散打实战班', 6, '转介绍', NULL, DATE_ADD(NOW(), INTERVAL 5 DAY), '已联系', 3, DATE_SUB(NOW(), INTERVAL 50 DAY)),
('Ava', 12, '13910001003', '武术套路提高班', 9, '门店咨询', NULL, DATE_SUB(NOW(), INTERVAL 10 DAY), '已联系', 4, DATE_SUB(NOW(), INTERVAL 40 DAY)),
('Sophia', 18, '13910001004', '武术器械班', 7, '线上活动', NULL, DATE_SUB(NOW(), INTERVAL 5 DAY), '已转客户', 5, DATE_SUB(NOW(), INTERVAL 90 DAY)),
('Isabella', 14, '13910001005', '少儿武术基础班', 5, '地推', NULL, DATE_ADD(NOW(), INTERVAL 7 DAY), '未联系', 6, DATE_SUB(NOW(), INTERVAL 20 DAY)),
('Mia', 20, '13910001006', '散打实战班', 8, '抖音团购', NULL, DATE_SUB(NOW(), INTERVAL 15 DAY), '已联系', 7, DATE_SUB(NOW(), INTERVAL 60 DAY)),
('王熙凤', 19, '13910001007', '武术套路提高班', 9, '转介绍', NULL, DATE_SUB(NOW(), INTERVAL 8 DAY), '已转客户', 8, DATE_SUB(NOW(), INTERVAL 100 DAY)),
('薛宝钗', 17, '13910001008', '少儿武术基础班', 6, '门店咨询', NULL, DATE_ADD(NOW(), INTERVAL 2 DAY), '已联系', 2, DATE_SUB(NOW(), INTERVAL 30 DAY)),
('Han', 21, '13910001009', '散打实战班', 7, '抖音团购', NULL, DATE_SUB(NOW(), INTERVAL 12 DAY), '已联系', 14, DATE_SUB(NOW(), INTERVAL 45 DAY)),
('Oleg', 25, '13910001010', '武术器械班', 4, '地推', NULL, DATE_ADD(NOW(), INTERVAL 4 DAY), '未联系', 15, DATE_SUB(NOW(), INTERVAL 15 DAY)),
('Mickey', 10, '13910001011', '少儿武术基础班', 10, '转介绍', NULL, DATE_SUB(NOW(), INTERVAL 6 DAY), '已转客户', 3, DATE_SUB(NOW(), INTERVAL 80 DAY)),
('Elsa', 13, '13910001012', '武术套路提高班', 8, '线上活动', NULL, DATE_SUB(NOW(), INTERVAL 20 DAY), '已联系', 4, DATE_SUB(NOW(), INTERVAL 25 DAY));

-- 客户（部分由线索转化）
INSERT INTO t_customer (name, phone, age, course_type, remaining_lessons, course_expire_time, remark, create_by, create_time, source, studying, clue_id) VALUES
('Sophia', '13910001004', 18, '武术器械班', 20, DATE_ADD(NOW(), INTERVAL 120 DAY), NULL, 5, DATE_SUB(NOW(), INTERVAL 85 DAY), '线上活动', 1, 4),
('王熙凤', '13910001007', 19, '武术套路提高班', 16, DATE_ADD(NOW(), INTERVAL 90 DAY), NULL, 8, DATE_SUB(NOW(), INTERVAL 95 DAY), '转介绍', 1, 7),
('Mickey', '13910001011', 10, '少儿武术基础班', 24, DATE_ADD(NOW(), INTERVAL 180 DAY), NULL, 3, DATE_SUB(NOW(), INTERVAL 75 DAY), '转介绍', 1, 11),
('Anna', '13910002001', 11, '少儿武术基础班', 12, DATE_ADD(NOW(), INTERVAL 60 DAY), NULL, 2, DATE_SUB(NOW(), INTERVAL 200 DAY), '抖音团购', 1, NULL),
('Belle', '13910002002', 16, '散打实战班', 8, DATE_ADD(NOW(), INTERVAL 45 DAY), NULL, 3, DATE_SUB(NOW(), INTERVAL 180 DAY), '转介绍', 1, NULL),
('Cinderella', '13910002003', 22, '武术器械班', 0, DATE_SUB(NOW(), INTERVAL 10 DAY), '已结课', 4, DATE_SUB(NOW(), INTERVAL 300 DAY), '门店咨询', 0, NULL),
('Ariel', '13910002004', 14, '武术套路提高班', 6, DATE_ADD(NOW(), INTERVAL 30 DAY), NULL, 5, DATE_SUB(NOW(), INTERVAL 150 DAY), '线上活动', 1, NULL),
('Mulan', '13910002005', 15, '少儿武术基础班', 18, DATE_ADD(NOW(), INTERVAL 100 DAY), NULL, 6, DATE_SUB(NOW(), INTERVAL 120 DAY), '地推', 1, NULL),
('贾宝玉', '13910002006', 20, '散打实战班', 10, DATE_ADD(NOW(), INTERVAL 80 DAY), NULL, 8, DATE_SUB(NOW(), INTERVAL 100 DAY), '抖音团购', 1, NULL),
('林黛玉', '13910002007', 17, '武术套路提高班', 14, DATE_ADD(NOW(), INTERVAL 70 DAY), NULL, 12, DATE_SUB(NOW(), INTERVAL 90 DAY), '转介绍', 1, NULL);

-- 交易：每位客户在近 12 个月各有一笔（折线图、饼图用）；create_by 在 2~7 间轮换
INSERT INTO t_tran (tran_no, customer_id, student_name, money, create_by, deal_time, course_type, tran_remark, create_time)
SELECT
  CONCAT('T', c.id, '-M', mo.n),
  c.id,
  c.name,
  2500 + c.id * 280 + mo.n * 120,
  2 + ((c.id + mo.n) % 6),
  DATE_SUB(NOW(), INTERVAL mo.n MONTH),
  c.course_type,
  '演示-首单',
  DATE_SUB(NOW(), INTERVAL mo.n MONTH)
FROM t_customer c
CROSS JOIN (
  SELECT 0 AS n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3
  UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7
  UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11
) mo;

-- 续费：前 8 位客户每人再追加 2~3 笔（漏斗「续费交易数」用）
INSERT INTO t_tran (tran_no, customer_id, student_name, money, create_by, deal_time, course_type, tran_remark, create_time)
SELECT
  CONCAT('TR', c.id, '-', k.k),
  c.id,
  c.name,
  1800 + c.id * 150 + k.k * 80,
  c.create_by,
  DATE_SUB(NOW(), INTERVAL (k.k * 3 + 1) MONTH),
  c.course_type,
  '演示-续费',
  DATE_SUB(NOW(), INTERVAL (k.k * 3 + 1) MONTH)
FROM t_customer c
CROSS JOIN (SELECT 1 AS k UNION ALL SELECT 2 UNION ALL SELECT 3) k
WHERE c.id <= 8;

-- 菜单：将原「信息统计」下子菜单改为业绩分析、数据分析（请根据实际 parent_id 调整）
-- 若已有「图表分析」子菜单，可执行：
-- UPDATE t_permission SET name='业绩分析', url='/dashboard/chart/performance', code='chart:performance' WHERE url='' OR url='/dashboard';
-- INSERT 数据分析 ...

SET FOREIGN_KEY_CHECKS = 1;
