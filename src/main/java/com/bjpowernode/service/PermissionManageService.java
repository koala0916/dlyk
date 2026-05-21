package com.bjpowernode.service;

import com.bjpowernode.entity.TPermission;
import com.bjpowernode.query.PermissionQuery;
import com.bjpowernode.query.RoleQuery;
import com.bjpowernode.vo.PermManagePermRow;
import com.bjpowernode.vo.PermManageRoleRow;
import com.bjpowernode.vo.PermOptionRow;
import com.bjpowernode.vo.RoleOptionRow;

import java.util.List;

/**
 * 权限管理：角色-权限关系维护
 */
public interface PermissionManageService {

    List<PermManageRoleRow> listRoleView();

    List<PermManagePermRow> listPermissionView();

    List<PermOptionRow> listPermissionOptionsForRole(Integer roleId);

    List<RoleOptionRow> listRoleOptionsForPermission(Integer permissionId);

    List<TPermission> listParentMenuOptions();

    int addRole(RoleQuery roleQuery);

    int deleteRole(Integer roleId);

    int addPermission(PermissionQuery query);

    /**
     * @return 正数成功；0 失败；-1 存在子权限不可删
     */
    int deletePermission(Integer permissionId);

    int removeRolePermission(Integer roleId, Integer permissionId);

    int addPermissionsToRole(Integer roleId, List<Integer> permissionIds);

    int addRolesToPermission(Integer permissionId, List<Integer> roleIds);
}
