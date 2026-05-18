package com.bjpowernode.controller;

import com.bjpowernode.query.RoleQuery;
import com.bjpowernode.result.Result;
import com.bjpowernode.service.RoleManageService;
import jakarta.annotation.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色管理：用户-角色关系维护
 */
@RestController
public class RoleManageController {

    @Resource
    private RoleManageService roleManageService;

    /** 按角色查看列表 */
    @GetMapping("api/role/manage/roles")
    public Result roleView() {
        return Result.OK(roleManageService.listRoleView());
    }

    /** 按用户查看列表 */
    @GetMapping("api/role/manage/users")
    public Result userView() {
        return Result.OK(roleManageService.listUserView());
    }

    /** 为某角色添加用户：弹窗用户选项 */
    @GetMapping("api/role/manage/role/{roleId}/user-options")
    public Result userOptionsForRole(@PathVariable("roleId") Integer roleId) {
        return Result.OK(roleManageService.listUserOptionsForRole(roleId));
    }

    /** 为某用户添加角色：弹窗角色选项 */
    @GetMapping("api/role/manage/user/{userId}/role-options")
    public Result roleOptionsForUser(@PathVariable("userId") Integer userId) {
        return Result.OK(roleManageService.listRoleOptionsForUser(userId));
    }

    /** 添加角色 */
    @PostMapping("api/role")
    public Result addRole(RoleQuery roleQuery) {
        int flag = roleManageService.addRole(roleQuery);
        if (flag > 0) {
            return Result.OK("添加角色成功");
        }
        return Result.FAIL("添加角色失败，请填写角色编码和角色名称");
    }

    /** 删除角色（同时删除 t_user_role、t_role_permission，不删用户表） */
    @DeleteMapping("api/role/{id}")
    public Result deleteRole(@PathVariable("id") Integer id) {
        int flag = roleManageService.deleteRole(id);
        if (flag > 0) {
            return Result.OK("删除角色成功");
        }
        return Result.FAIL("删除角色失败，角色可能不存在");
    }

    /** 删除用户-角色关系（仅删 t_user_role） */
    @DeleteMapping("api/user-role")
    public Result removeUserRole(@RequestParam("userId") Integer userId,
                                 @RequestParam("roleId") Integer roleId) {
        int flag = roleManageService.removeUserRole(userId, roleId);
        if (flag > 0) {
            return Result.OK("删除成功");
        }
        return Result.FAIL("删除失败，关系可能不存在");
    }

    /** 为角色批量添加用户，userIds 逗号分隔 */
    @PostMapping("api/user-role/add-to-role")
    public Result addUsersToRole(@RequestParam("roleId") Integer roleId,
                                 @RequestParam("userIds") String userIds) {
        List<Integer> idList = parseIdList(userIds);
        if (idList.isEmpty()) {
            return Result.FAIL("请至少选择一个用户");
        }
        int flag = roleManageService.addUsersToRole(roleId, idList);
        if (flag > 0) {
            return Result.OK("添加成功，共添加 " + flag + " 条关系");
        }
        return Result.FAIL("添加失败，所选用户可能均已拥有该角色");
    }

    /** 为用户批量添加角色，roleIds 逗号分隔 */
    @PostMapping("api/user-role/add-to-user")
    public Result addRolesToUser(@RequestParam("userId") Integer userId,
                                 @RequestParam("roleIds") String roleIds) {
        List<Integer> idList = parseIdList(roleIds);
        if (idList.isEmpty()) {
            return Result.FAIL("请至少选择一个角色");
        }
        int flag = roleManageService.addRolesToUser(userId, idList);
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
