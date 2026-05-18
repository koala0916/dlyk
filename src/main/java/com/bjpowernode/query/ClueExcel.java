package com.bjpowernode.query;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ClueExcel {

  @ExcelProperty("姓名")
  private String name;
  @ExcelProperty("电话")
  private String phone;
  @ExcelProperty("年龄")
  private Integer age;
  @ExcelProperty("意向课程")
  private String intentionCourse;
  @ExcelProperty("意向强度")
  private Integer intentionStrength;
  @ExcelProperty("来源")
  private String source;
  @ExcelProperty("线索状态")
  private String clueStatus;
  @ExcelProperty("体验课时间")
  private Date trialClassTime;
  @ExcelProperty("备注")
  private String remark;
  @ExcelProperty("创建时间")
  private Date createTime;
  @ExcelProperty("创建人")
  private String createByName;
}
