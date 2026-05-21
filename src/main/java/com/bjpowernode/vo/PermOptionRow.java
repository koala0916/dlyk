package com.bjpowernode.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 为角色添加权限弹窗：权限选项行
 */
@Data
public class PermOptionRow implements Serializable {
    private Integer permissionId;
    private String name;
    private String code;
    private String url;
    private String type;
    /** 该角色是否已拥有此权限 */
    private Boolean assigned;
}
