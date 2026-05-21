package com.bjpowernode.mapper;

import com.bjpowernode.entity.TRolePermission;
import com.bjpowernode.vo.PermManageRoleItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TRolePermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TRolePermission record);

    int insertSelective(TRolePermission record);

    TRolePermission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TRolePermission record);

    int updateByPrimaryKey(TRolePermission record);

    int deleteByRoleId(Integer roleId);

    int deleteByPermissionId(Integer permissionId);

    int deleteByRoleIdAndPermissionId(@Param("roleId") Integer roleId,
                                      @Param("permissionId") Integer permissionId);

    int countByRoleIdAndPermissionId(@Param("roleId") Integer roleId,
                                     @Param("permissionId") Integer permissionId);

    List<PermManageRoleItem> selectRoleItemsByPermissionId(Integer permissionId);
}