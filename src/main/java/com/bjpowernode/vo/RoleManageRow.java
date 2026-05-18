package com.bjpowernode.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 按角色查看：一行角色及其成员
 */
@Data
public class RoleManageRow implements Serializable {
    private Integer roleId;
    private String role;
    private String roleName;
    private List<RoleManageUserItem> users;
}
