package com.bjpowernode.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 按角色查看：一行角色及其权限
 */
@Data
public class PermManageRoleRow implements Serializable {
    private Integer roleId;
    private String role;
    private String roleName;
    private List<PermManagePermItem> permissions;
}
