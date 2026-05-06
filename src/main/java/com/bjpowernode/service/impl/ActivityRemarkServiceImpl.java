package com.bjpowernode.service.impl;

import com.bjpowernode.constant.Constant;
import com.bjpowernode.entity.TActivityRemark;
import com.bjpowernode.mapper.TActivityRemarkMapper;
import com.bjpowernode.query.ActivityRemarkQuery;
import com.bjpowernode.service.ActivityRemarkService;
import com.bjpowernode.util.LoginInfoUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ActivityRemarkServiceImpl implements ActivityRemarkService {

    @Resource
    private TActivityRemarkMapper tActivityRemarkMapper;

    @Override
    public int saveActivityRemark(ActivityRemarkQuery activityRemarkQuery) {

        TActivityRemark tActivityRemark = new TActivityRemark();

        BeanUtils.copyProperties(activityRemarkQuery, tActivityRemark);

        tActivityRemark.setCreateTime(new Date());
        tActivityRemark.setCreateBy(LoginInfoUtil.getCurrentLoginUser().getId());

        return tActivityRemarkMapper.insertSelective(tActivityRemark);
    }


    /**
     * 分页查询
     * @param current
     * @param activityId
     * @return
     */
    @Override
    public PageInfo<TActivityRemark> selectActivityRemarkByPage(Integer current, Integer activityId) {
        PageHelper.startPage(current, Constant.PAGE_SIZE);
        List<TActivityRemark> tActivityRemarkList =  tActivityRemarkMapper.selectByPage(activityId);
        PageInfo<TActivityRemark> pageInfo = new PageInfo<>(tActivityRemarkList);
        return pageInfo;
    }
}
