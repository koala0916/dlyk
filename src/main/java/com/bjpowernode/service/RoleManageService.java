package com.bjpowernode.service;

import com.bjpowernode.query.RoleQuery;
import com.bjpowernode.vo.RoleManageRow;
import com.bjpowernode.vo.RoleOptionRow;
import com.bjpowernode.vo.UserManageRow;
import com.bjpowernode.vo.UserOptionRow;

import java.util.List;

public interface RoleManageService {

    List<RoleManageRow> listRoleView();

    List<UserManageRow> listUserView();

    List<UserOptionRow> listUserOptionsForRole(Integer roleId);

    List<RoleOptionRow> listRoleOptionsForUser(Integer userId);

    int addRole(RoleQuery roleQuery);

    int removeUserRole(Integer userId, Integer roleId);

    int addUsersToRole(Integer roleId, List<Integer> userIds);

    int addRolesToUser(Integer userId, List<Integer> roleIds);

    int deleteRole(Integer roleId);
}
