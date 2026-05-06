package com.bjpowernode.query;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 接受表单提交的参数
 */
@Data
public class UserQuery {

    private Integer id;

    @NotEmpty(message = "账号不能为空")
    private String loginAct;

    @Size(min = 1, max = 20, message = "密码长度在1-20位之间")
    private String loginPwd;

    //修改使用的密码
    private String loginPwd2;

    private String name;

    private String phone;

    private String email;

    private Integer accountNoExpired;

    private Integer credentialsNoExpired;

    private Integer accountNoLocked;

    private Integer accountEnabled;
}
