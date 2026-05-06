package com.bjpowernode.service;

import com.bjpowernode.entity.TActivity;
import com.bjpowernode.query.ActivityQuery;
import com.github.pagehelper.PageInfo;

public interface ActivityService {
    PageInfo<TActivity> getActivityByPage(Integer current, ActivityQuery activityQuery);

    TActivity getActivityById(Integer activityId);
}
