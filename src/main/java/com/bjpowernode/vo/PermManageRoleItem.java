package com.bjpowernode.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 权限管理：权限行中的角色简要信息
 */
@Data
public class PermManageRoleItem implements Serializable {
    private Integer roleId;
    private String roleName;
}
