package com.bjpowernode.query;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TranExcel {

  @ExcelProperty("流水号")
  private String tranNo;
  @ExcelProperty("姓名")
  private String studentName;
  @ExcelProperty("交易金额")
  private BigDecimal money;
  @ExcelProperty("课程类型")
  private String courseType;
  @ExcelProperty("成交时间")
  private Date dealTime;
  @ExcelProperty("交易备注")
  private String tranRemark;
  @ExcelProperty("创建人")
  private String createByName;
}
