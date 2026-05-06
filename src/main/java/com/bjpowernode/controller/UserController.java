package com.bjpowernode.controller;

import com.bjpowernode.entity.TPermission;
import com.bjpowernode.entity.TUser;
import com.bjpowernode.query.UserQuery;
import com.bjpowernode.result.Result;
import com.bjpowernode.service.PermissionService;
import com.bjpowernode.service.UserService;
import com.bjpowernode.util.LoginInfoUtil;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private PermissionService permissionService;

    @GetMapping("api/login/user")
    public Result loginUser() {

        //从security中获取登录的用户信息
        TUser currentLoginUser = LoginInfoUtil.getCurrentLoginUser();

        //获取当前登录的用户权限信息
        List<TPermission> permissionList = permissionService.getMenuPermissionList(currentLoginUser.getId());

        //将权限放入到用户对象中
        currentLoginUser.setTMenuPermissionList(permissionList);

        //将登录的用户信息返回给前端
        return Result.OK(currentLoginUser);
    }

    /**
     * 分页查询用户信息
     *
     * @param current 当前页码
     */
    @GetMapping("api/users")
    public Result users(Integer current) {
        PageInfo<TUser> pageInfo = userService.getUserByPage(current);

        return Result.OK(pageInfo);
    }

    /**
     * 查看用户明细信息
     */
    @GetMapping("api/user/{id}")
    public Result getUserById(@PathVariable("id") Integer id) {
        TUser tUser = userService.getUserById(id);

        return Result.OK(tUser);
    }


    /**
     * 用户添加
     *
     * @Valid 对参数里面的数据进行校验
     */
    @PreAuthorize("hasAnyAuthority('user:add')")
    @PostMapping("api/user")
    public Result addUser(@Valid UserQuery userQuery) {
        int flag = userService.addUser(userQuery);

        if (flag > 0) {
            return Result.OK();
        } else {
            return Result.FAIL();
        }
    }

    /**
     * 用户修改
     */
    @PutMapping("api/user")
    public Result editUser(UserQuery userQuery) {
        int flag = userService.editUser(userQuery);
        if (flag > 0) {
            return Result.OK();
        } else {
            return Result.FAIL();
        }
    }

    /**
     * 用户删除
     */
    @DeleteMapping("api/user/{id}")
    public Result deleteUser(@PathVariable("id") Integer id) {
        int flag = userService.deleteUser(id);
        if (flag > 0) {
            return Result.OK();
        } else {
            return Result.FAIL();
        }
    }

    /**
     * 批量删除
     */
    @DeleteMapping("api/user")
    public Result batchDeleteUser(String ids) {
        List<String> idList = Arrays.asList(ids.split(","));

        int flag = userService.batchDeleteUser(idList);
        if (flag > 0) {
            return Result.OK();
        } else {
            return Result.FAIL();
        }
    }


    /**
     * 查询全部的用户信息
     */
    @GetMapping("api/owners")
    public Result owners() {
        List<TUser> tUserList = userService.getOwners();

        return Result.OK(tUserList);
    }
}
