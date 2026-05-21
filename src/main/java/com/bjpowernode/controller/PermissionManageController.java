package com.bjpowernode.controller;

import com.bjpowernode.query.PermissionQuery;
import com.bjpowernode.query.RoleQuery;
import com.bjpowernode.result.Result;
import com.bjpowernode.service.PermissionManageService;
import jakarta.annotation.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限管理：角色-权限关系维护
 */
@RestController
public class PermissionManageController {

    @Resource
    private PermissionManageService permissionManageService;

    @GetMapping("api/permission/manage/roles")
    public Result roleView() {
        return Result.OK(permissionManageService.listRoleView());
    }

    @GetMapping("api/permission/manage/permissions")
    public Result permissionView() {
        return Result.OK(permissionManageService.listPermissionView());
    }

    @GetMapping("api/permission/manage/parent-options")
    public Result parentMenuOptions() {
        return Result.OK(permissionManageService.listParentMenuOptions());
    }

    @GetMapping("api/permission/manage/role/{roleId}/permission-options")
    public Result permissionOptionsForRole(@PathVariable("roleId") Integer roleId) {
        return Result.OK(permissionManageService.listPermissionOptionsForRole(roleId));
    }

    @GetMapping("api/permission/manage/permission/{permissionId}/role-options")
    public Result roleOptionsForPermission(@PathVariable("permissionId") Integer permissionId) {
        return Result.OK(permissionManageService.listRoleOptionsForPermission(permissionId));
    }

    @PostMapping("api/permission/manage/role")
    public Result addRole(RoleQuery roleQuery) {
        int flag = permissionManageService.addRole(roleQuery);
        if (flag > 0) {
            return Result.OK("添加角色成功");
        }
        return Result.FAIL("添加角色失败，请填写角色编码和角色名称");
    }

    @DeleteMapping("api/permission/manage/role/{id}")
    public Result deleteRole(@PathVariable("id") Integer id) {
        int flag = permissionManageService.deleteRole(id);
        if (flag > 0) {
            return Result.OK("删除角色成功");
        }
        return Result.FAIL("删除角色失败，角色可能不存在");
    }

    @PostMapping("api/permission")
    public Result addPermission(PermissionQuery query) {
        int flag = permissionManageService.addPermission(query);
        if (flag > 0) {
            return Result.OK("添加权限成功");
        }
        return Result.FAIL("添加权限失败，请填写权限名称和编码");
    }

    @DeleteMapping("api/permission/{id}")
    public Result deletePermission(@PathVariable("id") Integer id) {
        int flag = permissionManageService.deletePermission(id);
        if (flag > 0) {
            return Result.OK("删除权限成功");
        }
        if (flag < 0) {
            return Result.FAIL("该权限下仍有子菜单，请先删除子菜单后再删");
        }
        return Result.FAIL("删除权限失败，权限可能不存在");
    }

    @DeleteMapping("api/role-permission")
    public Result removeRolePermission(@RequestParam("roleId") Integer roleId,
                                       @RequestParam("permissionId") Integer permissionId) {
        int flag = permissionManageService.removeRolePermission(roleId, permissionId);
        if (flag > 0) {
            return Result.OK("删除成功");
        }
        return Result.FAIL("删除失败，关系可能不存在");
    }

    @PostMapping("api/role-permission/add-to-role")
    public Result addPermissionsToRole(@RequestParam("roleId") Integer roleId,
                                       @RequestParam("permissionIds") String permissionIds) {
        List<Integer> idList = parseIdList(permissionIds);
        if (idList.isEmpty()) {
            return Result.FAIL("请至少选择一个权限");
        }
        int flag = permissionManageService.addPermissionsToRole(roleId, idList);
        if (flag > 0) {
            return Result.OK("添加成功，共添加 " + flag + " 条关系");
        }
        return Result.FAIL("添加失败，所选权限可能均已拥有");
    }

    @PostMapping("api/role-permission/add-to-permission")
    public Result addRolesToPermission(@RequestParam("permissionId") Integer permissionId,
                                       @RequestParam("roleIds") String roleIds) {
        List<Integer> idList = parseIdList(roleIds);
        if (idList.isEmpty()) {
            return Result.FAIL("请至少选择一个角色");
        }
        int flag = permissionManageService.addRolesToPermission(permissionId, idList);
        if (flag > 0) {
            return Result.OK("添加成功，共添加 " + flag + " 条关系");
        }
        return Result.FAIL("添加失败，所选角色可能均已拥有");
    }

    private List<Integer> parseIdList(String ids) {
        if (!StringUtils.hasText(ids)) {
            return List.of();
        }
        try {
            return Arrays.stream(ids.split(","))
                    .map(String::trim)
                    .filter(StringUtils::hasText)
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            return List.of();
        }
    }
}
