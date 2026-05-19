package com.bjpowernode.query;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ClueSearchQuery {

  private String name;
  private String phone;
  private String intentionCourse;
  private String source;
  private String remark;
  private String clueStatus;
  private Integer createBy;

  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTimeStart;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTimeEnd;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTimePoint;
  private String createTimePointType;

  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date trialClassTimeStart;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date trialClassTimeEnd;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date trialClassTimePoint;
  private String trialClassTimePointType;

  private String ageOp;
  private Integer ageValue;
  private Integer ageMin;
  private Integer ageMax;

  private String intentionStrengthOp;
  private Integer intentionStrengthValue;
  private Integer intentionStrengthMin;
  private Integer intentionStrengthMax;
}
