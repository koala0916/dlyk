package com.bjpowernode.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 为用户添加角色弹窗：角色选项行
 */
@Data
public class RoleOptionRow implements Serializable {
    private Integer roleId;
    private String role;
    private String roleName;
    /** 该用户是否已拥有此角色 */
    private Boolean assigned;
}
