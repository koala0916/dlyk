package com.bjpowernode.service.impl;

import com.bjpowernode.entity.TPermission;
import com.bjpowernode.entity.TRole;
import com.bjpowernode.entity.TRolePermission;
import com.bjpowernode.mapper.TPermissionMapper;
import com.bjpowernode.mapper.TRoleMapper;
import com.bjpowernode.mapper.TRolePermissionMapper;
import com.bjpowernode.mapper.TUserRoleMapper;
import com.bjpowernode.query.PermissionQuery;
import com.bjpowernode.query.RoleQuery;
import com.bjpowernode.service.PermissionManageService;
import com.bjpowernode.vo.PermManagePermItem;
import com.bjpowernode.vo.PermManagePermRow;
import com.bjpowernode.vo.PermManageRoleItem;
import com.bjpowernode.vo.PermManageRoleRow;
import com.bjpowernode.vo.PermOptionRow;
import com.bjpowernode.vo.RoleOptionRow;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionManageServiceImpl implements PermissionManageService {

    @Resource
    private TRoleMapper tRoleMapper;

    @Resource
    private TPermissionMapper tPermissionMapper;

    @Resource
    private TRolePermissionMapper tRolePermissionMapper;

    @Resource
    private TUserRoleMapper tUserRoleMapper;

    @Override
    public List<PermManageRoleRow> listRoleView() {
        List<TRole> roles = tRoleMapper.selectAll();
        List<PermManageRoleRow> rows = new ArrayList<>();
        if (roles == null) {
            return rows;
        }
        for (TRole role : roles) {
            PermManageRoleRow row = new PermManageRoleRow();
            row.setRoleId(role.getId());
            row.setRole(role.getRole());
            row.setRoleName(role.getRoleName());
            List<PermManagePermItem> perms = tPermissionMapper.selectPermItemsByRoleId(role.getId());
            row.setPermissions(perms != null ? perms : new ArrayList<>());
            rows.add(row);
        }
        return rows;
    }

    @Override
    public List<PermManagePermRow> listPermissionView() {
        List<TPermission> permissions = tPermissionMapper.selectAllForManage();
        List<PermManagePermRow> rows = new ArrayList<>();
        if (permissions == null) {
            return rows;
        }
        for (TPermission perm : permissions) {
            PermManagePermRow row = new PermManagePermRow();
            row.setPermissionId(perm.getId());
            row.setName(perm.getName());
            row.setCode(perm.getCode());
            row.setUrl(perm.getUrl());
            row.setType(perm.getType());
            List<PermManageRoleItem> roleItems = tRolePermissionMapper.selectRoleItemsByPermissionId(perm.getId());
            row.setRoles(roleItems != null ? roleItems : new ArrayList<>());
            rows.add(row);
        }
        return rows;
    }

    @Override
    public List<PermOptionRow> listPermissionOptionsForRole(Integer roleId) {
        if (roleId == null) {
            return List.of();
        }
        List<TPermission> all = tPermissionMapper.selectAllForManage();
        List<PermManagePermItem> assigned = tPermissionMapper.selectPermItemsByRoleId(roleId);
        List<Integer> assignedIds = assigned == null ? List.of()
                : assigned.stream().map(PermManagePermItem::getPermissionId).collect(Collectors.toList());

        List<PermOptionRow> options = new ArrayList<>();
        if (all == null) {
            return options;
        }
        for (TPermission perm : all) {
            PermOptionRow row = new PermOptionRow();
            row.setPermissionId(perm.getId());
            row.setName(perm.getName());
            row.setCode(perm.getCode());
            row.setUrl(perm.getUrl());
            row.setType(perm.getType());
            row.setAssigned(assignedIds.contains(perm.getId()));
            options.add(row);
        }
        return options;
    }

    @Override
    public List<RoleOptionRow> listRoleOptionsForPermission(Integer permissionId) {
        if (permissionId == null) {
            return List.of();
        }
        List<TRole> allRoles = tRoleMapper.selectAll();
        List<PermManageRoleItem> assigned = tRolePermissionMapper.selectRoleItemsByPermissionId(permissionId);
        List<Integer> assignedIds = assigned == null ? List.of()
                : assigned.stream().map(PermManageRoleItem::getRoleId).collect(Collectors.toList());

        List<RoleOptionRow> options = new ArrayList<>();
        if (allRoles == null) {
            return options;
        }
        for (TRole role : allRoles) {
            RoleOptionRow row = new RoleOptionRow();
            row.setRoleId(role.getId());
            row.setRole(role.getRole());
            row.setRoleName(role.getRoleName());
            row.setAssigned(assignedIds.contains(role.getId()));
            options.add(row);
        }
        return options;
    }

    @Override
    public List<TPermission> listParentMenuOptions() {
        List<TPermission> all = tPermissionMapper.selectAllForManage();
        if (all == null) {
            return List.of();
        }
        return all.stream()
                .filter(p -> "M".equalsIgnoreCase(p.getType())
                        || p.getParentId() == null || p.getParentId() == 0)
                .collect(Collectors.toList());
    }

    @Override
    public int addRole(RoleQuery roleQuery) {
        if (roleQuery == null || !StringUtils.hasText(roleQuery.getRole())
                || !StringUtils.hasText(roleQuery.getRoleName())) {
            return 0;
        }
        TRole role = new TRole();
        role.setRole(roleQuery.getRole().trim());
        role.setRoleName(roleQuery.getRoleName().trim());
        return tRoleMapper.insertSelective(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteRole(Integer roleId) {
        if (roleId == null) {
            return 0;
        }
        if (tRoleMapper.selectByPrimaryKey(roleId) == null) {
            return 0;
        }
        tUserRoleMapper.deleteByRoleId(roleId);
        tRolePermissionMapper.deleteByRoleId(roleId);
        return tRoleMapper.deleteByPrimaryKey(roleId);
    }

    @Override
    public int addPermission(PermissionQuery query) {
        if (query == null || !StringUtils.hasText(query.getName())
                || !StringUtils.hasText(query.getCode())) {
            return 0;
        }
        String type = StringUtils.hasText(query.getType()) ? query.getType().trim() : "menu";
        if (!"menu".equalsIgnoreCase(type) && !"M".equalsIgnoreCase(type)) {
            type = "menu";
        }
        TPermission perm = new TPermission();
        perm.setName(query.getName().trim());
        perm.setCode(query.getCode().trim());
        perm.setUrl(StringUtils.hasText(query.getUrl()) ? query.getUrl().trim() : "");
        perm.setType(type);
        Integer parentId = query.getParentId();
        if (parentId != null && parentId <= 0) {
            parentId = 0;
        }
        perm.setParentId(parentId != null ? parentId : 0);
        perm.setOrderNo(query.getOrderNo() != null ? query.getOrderNo() : 99);
        perm.setIcon(StringUtils.hasText(query.getIcon()) ? query.getIcon().trim() : "Menu");
        perm.setComponent("");
        return tPermissionMapper.insertSelective(perm);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deletePermission(Integer permissionId) {
        if (permissionId == null) {
            return 0;
        }
        if (tPermissionMapper.selectByPrimaryKey(permissionId) == null) {
            return 0;
        }
        if (tPermissionMapper.countByParentId(permissionId) > 0) {
            return -1;
        }
        tRolePermissionMapper.deleteByPermissionId(permissionId);
        return tPermissionMapper.deleteByPrimaryKey(permissionId) > 0 ? 1 : 0;
    }

    @Override
    public int removeRolePermission(Integer roleId, Integer permissionId) {
        if (roleId == null || permissionId == null) {
            return 0;
        }
        return tRolePermissionMapper.deleteByRoleIdAndPermissionId(roleId, permissionId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addPermissionsToRole(Integer roleId, List<Integer> permissionIds) {
        if (roleId == null || permissionIds == null || permissionIds.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (Integer permissionId : permissionIds) {
            if (permissionId == null) {
                continue;
            }
            if (tRolePermissionMapper.countByRoleIdAndPermissionId(roleId, permissionId) > 0) {
                continue;
            }
            TRolePermission rel = new TRolePermission();
            rel.setRoleId(roleId);
            rel.setPermissionId(permissionId);
            count += tRolePermissionMapper.insertSelective(rel);
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addRolesToPermission(Integer permissionId, List<Integer> roleIds) {
        if (permissionId == null || roleIds == null || roleIds.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (Integer roleId : roleIds) {
            if (roleId == null) {
                continue;
            }
            if (tRolePermissionMapper.countByRoleIdAndPermissionId(roleId, permissionId) > 0) {
                continue;
            }
            TRolePermission rel = new TRolePermission();
            rel.setRoleId(roleId);
            rel.setPermissionId(permissionId);
            count += tRolePermissionMapper.insertSelective(rel);
        }
        return count;
    }
}
