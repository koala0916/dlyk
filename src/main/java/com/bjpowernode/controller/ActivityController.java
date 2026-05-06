package com.bjpowernode.controller;


import com.bjpowernode.entity.TActivity;
import com.bjpowernode.query.ActivityQuery;
import com.bjpowernode.result.Result;
import com.bjpowernode.service.ActivityService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 活动controller
 */
@RestController
public class ActivityController {

    @Resource
    private ActivityService activityService;


    /**
     * 分页查询活动
     * @return
     */
    @GetMapping("api/activities")
    public Result activities(Integer current,  ActivityQuery activityQuery) {
        PageInfo<TActivity> pageInfo = activityService.getActivityByPage(current,activityQuery);

        return Result.OK(pageInfo);
    }


    /**
     * 查询活动明细
     */
    @GetMapping("api/activity/{id}")
    public Result queryActivityById(@PathVariable("id") Integer activityId) {
        TActivity tActivity = activityService.getActivityById(activityId);

        return Result.OK(tActivity);
    }
}
