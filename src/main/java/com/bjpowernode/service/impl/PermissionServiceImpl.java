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

@Service
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private TPermissionMapper tPermissionMapper;

    @Override
    public List<TPermission> getMenuPermissionList(Integer userId) {
        // 1. 扁平查询：仅包含 t_role_permission 中授权给该用户的 menu
        List<TPermission> permittedMenus = tPermissionMapper.selectMenuPermissionsByUserId(userId);
        if (permittedMenus == null || permittedMenus.isEmpty()) {
            return List.of();
        }

        Map<Integer, TPermission> menuById = new LinkedHashMap<>();
        for (TPermission menu : permittedMenus) {
            menuById.put(menu.getId(), menu);
        }

        // 2. 子菜单有权限但父菜单未授权时，补充父节点用于侧栏分组展示（子项仍仅显示有权限的）
        for (TPermission menu : permittedMenus) {
            Integer parentId = menu.getParentId();
            if (parentId != null && parentId > 0 && !menuById.containsKey(parentId)) {
                TPermission parent = tPermissionMapper.selectByPrimaryKey(parentId);
                if (parent != null && "menu".equals(parent.getType())) {
                    menuById.put(parent.getId(), parent);
                }
            }
        }

        // 3. 组装一级菜单，并只挂载用户有权限的子菜单
        List<TPermission> rootMenus = menuById.values().stream()
                .filter(this::isRootMenu)
                .sorted(menuOrderComparator())
                .collect(Collectors.toList());

        for (TPermission root : rootMenus) {
            List<TPermission> children = permittedMenus.stream()
                    .filter(m -> Objects.equals(m.getParentId(), root.getId()))
                    .sorted(menuOrderComparator())
                    .collect(Collectors.toList());
            root.setChildPermissionList(children);
        }

        // 4. 无子项的一级目录不展示；有 url 的叶子一级菜单保留
        return rootMenus.stream()
                .filter(root -> !root.getChildPermissionList().isEmpty()
                        || (permittedMenus.stream().anyMatch(m -> Objects.equals(m.getId(), root.getId()))
                        && StringUtils.hasText(root.getUrl())))
                .collect(Collectors.toList());
    }

    private boolean isRootMenu(TPermission menu) {
        Integer parentId = menu.getParentId();
        return parentId == null || parentId == 0;
    }

    private Comparator<TPermission> menuOrderComparator() {
        return Comparator
                .comparing(TPermission::getOrderNo, Comparator.nullsLast(Integer::compareTo))
                .thenComparing(TPermission::getId, Comparator.nullsLast(Integer::compareTo));
    }
}
