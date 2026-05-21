package com.bjpowernode.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 权限管理：角色行中的权限简要信息
 */
@Data
public class PermManagePermItem implements Serializable {
    private Integer permissionId;
    private String name;
    private String url;
}
