package com.bjpowernode.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 角色管理：用户行中的角色简要信息
 */
@Data
public class RoleManageRoleItem implements Serializable {
    private Integer roleId;
    private String roleName;
}
