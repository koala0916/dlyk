package com.bjpowernode.query;

import lombok.Data;

/**
 * 添加权限表单参数
 */
@Data
public class PermissionQuery {
    private String name;
    private String code;
    private String url;
    /** menu=子菜单，M=目录 */
    private String type;
    private Integer parentId;
    private Integer orderNo;
    private String icon;
}
