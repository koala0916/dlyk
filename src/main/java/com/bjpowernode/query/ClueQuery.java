package com.bjpowernode.query;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ClueQuery {

  private Integer id;
  private String name;
  private Integer age;
  private String phone;
  private String intentionCourse;
  private Integer intentionStrength;
  private String source;
  private String remark;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date trialClassTime;
  private String clueStatus;
}
