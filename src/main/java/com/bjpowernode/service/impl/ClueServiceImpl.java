package com.bjpowernode.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.excel.EasyExcel;
import com.bjpowernode.constant.Constant;
import com.bjpowernode.entity.TClue;
import com.bjpowernode.entity.TClueEditLog;
import com.bjpowernode.entity.TCustomer;
import com.bjpowernode.entity.TTran;
import com.bjpowernode.mapper.TClueEditLogMapper;
import com.bjpowernode.mapper.TClueMapper;
import com.bjpowernode.mapper.TCustomerMapper;
import com.bjpowernode.mapper.TTranMapper;
import com.bjpowernode.query.BaseQuery;
import com.bjpowernode.query.ClueConvertQuery;
import com.bjpowernode.query.ClueExcel;
import com.bjpowernode.query.ClueQuery;
import com.bjpowernode.service.ClueService;
import com.bjpowernode.util.LoginInfoUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ClueServiceImpl implements ClueService {

  private static final SimpleDateFormat DT_FMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  @Resource
  private TClueMapper tClueMapper;
  @Resource
  private TClueEditLogMapper tClueEditLogMapper;
  @Resource
  private TCustomerMapper tCustomerMapper;
  @Resource
  private TTranMapper tTranMapper;

  @Override
  public PageInfo<TClue> getClueByPage(Integer current) {
    PageHelper.startPage(current, Constant.PAGE_SIZE);
    return new PageInfo<>(tClueMapper.selectByPage(new BaseQuery()));
  }

  @Override
  public TClue getClueById(Integer id) {
    return tClueMapper.selectDetailById(id);
  }

  @Override
  public List<TClueEditLog> listEditLogs(Integer clueId) {
    return tClueEditLogMapper.selectByClueId(clueId);
  }

  @Override
  public int addClue(ClueQuery q) {
    validateNamePhone(q);
    TClue clue = buildFromQuery(q);
    clue.setCreateTime(new Date());
    clue.setCreateBy(LoginInfoUtil.getCurrentLoginUser().getId());
    if (!StringUtils.hasText(clue.getClueStatus())) {
      clue.setClueStatus(TClue.STATUS_UNCONTACTED);
    }
    return tClueMapper.insertSelective(clue);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public int editClue(ClueQuery q) {
    if (q == null || q.getId() == null) {
      return 0;
    }
    validateNamePhone(q);
    TClue old = tClueMapper.selectByPrimaryKey(q.getId());
    if (old == null) {
      throw new RuntimeException("线索不存在");
    }
    if (TClue.STATUS_CONVERTED.equals(old.getClueStatus())) {
      throw new RuntimeException("已转客户的线索不能编辑");
    }
    TClue neu = buildFromQuery(q);
    neu.setId(q.getId());
    String change = buildChangeLog(old, neu);
    int rows = tClueMapper.updateByPrimaryKeySelective(neu);
    if (rows > 0 && StringUtils.hasText(change)) {
      saveEditLog(neu.getId(), change);
    }
    return rows;
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public int deleteClue(Integer id) {
    if (id == null) {
      return 0;
    }
    TClue clue = tClueMapper.selectByPrimaryKey(id);
    if (clue == null) {
      return 0;
    }
    tClueEditLogMapper.deleteByClueId(id);
    return tClueMapper.deleteByPrimaryKey(id);
  }

  @Override
  public void exportExcel(List<String> idList, OutputStream outputStream) {
    List<TClue> list = tClueMapper.selectByPageForExport(new BaseQuery(), idList);
    List<ClueExcel> rows = new ArrayList<>();
    for (TClue c : list) {
      ClueExcel row = new ClueExcel();
      row.setName(c.getName());
      row.setPhone(c.getPhone());
      row.setAge(c.getAge());
      row.setIntentionCourse(c.getIntentionCourse());
      row.setIntentionStrength(c.getIntentionStrength());
      row.setSource(c.getSource());
      row.setClueStatus(c.getClueStatus());
      row.setTrialClassTime(c.getTrialClassTime());
      row.setRemark(c.getRemark());
      row.setCreateTime(c.getCreateTime());
      row.setCreateByName(c.getCreateByDO() != null ? c.getCreateByDO().getName() : "");
      rows.add(row);
    }
    EasyExcel.write(outputStream, ClueExcel.class).sheet("线索列表").doWrite(rows);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void convertToCustomer(ClueConvertQuery q) {
    if (q == null || q.getClueId() == null) {
      throw new RuntimeException("线索ID不能为空");
    }
    if (!StringUtils.hasText(q.getName())) {
      throw new RuntimeException("姓名不能为空");
    }
    if (!StringUtils.hasText(q.getPhone())) {
      throw new RuntimeException("电话不能为空");
    }
    if (q.getTranMoney() == null || q.getTranMoney().compareTo(BigDecimal.ZERO) < 0) {
      throw new RuntimeException("请填写有效的交易金额");
    }

    TClue clue = tClueMapper.selectByPrimaryKey(q.getClueId());
    if (clue == null) {
      throw new RuntimeException("线索不存在，无法转换");
    }
    if (TClue.STATUS_CONVERTED.equals(clue.getClueStatus())) {
      throw new RuntimeException("该线索已转为客户，请勿重复操作");
    }

    Date now = new Date();
    Integer customerCreateBy = q.getCreateBy() != null ? q.getCreateBy() : clue.getCreateBy();
    if (customerCreateBy == null) {
      customerCreateBy = LoginInfoUtil.getCurrentLoginUser().getId();
    }

    TCustomer customer = new TCustomer();
    customer.setClueId(q.getClueId());
    customer.setName(q.getName().trim());
    customer.setPhone(q.getPhone().trim());
    customer.setAge(q.getAge());
    customer.setCourseType(trimToNull(q.getCourseType()));
    customer.setRemainingLessons(q.getRemainingLessons());
    customer.setCourseExpireTime(q.getCourseExpireTime());
    customer.setRemark(trimToNull(q.getRemark()));
    customer.setSource(trimToNull(q.getSource()));
    customer.setStudying(q.getStudying() != null ? q.getStudying() : 1);
    customer.setCreateBy(customerCreateBy);
    customer.setCreateTime(now);

    int customerRows = tCustomerMapper.insertSelective(customer);
    if (customerRows <= 0 || customer.getId() == null) {
      throw new RuntimeException("创建客户失败");
    }

    TClue updateClue = new TClue();
    updateClue.setId(q.getClueId());
    updateClue.setClueStatus(TClue.STATUS_CONVERTED);
    int clueRows = tClueMapper.updateByPrimaryKeySelective(updateClue);
    if (clueRows <= 0) {
      throw new RuntimeException("更新线索状态失败");
    }

    TTran tran = new TTran();
    tran.setTranNo(String.valueOf(IdUtil.getSnowflakeNextId()));
    tran.setCustomerId(customer.getId());
    tran.setStudentName(customer.getName());
    tran.setMoney(q.getTranMoney());
    tran.setCourseType(customer.getCourseType());
    tran.setTranRemark(trimToNull(q.getTranRemark()));
    tran.setDealTime(now);
    tran.setCreateBy(LoginInfoUtil.getCurrentLoginUser().getId());
    tran.setCreateTime(now);

    int tranRows = tTranMapper.insertSelective(tran);
    if (tranRows <= 0) {
      throw new RuntimeException("创建交易失败");
    }
  }

  private void validateNamePhone(ClueQuery q) {
    if (!StringUtils.hasText(q.getName())) {
      throw new RuntimeException("姓名不能为空");
    }
    if (!StringUtils.hasText(q.getPhone())) {
      throw new RuntimeException("电话不能为空");
    }
  }

  private TClue buildFromQuery(ClueQuery q) {
    TClue c = new TClue();
    c.setName(q.getName().trim());
    c.setPhone(q.getPhone().trim());
    c.setAge(q.getAge());
    c.setIntentionCourse(trimToNull(q.getIntentionCourse()));
    c.setIntentionStrength(q.getIntentionStrength());
    c.setSource(trimToNull(q.getSource()));
    c.setRemark(trimToNull(q.getRemark()));
    c.setTrialClassTime(q.getTrialClassTime());
    c.setClueStatus(q.getClueStatus());
    return c;
  }

  private String trimToNull(String s) {
    return StringUtils.hasText(s) ? s.trim() : null;
  }

  private void saveEditLog(Integer clueId, String content) {
    TClueEditLog log = new TClueEditLog();
    log.setClueId(clueId);
    log.setEditBy(LoginInfoUtil.getCurrentLoginUser().getId());
    log.setEditTime(new Date());
    log.setChangeContent(content);
    tClueEditLogMapper.insert(log);
  }

  private String buildChangeLog(TClue old, TClue neu) {
    StringBuilder sb = new StringBuilder();
    appendChange(sb, "姓名", old.getName(), neu.getName());
    appendChange(sb, "电话", old.getPhone(), neu.getPhone());
    appendChange(sb, "年龄", fmt(old.getAge()), fmt(neu.getAge()));
    appendChange(sb, "意向课程", old.getIntentionCourse(), neu.getIntentionCourse());
    appendChange(sb, "意向强度", fmt(old.getIntentionStrength()), fmt(neu.getIntentionStrength()));
    appendChange(sb, "来源", old.getSource(), neu.getSource());
    appendChange(sb, "体验课时间", fmtDate(old.getTrialClassTime()), fmtDate(neu.getTrialClassTime()));
    appendChange(sb, "线索状态", old.getClueStatus(), neu.getClueStatus());
    appendChange(sb, "备注", old.getRemark(), neu.getRemark());
    return sb.toString().trim();
  }

  private void appendChange(StringBuilder sb, String label, String o, String n) {
    String oldVal = o == null ? "" : o;
    String newVal = n == null ? "" : n;
    if (Objects.equals(oldVal, newVal)) {
      return;
    }
    if (sb.length() > 0) {
      sb.append("；");
    }
    sb.append(label).append("：由「").append(oldVal).append("」改为「").append(newVal).append("」");
  }

  private String fmt(Object v) {
    return v == null ? "" : String.valueOf(v);
  }

  private String fmtDate(Date d) {
    return d == null ? "" : DT_FMT.format(d);
  }
}
