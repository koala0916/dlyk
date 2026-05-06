package com.bjpowernode.service;

import com.bjpowernode.entity.TActivityRemark;
import com.bjpowernode.query.ActivityRemarkQuery;
import com.github.pagehelper.PageInfo;

public interface ActivityRemarkService {
    int saveActivityRemark(ActivityRemarkQuery activityRemarkQuery);

    PageInfo<TActivityRemark> selectActivityRemarkByPage(Integer current, Integer activityId);
}
