package com.bjpowernode.mapper;

import com.bjpowernode.annotation.DataScope;
import com.bjpowernode.entity.TActivity;
import com.bjpowernode.query.ActivityQuery;
import com.bjpowernode.query.BaseQuery;

import java.util.List;

public interface TActivityMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(TActivity record);

    int insertSelective(TActivity record);

    TActivity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TActivity record);

    int updateByPrimaryKey(TActivity record);

    //  数据权限控制
    @DataScope(tableAlias = "ta",columnName = "owner_id")
    List<TActivity> selectByPage(BaseQuery baseQuery, ActivityQuery activityQuery);

    TActivity selectActivityById(Integer activityId);

    Integer selectGoingActivityCount();

    Integer selectTotalActivityCount();
}