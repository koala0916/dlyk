package com.bjpowernode.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.excel.EasyExcel;
import com.bjpowernode.constant.Constant;
import com.bjpowernode.entity.TTran;
import com.bjpowernode.mapper.TTranMapper;
import com.bjpowernode.query.BaseQuery;
import com.bjpowernode.query.TranExcel;
import com.bjpowernode.query.TranQuery;
import com.bjpowernode.service.TranService;
import com.bjpowernode.util.LoginInfoUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TranServiceImpl implements TranService {

  @Resource
  private TTranMapper tTranMapper;

  @Override
  public PageInfo<TTran> getTrans(Integer current, com.bjpowernode.query.TranSearchQuery search) {
    PageHelper.startPage(current, Constant.PAGE_SIZE);
    return new PageInfo<>(tTranMapper.selectPage(new BaseQuery(), search));
  }

  @Override
  public TTran getTranById(Integer id) {
    return tTranMapper.selectDetailById(id);
  }

  @Override
  public int addTran(TranQuery q) {
    TTran tran = buildFromQuery(q);
    tran.setTranNo(String.valueOf(IdUtil.getSnowflakeNextId()));
    Date now = new Date();
    if (tran.getDealTime() == null) {
      tran.setDealTime(now);
    }
    tran.setCreateTime(now);
    tran.setCreateBy(LoginInfoUtil.getCurrentLoginUser().getId());
    return tTranMapper.insertSelective(tran);
  }

  @Override
  public int editTran(TranQuery q) {
    if (q == null || q.getId() == null) {
      return 0;
    }
    TTran tran = buildFromQuery(q);
    tran.setId(q.getId());
    return tTranMapper.updateByPrimaryKeySelective(tran);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public int deleteTran(Integer id) {
    if (id == null) {
      return 0;
    }
    return tTranMapper.deleteByPrimaryKey(id);
  }

  @Override
  public void exportExcel(List<String> idList, OutputStream outputStream) {
    List<TTran> list = tTranMapper.selectPageForExport(new BaseQuery(), idList);
    List<TranExcel> rows = new ArrayList<>();
    for (TTran t : list) {
      TranExcel row = new TranExcel();
      row.setTranNo(t.getTranNo());
      row.setStudentName(t.getStudentName());
      row.setMoney(t.getMoney());
      row.setCourseType(t.getCourseType());
      row.setDealTime(t.getDealTime());
      row.setTranRemark(t.getTranRemark());
      row.setCreateByName(t.getCreateByDO() != null ? t.getCreateByDO().getName() : "");
      rows.add(row);
    }
    EasyExcel.write(outputStream, TranExcel.class).sheet("交易列表").doWrite(rows);
  }

  private TTran buildFromQuery(TranQuery q) {
    TTran t = new TTran();
    t.setCustomerId(q.getCustomerId());
    t.setStudentName(StringUtils.hasText(q.getStudentName()) ? q.getStudentName().trim() : null);
    t.setMoney(q.getMoney());
    t.setDealTime(q.getDealTime());
    t.setCourseType(StringUtils.hasText(q.getCourseType()) ? q.getCourseType().trim() : null);
    t.setTranRemark(StringUtils.hasText(q.getTranRemark()) ? q.getTranRemark().trim() : null);
    return t;
  }
}
