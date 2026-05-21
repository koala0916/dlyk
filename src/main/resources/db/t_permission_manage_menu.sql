-- 权限管理菜单（与角色管理平级，请在 dlyk 库执行）
-- 1. 查找系统管理/权限相关父菜单 id：
-- SELECT id, name, url, parent_id FROM t_permission WHERE name LIKE '%角色%' OR name LIKE '%系统%';

-- 2. 假设与「角色管理」同一父菜单 parent_id = <父id>，在角色管理后增加一项：
-- INSERT INTO t_permission (name, code, url, type, parent_id, order_no, icon) VALUES
-- ('权限管理', 'permission:manage', '/dashboard/permission', 'menu', <父id>, 2, 'Key');

-- 3. 为管理员角色授权（假设管理员 role_id = 1）：
-- INSERT INTO t_role_permission (role_id, permission_id)
-- SELECT 1, id FROM t_permission WHERE url = '/dashboard/permission' LIMIT 1;

-- 执行后重新登录，侧栏会出现「权限管理」
