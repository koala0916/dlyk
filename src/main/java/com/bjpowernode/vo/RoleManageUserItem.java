package com.bjpowernode.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 角色管理：角色行中的用户简要信息
 */
@Data
public class RoleManageUserItem implements Serializable {
    private Integer userId;
    private String name;
}
