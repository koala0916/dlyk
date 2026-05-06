package com.bjpowernode.controller;

import com.bjpowernode.entity.TActivityRemark;
import com.bjpowernode.query.ActivityRemarkQuery;
import com.bjpowernode.result.Result;
import com.bjpowernode.service.ActivityRemarkService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 活动备注
 */
@RestController
public class ActivityRemarkController {

    @Resource
    private ActivityRemarkService activityRemarkService;

    @PostMapping("/api/activity/remark")
    public Result addActivityRemark(@RequestBody ActivityRemarkQuery activityRemarkQuery){
        int flag = activityRemarkService.saveActivityRemark(activityRemarkQuery);
        return flag > 0 ? Result.OK() : Result.FAIL();
    }


    /**
     * 分页查询活动备注信息
     */
    @GetMapping("/api/activity/remark")
    public Result getActivityRemarkByPage(Integer current, Integer activityId){
        PageInfo<TActivityRemark> pageInfo = activityRemarkService.selectActivityRemarkByPage(current,activityId);

        return Result.OK(pageInfo);
    }
}
