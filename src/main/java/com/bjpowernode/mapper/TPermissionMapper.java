package com.bjpowernode.mapper;

import com.bjpowernode.entity.TPermission;

import java.util.List;

public interface TPermissionMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(TPermission record);

    int insertSelective(TPermission record);

    TPermission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TPermission record);

    int updateByPrimaryKey(TPermission record);

    List<TPermission> selectByUserId(Integer userId);

    List<TPermission> selectMenuPermissionListById(Integer id);

    /**
     * 按 RBAC 查询当前用户拥有权限的全部菜单（扁平列表，不含未授权的菜单）
     */
    List<TPermission> selectMenuPermissionsByUserId(Integer userId);

    /** 权限管理：可分配的菜单/目录权限 */
    List<TPermission> selectAllForManage();

    int countByParentId(Integer parentId);

    List<com.bjpowernode.vo.PermManagePermItem> selectPermItemsByRoleId(Integer roleId);
}