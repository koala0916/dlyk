package com.bjpowernode.service.impl;

import com.bjpowernode.entity.TPermission;
import com.bjpowernode.mapper.TPermissionMapper;
import com.bjpowernode.service.PermissionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 权限相关业务实现类。
 * <p>
 * 本类目前只做一件事：根据「用户 id」查出他能在左侧看到的菜单树。
 * 数据关系（便于理解 SQL）：
 * 用户 t_user → 用户角色 t_user_role → 角色 t_role → 角色权限 t_role_permission → 权限 t_permission
 * 只有 type = 'menu' 的权限记录会参与左侧菜单展示；按钮类权限（如 customer:add）不在此处理。
 * </p>
 */
@Service // 标记为 Spring 服务 Bean，可被 Controller 等通过 @Resource / @Autowired 注入
public class PermissionServiceImpl implements PermissionService {

    /** 操作 t_permission 表的 Mapper，具体 SQL 在 TPermissionMapper.xml 中 */
    @Resource
    private TPermissionMapper tPermissionMapper;

    /**
     * 获取指定用户登录后左侧应展示的菜单列表（树形：一级目录 + 子菜单）。
     * <p>
     * 返回结构示例：
     * <pre>
     * [
     *   { id:1, name:"系统管理", childPermissionList:[ { name:"用户管理", url:"/dashboard/user" }, ... ] },
     *   { id:2, name:"业务管理", childPermissionList:[ ... ] }
     * ]
     * </pre>
     * DashboardView.vue 会遍历 userInfo.tMenuPermissionList 渲染 el-menu。
     * </p>
     *
     * @param userId 当前登录用户在 t_user 表中的主键 id
     * @return 一级菜单列表，每个一级菜单的 childPermissionList 里放有权限的子项；无权限则返回空列表
     */
    @Override
    public List<TPermission> getMenuPermissionList(Integer userId) {

        // ---------- 第 1 步：从数据库查出该用户「有权限的」所有菜单（扁平列表，尚未组树） ----------
        // SQL 会关联 user → role → role_permission → permission，且 tp.type = 'menu'
        List<TPermission> permittedMenus = tPermissionMapper.selectMenuPermissionsByUserId(userId);
        if (permittedMenus == null || permittedMenus.isEmpty()) {
            // 用户没有任何菜单权限时，左侧不显示菜单项
            return List.of();
        }

        // 用 LinkedHashMap 按菜单 id 去重并保留插入顺序（后面可能补父节点）
        // key = 权限 id，value = 对应的 TPermission 对象
        Map<Integer, TPermission> menuById = new LinkedHashMap<>();
        for (TPermission menu : permittedMenus) {
            menuById.put(menu.getId(), menu);
        }

        // ---------- 第 2 步：若只授权了子菜单、未授权父目录，则补上父节点 ----------
        // 例如：只给了「客户管理」子菜单，没给「业务管理」父级，侧栏仍需要父级标题才能分组展示
        for (TPermission menu : permittedMenus) {
            Integer parentId = menu.getParentId(); // 父权限 id，0 或 null 表示一级菜单
            // 有父节点，且父节点目前还不在 menuById 里（说明用户没被直接授权父菜单）
            if (parentId != null && parentId > 0 && !menuById.containsKey(parentId)) {
                // 再查一次数据库，把父菜单记录取出来
                TPermission parent = tPermissionMapper.selectByPrimaryKey(parentId);
                // 只补充 type 为 menu 的父项（避免误加非菜单类型）
                if (parent != null && "menu".equals(parent.getType())) {
                    menuById.put(parent.getId(), parent);
                }
            }
        }

        // ---------- 第 3 步：筛出一级菜单，并为每个一级菜单挂上「用户有权限」的子菜单 ----------
        // isRootMenu：parentId 为空或 0 的视为根（一级目录）
        List<TPermission> rootMenus = menuById.values().stream()
                .filter(this::isRootMenu)
                .sorted(menuOrderComparator()) // 按 orderNo、id 排序，与库表 order_no 一致
                .collect(Collectors.toList());

        for (TPermission root : rootMenus) {
            // 子菜单只从 permittedMenus 里取（用户真正被授权的子项），不会把未授权兄弟菜单挂上去
            List<TPermission> children = permittedMenus.stream()
                    .filter(m -> Objects.equals(m.getParentId(), root.getId()))
                    .sorted(menuOrderComparator())
                    .collect(Collectors.toList());
            // 设置到实体上的 childPermissionList，供前端 el-sub-menu / el-menu-item 使用
            root.setChildPermissionList(children);
        }

        // ---------- 第 4 步：过滤掉「既没有子菜单、自己也不是可点击叶子」的一级项 ----------
        // 规则：
        // 1）有子菜单的一级 → 保留（正常分组目录）
        // 2）无子菜单但自己在 permittedMenus 里且有 url → 保留（一级就是可点的单页菜单）
        // 3）无子菜单且只是被第 2 步补进来的空壳父级 → 去掉，避免侧栏出现空目录
        return rootMenus.stream()
                .filter(root -> !root.getChildPermissionList().isEmpty()
                        || (permittedMenus.stream().anyMatch(m -> Objects.equals(m.getId(), root.getId()))
                        && StringUtils.hasText(root.getUrl())))
                .collect(Collectors.toList());
    }

    /**
     * 判断一条权限记录是否是一级（根）菜单。
     *
     * @param menu 权限实体
     * @return parentId 为 null 或 0 时返回 true，表示没有上级菜单
     */
    private boolean isRootMenu(TPermission menu) {
        Integer parentId = menu.getParentId();
        return parentId == null || parentId == 0;
    }

    /**
     * 菜单排序比较器：先按 orderNo 升序，再按 id 升序。
     * nullsLast 表示 orderNo 为空的记录排在后面，避免空指针。
     */
    private Comparator<TPermission> menuOrderComparator() {
        return Comparator
                .comparing(TPermission::getOrderNo, Comparator.nullsLast(Integer::compareTo))
                .thenComparing(TPermission::getId, Comparator.nullsLast(Integer::compareTo));
    }
}
