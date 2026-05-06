package com.bjpowernode.service.impl;

import com.bjpowernode.constant.Constant;
import com.bjpowernode.entity.TActivity;
import com.bjpowernode.mapper.TActivityMapper;
import com.bjpowernode.query.ActivityQuery;
import com.bjpowernode.query.BaseQuery;
import com.bjpowernode.service.ActivityService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Resource
    private TActivityMapper tActivityMapper;


    /**
     * 分页查询
     *
     * @param current
     * @param activityQuery
     * @return
     */
    @Override
    public PageInfo<TActivity> getActivityByPage(Integer current, ActivityQuery activityQuery) {
        //1.设置查询第几页，每页查多少条数据
        PageHelper.startPage(current, Constant.PAGE_SIZE);
        //2.调用mapper查询活动列表
        List<TActivity> tActivityList = tActivityMapper.selectByPage(new BaseQuery(),activityQuery);
        //3.创建PageInfo对象，封装查询结果
        PageInfo<TActivity> pageInfo = new PageInfo<>(tActivityList);

        return pageInfo;
    }

    @Override
    public TActivity getActivityById(Integer activityId) {
        return tActivityMapper.selectActivityById(activityId);
    }
}
