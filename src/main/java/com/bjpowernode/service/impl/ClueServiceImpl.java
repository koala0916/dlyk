package com.bjpowernode.service.impl;

import com.alibaba.excel.EasyExcel;
import com.bjpowernode.constant.Constant;
import com.bjpowernode.entity.TClue;
import com.bjpowernode.listener.ClueExcelListener;
import com.bjpowernode.mapper.TClueMapper;
import com.bjpowernode.query.BaseQuery;
import com.bjpowernode.query.ClueExcel;
import com.bjpowernode.service.ClueService;
import com.bjpowernode.util.LoginInfoUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class ClueServiceImpl implements ClueService{

    @Resource
    private TClueMapper  clueMapper;

    @Override
    public PageInfo<TClue> getClueByPage(Integer current) {
        PageHelper.startPage(current, Constant.PAGE_SIZE);

        List<TClue> tClueList =clueMapper.selectByPage(new BaseQuery());

        PageInfo<TClue> tCluePageInfo = new PageInfo<>(tClueList);

        return tCluePageInfo;
    }

    @Override
    public void importExcel(InputStream inputStream) {

        Integer loginUserId = LoginInfoUtil.getCurrentLoginUser().getId();
        EasyExcel.read(inputStream, ClueExcel.class, new ClueExcelListener(clueMapper, loginUserId)).sheet().doRead();
    }

    @Override
    public TClue getClueById(Integer id) {
        return clueMapper.selectClueDetailById(id);
    }
}
