package com.bjpowernode.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 按权限查看：一行权限及其角色
 */
@Data
public class PermManagePermRow implements Serializable {
    private Integer permissionId;
    private String name;
    private String code;
    private String url;
    private String type;
    private List<PermManageRoleItem> roles;
}
