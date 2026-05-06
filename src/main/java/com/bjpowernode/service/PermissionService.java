package com.bjpowernode.service;

import com.bjpowernode.entity.TPermission;

import java.util.List;

public interface PermissionService {
    List<TPermission> getMenuPermissionList(Integer id);
}
