package com.bjpowernode.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.bjpowernode.entity.TClue;
import com.bjpowernode.mapper.TClueMapper;
import com.bjpowernode.query.ClueExcel;

import java.util.Date;
import java.util.List;

/**
 * Excel 导入线索（按需使用）
 */
public class ClueExcelListener implements ReadListener<ClueExcel> {

  private static final int BATCH_COUNT = 100;
  private List<ClueExcel> clueExcelList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
  private final TClueMapper tClueMapper;
  private final Integer loginUserId;

  public ClueExcelListener(TClueMapper tClueMapper, Integer loginUserId) {
    this.tClueMapper = tClueMapper;
    this.loginUserId = loginUserId;
  }

  @Override
  public void invoke(ClueExcel excel, AnalysisContext context) {
    clueExcelList.add(excel);
    if (clueExcelList.size() >= BATCH_COUNT) {
      saveBatch();
    }
  }

  @Override
  public void doAfterAllAnalysed(AnalysisContext context) {
    saveBatch();
  }

  private void saveBatch() {
    for (ClueExcel excel : clueExcelList) {
      TClue clue = new TClue();
      clue.setName(excel.getName());
      clue.setPhone(excel.getPhone());
      clue.setAge(excel.getAge());
      clue.setIntentionCourse(excel.getIntentionCourse());
      clue.setIntentionStrength(excel.getIntentionStrength());
      clue.setSource(excel.getSource());
      clue.setRemark(excel.getRemark());
      clue.setTrialClassTime(excel.getTrialClassTime());
      clue.setClueStatus(excel.getClueStatus() != null ? excel.getClueStatus() : TClue.STATUS_UNCONTACTED);
      clue.setCreateBy(loginUserId);
      clue.setCreateTime(new Date());
      tClueMapper.insertSelective(clue);
    }
    clueExcelList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
  }
}
