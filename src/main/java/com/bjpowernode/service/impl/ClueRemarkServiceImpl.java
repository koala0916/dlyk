package com.bjpowernode.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.bjpowernode.entity.TClueRemark;
import com.bjpowernode.mapper.TClueRemarkMapper;
import com.bjpowernode.mapper.TDicValueMapper;
import com.bjpowernode.query.ClueRemarkQuery;
import com.bjpowernode.service.ClueRemarkService;
import com.bjpowernode.util.LoginInfoUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ClueRemarkServiceImpl implements ClueRemarkService {

    @Resource
    private TClueRemarkMapper tClueRemarkMapper;

    @Override
    public int addClueRemark(ClueRemarkQuery clueRemarkQuery) {
        TClueRemark tClueRemark = new TClueRemark();

        BeanUtil.copyProperties(clueRemarkQuery, tClueRemark);

        tClueRemark.setCreateTime(new Date());
        tClueRemark.setCreateBy(LoginInfoUtil.getCurrentLoginUser().getId());
        return tClueRemarkMapper.insertSelective(tClueRemark);
    }
}
