package com.bjpowernode.mapper;

import com.bjpowernode.entity.TUserRole;
import com.bjpowernode.vo.RoleManageUserItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TUserRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TUserRole record);

    int insertSelective(TUserRole record);

    TUserRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TUserRole record);

    int updateByPrimaryKey(TUserRole record);

    int deleteByUserIdAndRoleId(@Param("userId") Integer userId, @Param("roleId") Integer roleId);

    int countByUserIdAndRoleId(@Param("userId") Integer userId, @Param("roleId") Integer roleId);

    List<RoleManageUserItem> selectUsersByRoleId(@Param("roleId") Integer roleId);

    int deleteByRoleId(@Param("roleId") Integer roleId);
}