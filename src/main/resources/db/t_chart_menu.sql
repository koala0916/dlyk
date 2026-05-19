-- 菜单：信息统计 -> 业绩分析、数据分析（在 dlyk 库执行，按实际表结构调整）
-- 查找「信息统计」父菜单 id
-- SELECT id, name, url FROM t_permission WHERE name LIKE '%统计%';

-- 示例：父菜单 id = 200（请改成你的实际 id）
-- UPDATE t_permission SET name = '信息统计', type = 'M' WHERE id = 200;

-- 删除或禁用旧的单页图表菜单（url 为空或指向旧 StatisticsView）
-- DELETE FROM t_permission WHERE url = '' AND parent_id = 200;

-- INSERT INTO t_permission (name, code, url, type, parent_id, order_no, icon) VALUES
-- ('业绩分析', 'chart:performance', '/dashboard/chart/performance', 'C', 200, 1, 'TrendCharts'),
-- ('数据分析', 'chart:analysis', '/dashboard/chart/analysis', 'C', 200, 2, 'PieChart');

-- 给管理员角色授权后重新登录生效
-- INSERT INTO t_role_permission (role_id, permission_id) ...
