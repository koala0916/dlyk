package com.bjpowernode.service.impl;


import com.bjpowernode.entity.TTran;
import com.bjpowernode.entity.TTranHistory;
import com.bjpowernode.mapper.TTranHistoryMapper;
import com.bjpowernode.mapper.TTranMapper;
import com.bjpowernode.query.TranHistoryQuery;
import com.bjpowernode.service.TranHistoryService;
import com.bjpowernode.util.LoginInfoUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class TranHistoryServiceImpl implements TranHistoryService {


    @Resource
    private TTranHistoryMapper tTranHistoryMapper;

    @Resource
    private TTranMapper  tTranMapper;

    /**
     * 1.向交易历史记录中添加数据
     * 2.修改交易表中的stage
     */
    @Transactional
    @Override
    public boolean modifyTranStage(TranHistoryQuery tranHistoryQuery) {
        //1.向交易历史记录中添加数据
        TTranHistory tTranHistory = new TTranHistory();

        BeanUtils.copyProperties(tranHistoryQuery, tTranHistory);

        tTranHistory.setCreateTime(new Date());
        tTranHistory.setCreateBy(LoginInfoUtil.getCurrentLoginUser().getId());

        int insertCount = tTranHistoryMapper.insertSelective(tTranHistory);


        //2.修改交易表中的stage
        TTran tTran = new TTran();
        tTran.setId(tranHistoryQuery.getTranId());
        tTran.setStage(tranHistoryQuery.getStage());
        tTran.setExpectedDate(tranHistoryQuery.getExpectedDate());
        tTran.setEditTime(new Date());
        tTran.setEditBy(LoginInfoUtil.getCurrentLoginUser().getId());

        int updateCount = tTranMapper.updateByPrimaryKeySelective(tTran);

        return insertCount > 0 && updateCount > 0;
    }
}
