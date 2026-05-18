package com.bjpowernode.service.impl;

import com.bjpowernode.entity.TRole;
import com.bjpowernode.entity.TUser;
import com.bjpowernode.entity.TUserRole;
import com.bjpowernode.mapper.TRoleMapper;
import com.bjpowernode.mapper.TRolePermissionMapper;
import com.bjpowernode.mapper.TUserMapper;
import com.bjpowernode.mapper.TUserRoleMapper;
import com.bjpowernode.query.RoleQuery;
import com.bjpowernode.service.RoleManageService;
import com.bjpowernode.vo.RoleManageRoleItem;
import com.bjpowernode.vo.RoleManageRow;
import com.bjpowernode.vo.RoleManageUserItem;
import com.bjpowernode.vo.RoleOptionRow;
import com.bjpowernode.vo.UserManageRow;
import com.bjpowernode.vo.UserOptionRow;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleManageServiceImpl implements RoleManageService {

    @Resource
    private TRoleMapper tRoleMapper;

    @Resource
    private TUserMapper tUserMapper;

    @Resource
    private TUserRoleMapper tUserRoleMapper;

    @Resource
    private TRolePermissionMapper tRolePermissionMapper;

    @Override
    public List<RoleManageRow> listRoleView() {
        List<TRole> roles = tRoleMapper.selectAll();
        List<RoleManageRow> rows = new ArrayList<>();
        if (roles == null) {
            return rows;
        }
        for (TRole role : roles) {
            RoleManageRow row = new RoleManageRow();
            row.setRoleId(role.getId());
            row.setRole(role.getRole());
            row.setRoleName(role.getRoleName());
            List<RoleManageUserItem> users = tUserRoleMapper.selectUsersByRoleId(role.getId());
            row.setUsers(users != null ? users : new ArrayList<>());
            rows.add(row);
        }
        return rows;
    }

    @Override
    public List<UserManageRow> listUserView() {
        List<TUser> users = tUserMapper.selectAllForRoleManage();
        List<UserManageRow> rows = new ArrayList<>();
        if (users == null) {
            return rows;
        }
        for (TUser user : users) {
            UserManageRow row = new UserManageRow();
            row.setUserId(user.getId());
            row.setLoginAct(user.getLoginAct());
            row.setName(user.getName());
            List<TRole> roles = tRoleMapper.selectByUserId(user.getId());
            List<RoleManageRoleItem> roleItems = new ArrayList<>();
            if (roles != null) {
                for (TRole role : roles) {
                    RoleManageRoleItem item = new RoleManageRoleItem();
                    item.setRoleId(role.getId());
                    item.setRoleName(role.getRoleName());
                    roleItems.add(item);
                }
            }
            row.setRoles(roleItems);
            rows.add(row);
        }
        return rows;
    }

    @Override
    public List<UserOptionRow> listUserOptionsForRole(Integer roleId) {
        if (roleId == null) {
            return List.of();
        }
        List<TUser> users = tUserMapper.selectAllForRoleManage();
        List<RoleManageUserItem> assigned = tUserRoleMapper.selectUsersByRoleId(roleId);
        List<Integer> assignedIds = assigned == null ? List.of()
                : assigned.stream().map(RoleManageUserItem::getUserId).collect(Collectors.toList());

        List<UserOptionRow> options = new ArrayList<>();
        if (users == null) {
            return options;
        }
        for (TUser user : users) {
            UserOptionRow row = new UserOptionRow();
            row.setUserId(user.getId());
            row.setLoginAct(user.getLoginAct());
            row.setName(user.getName());
            row.setAssigned(assignedIds.contains(user.getId()));
            options.add(row);
        }
        return options;
    }

    @Override
    public List<RoleOptionRow> listRoleOptionsForUser(Integer userId) {
        if (userId == null) {
            return List.of();
        }
        List<TRole> allRoles = tRoleMapper.selectAll();
        List<TRole> userRoles = tRoleMapper.selectByUserId(userId);
        List<Integer> assignedIds = userRoles == null ? List.of()
                : userRoles.stream().map(TRole::getId).collect(Collectors.toList());

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
    public int removeUserRole(Integer userId, Integer roleId) {
        if (userId == null || roleId == null) {
            return 0;
        }
        return tUserRoleMapper.deleteByUserIdAndRoleId(userId, roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addUsersToRole(Integer roleId, List<Integer> userIds) {
        if (roleId == null || userIds == null || userIds.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (Integer userId : userIds) {
            if (userId == null) {
                continue;
            }
            if (tUserRoleMapper.countByUserIdAndRoleId(userId, roleId) > 0) {
                continue;
            }
            TUserRole rel = new TUserRole();
            rel.setUserId(userId);
            rel.setRoleId(roleId);
            count += tUserRoleMapper.insertSelective(rel);
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addRolesToUser(Integer userId, List<Integer> roleIds) {
        if (userId == null || roleIds == null || roleIds.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (Integer roleId : roleIds) {
            if (roleId == null) {
                continue;
            }
            if (tUserRoleMapper.countByUserIdAndRoleId(userId, roleId) > 0) {
                continue;
            }
            TUserRole rel = new TUserRole();
            rel.setUserId(userId);
            rel.setRoleId(roleId);
            count += tUserRoleMapper.insertSelective(rel);
        }
        return count;
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
}
