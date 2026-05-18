package com.bjpowernode.service.impl;

import com.alibaba.excel.EasyExcel;
import com.bjpowernode.constant.Constant;
import com.bjpowernode.entity.TCustomer;
import com.bjpowernode.entity.TCustomerEditLog;
import com.bjpowernode.mapper.TCustomerEditLogMapper;
import com.bjpowernode.mapper.TCustomerMapper;
import com.bjpowernode.mapper.TTranMapper;
import com.bjpowernode.query.BaseQuery;
import com.bjpowernode.query.CustomerExcel;
import com.bjpowernode.query.CustomerQuery;
import com.bjpowernode.service.CustomerService;
import com.bjpowernode.util.LoginInfoUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class CustomerServiceImpl implements CustomerService {

  private static final SimpleDateFormat DT_FMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  @Resource
  private TCustomerMapper tCustomerMapper;

  @Resource
  private TCustomerEditLogMapper tCustomerEditLogMapper;

  @Resource
  private TTranMapper tTranMapper;

  @Override
  public PageInfo<TCustomer> getCustomersByPage(Integer current) {
    PageHelper.startPage(current, Constant.PAGE_SIZE);
    List<TCustomer> list = tCustomerMapper.selectByPage(new BaseQuery(), null);
    return new PageInfo<>(list);
  }

  @Override
  public void exportExcel(List<String> idList, OutputStream outputStream) {
    List<TCustomer> list = tCustomerMapper.selectByPage(new BaseQuery(), idList);
    List<CustomerExcel> rows = new ArrayList<>();
    for (TCustomer c : list) {
      CustomerExcel row = new CustomerExcel();
      row.setName(c.getName());
      row.setPhone(c.getPhone());
      row.setAge(c.getAge());
      row.setCourseType(c.getCourseType());
      row.setRemainingLessons(c.getRemainingLessons());
      row.setCourseExpireTime(c.getCourseExpireTime());
      row.setSource(c.getSource());
      row.setStudyingText(studyingText(c.getStudying()));
      row.setRemark(c.getRemark());
      row.setCreateTime(c.getCreateTime());
      row.setCreateByName(ObjectUtils.isEmpty(c.getCreateByDO()) ? "" : c.getCreateByDO().getName());
      rows.add(row);
    }
    EasyExcel.write(outputStream, CustomerExcel.class).sheet("学员列表").doWrite(rows);
  }

  @Override
  public TCustomer queryCustomerByCustomerId(Integer customerId) {
    return tCustomerMapper.selectDetailById(customerId);
  }

  @Override
  public List<TCustomerEditLog> listEditLogs(Integer customerId) {
    return tCustomerEditLogMapper.selectByCustomerId(customerId);
  }

  @Override
  public int addCustomer(CustomerQuery customerQuery) {
    validateNamePhone(customerQuery);
    TCustomer customer = buildFromQuery(customerQuery);
    customer.setCreateTime(new Date());
    customer.setCreateBy(LoginInfoUtil.getCurrentLoginUser().getId());
    if (customer.getStudying() == null) {
      customer.setStudying(1);
    }
    return tCustomerMapper.insertSelective(customer);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public int editCustomer(CustomerQuery customerQuery) {
    if (customerQuery == null || customerQuery.getId() == null) {
      return 0;
    }
    validateNamePhone(customerQuery);
    TCustomer old = tCustomerMapper.selectByPrimaryKey(customerQuery.getId());
    if (old == null) {
      return 0;
    }
    TCustomer neu = buildFromQuery(customerQuery);
    neu.setId(customerQuery.getId());
    neu.setCreateBy(old.getCreateBy());
    neu.setCreateTime(old.getCreateTime());
    neu.setClueId(old.getClueId());

    String changeContent = buildChangeLog(old, neu);
    int rows = tCustomerMapper.updateByPrimaryKeySelective(neu);
    if (rows > 0 && StringUtils.hasText(changeContent)) {
      TCustomerEditLog log = new TCustomerEditLog();
      log.setCustomerId(neu.getId());
      log.setEditBy(LoginInfoUtil.getCurrentLoginUser().getId());
      log.setEditTime(new Date());
      log.setChangeContent(changeContent);
      tCustomerEditLogMapper.insert(log);
    }
    return rows;
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public int deleteCustomer(Integer id) {
    if (id == null) {
      return 0;
    }
    tTranMapper.deleteByCustomerId(id);
    tCustomerEditLogMapper.deleteByCustomerId(id);
    return tCustomerMapper.deleteByPrimaryKey(id);
  }

  private void validateNamePhone(CustomerQuery q) {
    if (!StringUtils.hasText(q.getName())) {
      throw new RuntimeException("姓名不能为空");
    }
    if (!StringUtils.hasText(q.getPhone())) {
      throw new RuntimeException("电话不能为空");
    }
  }

  private TCustomer buildFromQuery(CustomerQuery q) {
    TCustomer c = new TCustomer();
    c.setName(q.getName().trim());
    c.setPhone(q.getPhone().trim());
    c.setAge(q.getAge());
    c.setCourseType(trimToNull(q.getCourseType()));
    c.setRemainingLessons(q.getRemainingLessons());
    c.setCourseExpireTime(q.getCourseExpireTime());
    c.setRemark(trimToNull(q.getRemark()));
    c.setSource(trimToNull(q.getSource()));
    c.setStudying(q.getStudying());
    c.setClueId(q.getClueId());
    return c;
  }

  private String trimToNull(String s) {
    if (!StringUtils.hasText(s)) {
      return null;
    }
    return s.trim();
  }

  private String studyingText(Integer studying) {
    if (studying == null) {
      return "";
    }
    return studying == 1 ? "是" : "否";
  }

  private String buildChangeLog(TCustomer old, TCustomer neu) {
    StringBuilder sb = new StringBuilder();
    appendChange(sb, "姓名", old.getName(), neu.getName());
    appendChange(sb, "电话", old.getPhone(), neu.getPhone());
    appendChange(sb, "年龄", fmt(old.getAge()), fmt(neu.getAge()));
    appendChange(sb, "课程类型", old.getCourseType(), neu.getCourseType());
    appendChange(sb, "剩余课时", fmt(old.getRemainingLessons()), fmt(neu.getRemainingLessons()));
    appendChange(sb, "课程到期时间", fmtDate(old.getCourseExpireTime()), fmtDate(neu.getCourseExpireTime()));
    appendChange(sb, "来源", old.getSource(), neu.getSource());
    appendChange(sb, "是否正在学习", studyingText(old.getStudying()), studyingText(neu.getStudying()));
    appendChange(sb, "备注", old.getRemark(), neu.getRemark());
    return sb.toString().trim();
  }

  private void appendChange(StringBuilder sb, String label, String oldVal, String newVal) {
    String o = oldVal == null ? "" : oldVal;
    String n = newVal == null ? "" : newVal;
    if (Objects.equals(o, n)) {
      return;
    }
    if (sb.length() > 0) {
      sb.append("；");
    }
    sb.append(label).append("：由「").append(o).append("」改为「").append(n).append("」");
  }

  private String fmt(Object v) {
    return v == null ? "" : String.valueOf(v);
  }

  private String fmtDate(Date d) {
    return d == null ? "" : DT_FMT.format(d);
  }
}
