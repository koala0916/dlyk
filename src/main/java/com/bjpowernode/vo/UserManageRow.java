package com.bjpowernode.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 按用户查看：一行用户及其角色
 */
@Data
public class UserManageRow implements Serializable {
    private Integer userId;
    private String loginAct;
    private String name;
    private List<RoleManageRoleItem> roles;
}
