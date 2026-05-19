package com.bjpowernode.mapper;

import com.bjpowernode.annotation.DataScope;
import com.bjpowernode.entity.TUser;
import com.bjpowernode.query.BaseQuery;

import java.util.List;

//@Mapper
public interface TUserMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(TUser record);

    int insertSelective(TUser record);

    TUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TUser record);

    int updateByPrimaryKey(TUser record);

    TUser selectByLoginAct(String loginAct);

    //别名tableAlias要与sql中的一致
    @DataScope(tableAlias = "tu",columnName = "id")
    List<TUser> selectByPage(@org.apache.ibatis.annotations.Param("baseQuery") BaseQuery baseQuery,
        @org.apache.ibatis.annotations.Param("search") com.bjpowernode.query.UserSearchQuery search);

    TUser selectById(Integer id);

    int batchDeleteUser(List<String> idList);

    List<TUser> selectOwners();

    List<TUser> selectAllForRoleManage();
}