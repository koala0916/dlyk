package com.bjpowernode.query;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class CustomerExcel {

  @ExcelProperty("姓名")
  private String name;

  @ExcelProperty("电话")
  private String phone;

  @ExcelProperty("年龄")
  private Integer age;

  @ExcelProperty("课程类型")
  private String courseType;

  @ExcelProperty("剩余课时")
  private Integer remainingLessons;

  @ExcelProperty("课程到期时间")
  private Date courseExpireTime;

  @ExcelProperty("来源")
  private String source;

  @ExcelProperty("是否正在学习")
  private String studyingText;

  @ExcelProperty("备注")
  private String remark;

  @ExcelProperty("创建时间")
  private Date createTime;

  @ExcelProperty("创建人")
  private String createByName;
}
