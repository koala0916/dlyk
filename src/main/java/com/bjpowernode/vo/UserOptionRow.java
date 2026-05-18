package com.bjpowernode.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 为角色添加用户弹窗：用户选项行
 */
@Data
public class UserOptionRow implements Serializable {
    private Integer userId;
    private String loginAct;
    private String name;
    /** 是否已拥有当前角色（已拥有则勾选框禁用） */
    private Boolean assigned;
}
