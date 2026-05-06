package com.bjpowernode.service.impl;


import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.bjpowernode.constant.Constant;
import com.bjpowernode.entity.TTran;
import com.bjpowernode.mapper.TTranMapper;
import com.bjpowernode.query.BaseQuery;
import com.bjpowernode.query.TranQuery;
import com.bjpowernode.service.TranService;
import com.bjpowernode.util.LoginInfoUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TranServiceImpl implements TranService {

    @Resource
    private TTranMapper tTranMapper;

    /**
     * 添加交易
     *
     * @param tranQuery
     * @return
     */
    @Override
    public int addTran(TranQuery tranQuery) {
        TTran tTran = new TTran();
        BeanUtils.copyProperties(tranQuery, tTran);

        //交易流水号 1.不能重复  2.有规律
        long tranId = IdUtil.getSnowflakeNextId();

        tTran.setTranNo(String.valueOf(tranId));

        tTran.setCreateTime(new Date());
        tTran.setCreateBy(LoginInfoUtil.getCurrentLoginUser().getId());
        return tTranMapper.insertSelective(tTran);
    }

    @Override
    public PageInfo<TTran> getTrans(Integer current) {
        PageHelper.startPage(current, Constant.PAGE_SIZE);
        List<TTran> tTranList = tTranMapper.selectPage(new BaseQuery());
        PageInfo<TTran> tTranPageInfo = new PageInfo<>(tTranList);

        return tTranPageInfo;
    }

    @Override
    public TTran getTranById(Integer id) {
        return tTranMapper.selectTranById(id);
    }
}
