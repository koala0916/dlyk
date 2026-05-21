-- 侧栏图标须与 @element-plus/icons-vue 组件名一致（区分大小写，如 User、Setting、TrendCharts）
-- 执行后重新登录查看侧栏

-- 示例（按实际 id / name 修改）：
-- UPDATE t_permission SET icon = 'User' WHERE name LIKE '%用户%';
-- UPDATE t_permission SET icon = 'Memo' WHERE name LIKE '%线索%';
-- UPDATE t_permission SET icon = 'UserFilled' WHERE name LIKE '%客户%';
-- UPDATE t_permission SET icon = 'Wallet' WHERE name LIKE '%交易%';
-- UPDATE t_permission SET icon = 'Setting' WHERE name LIKE '%角色%';
-- UPDATE t_permission SET icon = 'Key' WHERE name = '权限管理';
-- UPDATE t_permission SET icon = 'TrendCharts' WHERE name = '业绩分析';
-- UPDATE t_permission SET icon = 'PieChart' WHERE name = '数据分析';
-- UPDATE t_permission SET icon = 'DataAnalysis' WHERE name LIKE '%统计%';

-- 查当前配置：
-- SELECT id, name, icon, url, type FROM t_permission WHERE type = 'menu' ORDER BY parent_id, order_no;
