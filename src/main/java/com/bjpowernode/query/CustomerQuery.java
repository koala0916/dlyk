package com.bjpowernode.query;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 客户（学员）新增、编辑、线索转客户参数
 */
@Data
public class CustomerQuery {

  private Integer id;
  private Integer clueId;
  private String name;
  private String phone;
  private Integer age;
  private String courseType;
  private Integer remainingLessons;
  /** 表单提交为字符串，需指定格式才能绑定为 Date */
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date courseExpireTime;
  private String remark;
  private String source;
  /** 是否正在学习 0否 1是 */
  private Integer studying;
}
