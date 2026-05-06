package com.bjpowernode.service.impl;


import com.bjpowernode.entity.TPermission;
import com.bjpowernode.mapper.TPermissionMapper;
import com.bjpowernode.service.PermissionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private TPermissionMapper  tPermissionMapper;

    @Override
    public List<TPermission> getMenuPermissionList(Integer id) {
        return tPermissionMapper.selectMenuPermissionListById(id);
    }
}
